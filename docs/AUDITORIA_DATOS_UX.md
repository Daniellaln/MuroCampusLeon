# Auditoría funcional y de datos de MuroCampus, fase 2

Fecha: 25 de noviembre de 2026. Alcance: lectura del código Kotlin, contratos HTTP y pruebas existentes; trabajo de datos/ViewModel solicitado durante la revisión. No se ejecutó interacción de emulador para evitar interferir con el recorrido principal. Los hallazgos de UI describen el estado encontrado al comenzar; la implementación visual se está resolviendo por separado.

## Datos que ya existen y se pueden aprovechar

`Post` conserva `userId`, `id`, `title` y `body`. No contiene foto, fecha, hora, número de reacciones ni categoría. Las portadas ambientales pueden ser decoración de la app, pero no deben parecer fotografías de la publicación. No conviene fabricar tiempos relativos, actividad reciente o categorías.

`Usuario` ya conserva `id`, `name`, `username` y `email`. `UsuarioApi` tenía `GET users/{id}`, pero `MainActivity` no lo conectaba al muro y las tarjetas, filtros y formularios mostraban «Usuario N». Un solo `GET users` permite resolver autores por `post.userId`, mostrar nombre e iniciales, y conservar un respaldo honesto «Usuario N» cuando falla la consulta. `username` puede ser el dato secundario; el correo no necesita ocupar todas las tarjetas.

`Comentario.name` es el asunto/título del comentario, no un nombre de persona. `Comentario.email` identifica a quien escribe. No se debe presentar el asunto como identidad del autor. Una respuesta puede mostrar inicial derivada del correo, el correo y el cuerpo; el asunto puede aparecer como texto secundario si mejora la lectura.

Los endpoints en uso son `GET posts?_start=&_limit=&userId=`, `GET posts/{id}`, `GET posts/{id}/comments`, `POST posts`, `PATCH posts/{id}` y `DELETE posts/{id}`. Existen además `listar` y `porUsuario` sin paginación para la guía y pruebas. Las escrituras simuladas ya se explicaban y no se deben convertir en persistencia ficticia. Un guardado personal local puede ser una función real, claramente independiente del servidor.

## P1: cambios concretos ya implementados en datos y ViewModel

1. **Autores reales sin bloquear la lectura.** `UsuarioApi.listar`, `UsuarioRepositoryRemote.listar` y `UsuarioRepositoryConCache` consultan y guardan usuarios de la API. `PostsViewModel(repo, usuariosRepo = null)` mantiene compatibilidad con los tests de posts existentes y expone `autores: StateFlow<Map<Int, Usuario>>`, `estadoAutores`, `sinConexionAutores` y `recargarAutores()`. El fallo no borra autores conocidos. Los posts cargan de forma independiente.

2. **Room conserva usuarios al actualizar.** `MuroDatabase` pasa a versión 2 con `UsuarioEntity`, `UsuarioDao` y `MIGRACION_1_2` explícita. No se borra la base. El listado de usuarios conserva un marcador de pertenencia en `paginas` con clave `usuarios:todos`; consultar un usuario suelto no permite fingir que se guardó el listado completo. Los errores HTTP o datos inválidos siguen siendo fallos y no se ocultan con una copia antigua.

3. **Leer una publicación aunque fallen sus comentarios.** Antes, `PostsViewModel.abrir` exigía ambos resultados y convertía todo el detalle en error si los comentarios no estaban disponibles. Ahora publica `DetalleUi(post, emptyList())` en cuanto llega el post. Expone `cargandoComentarios`, `errorComentarios` y `reintentarComentarios(id)`. Cerrar el detalle cancela ambos trabajos y evita resultados tardíos. La UI debe mostrar carga/error dentro de la sección de comentarios, conservando título y cuerpo.

4. **Carrera de paginación cancelada.** `PostsViewModel.cargarMas` podía ejecutar su `finally` después de una nueva actualización y apagar el indicador de otra carga. Una generación de lista impide que la página anterior modifique ese estado. La cancelación también se comprueba antes de persistir resultados de lectura de Room/red.

5. **Un resultado de cero comentarios también es una copia válida.** `PostRepositoryConCache.comentarios` antes solo consideraba válida una lista local no vacía; además hacía upsert y podía conservar respuestas retiradas por una consulta posterior. Ahora reemplaza el snapshot en una transacción y guarda su marcador, incluso vacío. Las copias de versión 1 no vacías continúan siendo utilizables.

6. **UsuarioViewModel conserva la consulta más reciente.** `cargar(id)` cancela la llamada previa y comprueba cancelación antes de publicar. Una respuesta lenta ya no puede sustituir la selección posterior.

7. **Abrir una lectura guardada no exige red.** `PostsViewModel.abrirGuardado(post)` publica el snapshot local inmediatamente, cancela cualquier detalle anterior y deja los comentarios como carga explícita mediante `reintentarComentarios(id)`. No fabrica una respuesta del servidor.

8. **Procedencia independiente y caché opcional.** `PostRepository.origenDetalle` y `origenComentarios` tienen defaults compatibles con fakes. La implementación Room actualiza cada recurso por separado; `sinConexion` ahora representa exclusivamente la lista y conserva su origen mixto al añadir páginas. El ViewModel expone `detalleSinConexion`. Un fallo SQLite al guardar ya no descarta los datos correctos de la red: los repositorios exponen `avisoCache`, y el ViewModel reexpone `avisoCache`/`avisoCacheAutores`. El aviso se limpia al guardar correctamente el recurso; no se absorben excepciones genéricas.

9. **Filtro y búsqueda sobreviven a la reconstrucción del proceso.** `PostsViewModel` acepta un tercer argumento opcional `SavedStateHandle`, inicializa ambos valores desde ese estado y los guarda al modificarlos. La factory usa `CreationExtras.createSavedStateHandle()` para integrarse con Android y conserva `create(Class)` para consumidores anteriores. La primera página de la instancia reconstruida ya lleva el usuario restaurado; la búsqueda sigue siendo local.

Composición necesaria en `MainActivity`: crear una sola `bd = MuroDatabase.obtener(applicationContext)` y pasar a `PostsViewModelFactory` tanto `PostRepositoryConCache(PostRepositoryRemote(RetrofitClient.api), bd)` como `UsuarioRepositoryConCache(UsuarioRepositoryRemote(RetrofitClient.usuarioApi), bd)`.

## P1/P2: recomendaciones para la integración visual

| Prioridad | Archivo y función | Problema encontrado | Corrección acotada |
|---|---|---|---|
| P1 | `PantallaLista`, `PortadaPublicacion`, `Componentes.Autor`, formularios | Números impersonales aunque el servicio ya tiene nombres. | Consumir `vm.autores`, mostrar nombre e iniciales consistentes, conservar fallback por ID y ofrecer reintento discreto de autores. No bloquear el muro por ese fallo. |
| P1 | `PantallaDetalle` | La lectura se interrumpe por consultas auxiliares; el detalle inicialmente recortaba título y cuerpo aun después de entrar. | Usar los nuevos estados independientes de comentarios. Mostrar título completo y cuerpo legible al entrar; reservar expansión para contenido realmente largo. |
| P2 | `PantallaLista` | El contador decía publicaciones descargadas aunque la búsqueda mostrara otra cantidad. El vacío sugería ampliar descarga, pero solo enfatizaba limpiar. | Mostrar «N resultados · M descargadas», mantener aviso del alcance y dar acceso claro a «Cargar más» cuando aún haya páginas. La búsqueda debe seguir siendo local, por título, sin solicitudes por pulsación. |
| P2 | `PortadaPublicacion` | Portadas repetidas ocupan más atención que los datos y el título tiene solo dos líneas. | Priorizar autor, título y un pequeño extracto. Reservar ilustración a una cabecera ambiental o a estados vacíos. Una función real como guardar para leer luego aporta más utilidad que añadir más imágenes. |
| P2 | `Componentes.ErrorConReintento` | «No pudimos conectar» también aparece para HTTP 404 y JSON inválido. | Encabezado genérico «No pudimos cargar esto», detalle específico y acción contextual; comentarios deben reintentar solo comentarios. |
| P2 | `PantallaLista` y navegación | Un cambio de usuario puede mantener una posición de scroll profunda; volver al detalle debe conservar la posición original. | Reiniciar scroll al cambiar filtro/búsqueda por intención explícita; conservarlo al abrir/cerrar lectura. Evitar volver a consultar toda la lista al pulsar Volver. |
| P2 | `PantallaLista.AnimatedVisibility`, `FormularioPost.AnimatedVisibility` | La búsqueda no consultaba `LocalMotionEnabled`; el éxito del formulario solo desactivaba la entrada, no la salida. | Aplicar `EnterTransition.None` y `ExitTransition.None` en ambos caminos cuando se quiten animaciones. Comprobar el sistema en tiempo de ejecución, como ya hace `MotionSettings`. |
| P2 | `PantallaDetalle` y tarjetas | Los estados de expandido/guardado y los resultados cambiantes necesitan semántica accesible. | `stateDescription`/semántica de alternancia, nombres de acción concretos, anuncio breve de error o éxito, título como heading y orden de lectura natural. Conservar áreas táctiles de al menos 48 dp. |

## P2: límites técnicos pendientes que conviene conocer

- Los booleanos de procedencia indican copia local, no monitorizan la conectividad física del dispositivo. La UI debe presentar el aviso junto al recurso correspondiente y distinguir una lectura guardada deliberadamente de una recuperación automática por fallo de red.

## Verificación añadida

- `AutoresViewModelTest`: autores lentos no bloquean posts; reintento y conservación del mapa; lectura progresiva; cancelación y reintento independiente de comentarios.
- `UsuarioRepositoryTest`: una consulta `GET /users`, nombres/campos del servicio, cabecera Accept, rechazo HTTP y propagación de cancelación.
- `FaseDosViewModelTest`: regresión de la carrera entre una página cancelada y otra nueva y reconstrucción con filtro/búsqueda restaurados desde la primera página.
- `RoomCacheTest`: persistencia de autores tras cerrar/reabrir, fallo inválido no ocultado, usuario suelto no equivale a listado, migración 1→2 preserva posts/comentarios/páginas, caché vacía de comentarios, procedencias independientes y fallo SQLite real provocado mediante triggers sin ocultar datos de red.

Las pruebas nuevas se entregan para la compilación y recorrido coordinados. Este informe no afirma resultados de una ejecución que todavía no se ha realizado.
