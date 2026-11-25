# Dónde está cada cambio · fase 2

Abre la versión vigente de mejora-ia, incluida la extensión social, en Android Studio y usa la vista Project. Los enlaces señalan líneas comprobadas de esta versión; Ctrl+L permite saltar a una línea. main tiene su propia [guía de fase 1](GUIA_CODIGO.md): no conviene atribuirle las líneas de la fase 2.

## Mapa para encontrar conceptos

Cada fila agrupa sinónimos que pueden aparecer durante la exposición.

| Concepto que se busca | Archivo y línea |
| --- | --- |
| API / servicio REST / rutas GET POST PATCH DELETE | [PostApi.kt:4](../app/src/main/java/com/leon/murocampus/data/remote/PostApi.kt#L4) |
| Usuarios / personas / autores reales / GET users | [UsuarioApi.kt:6](../app/src/main/java/com/leon/murocampus/data/remote/UsuarioApi.kt#L6) |
| URL base / Retrofit / cliente único | [RetrofitClient.kt:18](../app/src/main/java/com/leon/murocampus/data/remote/RetrofitClient.kt#L18) |
| Accept / encabezado JSON / interceptor | [AcceptJsonInterceptor.kt:7](../app/src/main/java/com/leon/murocampus/data/remote/AcceptJsonInterceptor.kt#L7) |
| Modelos / JSON / Post Usuario Comentario | [Modelos.kt:3](../app/src/main/java/com/leon/murocampus/model/Modelos.kt#L3) |
| Result / errores HTTP / cancelación del servidor | [PostRepository.kt:37](../app/src/main/java/com/leon/murocampus/data/PostRepository.kt#L37) |
| Búsqueda local / título / resultados | [PostsViewModel.kt:43](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L43) |
| Filtro / userId / persona seleccionada | [PostsViewModel.kt:51](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L51) |
| SavedStateHandle / restaurar búsqueda y filtro | [PostsViewModel.kt:39](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L39) |
| Factory / CreationExtras / restauración de Android | [PostsViewModelFactory.kt:9](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModelFactory.kt#L9) |
| Autores / mapa por id / carga independiente | [PostsViewModel.kt:23](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L23) |
| Paginación / veinte / siguiente página / no duplicar | [PostsViewModel.kt:81](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L81) |
| Generación / carrera de cancelación / carga nueva | [PostsViewModel.kt:38](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L38) |
| Detalle progresivo / post primero / comentarios separados | [PostsViewModel.kt:106](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L106) |
| Comentarios / reintento independiente | [PostsViewModel.kt:140](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L140) |
| Abrir guardada / snapshot sin red | [PostsViewModel.kt:127](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L127) |
| PATCH / editar / validar campos | [PostsViewModel.kt:176](../app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt#L176) |
| DELETE / eliminar / comprobar respuesta HTTP | [PostRepository.kt:30](../app/src/main/java/com/leon/murocampus/data/PostRepository.kt#L30) |
| Room / entidades / tablas de usuarios | [MuroDatabase.kt:21](../app/src/main/java/com/leon/murocampus/data/local/MuroDatabase.kt#L21) |
| DAO / consultas / Upsert | [MuroDatabase.kt:31](../app/src/main/java/com/leon/murocampus/data/local/MuroDatabase.kt#L31) |
| Base de datos / versión 2 / singleton | [MuroDatabase.kt:50](../app/src/main/java/com/leon/murocampus/data/local/MuroDatabase.kt#L50) |
| Migración / conservar datos / versión 1 a 2 | [MuroDatabase.kt:55](../app/src/main/java/com/leon/murocampus/data/local/MuroDatabase.kt#L55) |
| Caché de páginas / pertenencia exacta / copia local | [PostRepositoryConCache.kt:29](../app/src/main/java/com/leon/murocampus/data/PostRepositoryConCache.kt#L29) |
| Caché de comentarios / snapshot vacío / transacción | [PostRepositoryConCache.kt:70](../app/src/main/java/com/leon/murocampus/data/PostRepositoryConCache.kt#L70) |
| SQLite / falla de caché con lectura de red disponible | [PostRepositoryConCache.kt:97](../app/src/main/java/com/leon/murocampus/data/PostRepositoryConCache.kt#L97) |
| Usuarios offline / listado completo no deducido | [UsuarioRepositoryConCache.kt:22](../app/src/main/java/com/leon/murocampus/data/UsuarioRepositoryConCache.kt#L22) |
| Guardadas / JSON persistente / preferencias | [GuardadosRepository.kt:88](../app/src/main/java/com/leon/murocampus/data/GuardadosRepository.kt#L88) |
| Guardadas / alternar sin duplicados / commit antes de publicar | [GuardadosRepository.kt:66](../app/src/main/java/com/leon/murocampus/data/GuardadosRepository.kt#L66) |
| Guardadas / ViewModel / estado y errores | [GuardadosViewModel.kt:13](../app/src/main/java/com/leon/murocampus/viewmodel/GuardadosViewModel.kt#L13) |
| Composición de dependencias / una base / MainActivity | [MainActivity.kt:19](../app/src/main/java/com/leon/murocampus/MainActivity.kt#L19) |
| Muro / feed / conteos / alcance compacto | [PantallaLista.kt:29](../app/src/main/java/com/leon/murocampus/ui/PantallaLista.kt#L29) |
| Filtro / hoja Personas / selección / diez autores | [PantallaLista.kt:73](../app/src/main/java/com/leon/murocampus/ui/PantallaLista.kt#L73) |
| Cabecera compacta / marca / filtro / actualizar | [MuroCampusApp.kt:82](../app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt#L82) |
| Fila social / avatar lateral / título 2 líneas / cuerpo 5 líneas | [Componentes.kt:60](../app/src/main/java/com/leon/murocampus/ui/Componentes.kt#L60) |
| Comentarios / guardar / compartir desde la fila | [Componentes.kt:99](../app/src/main/java/com/leon/murocampus/ui/Componentes.kt#L99) |
| Navegación inferior / Muro Buscar Publicar Guardadas / semántica | [NavegacionSocial.kt:20](../app/src/main/java/com/leon/murocampus/ui/NavegacionSocial.kt#L20) |
| Buscar / teclado / ocultar navegación inferior | [MuroCampusApp.kt:102](../app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt#L102) |
| Entrada desde comentarios / detalle desplegado | [MuroCampusApp.kt:122](../app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt#L122) |
| Iniciales / autor real / marcador guardado | [Comunidad.kt:35](../app/src/main/java/com/leon/murocampus/ui/Comunidad.kt#L35) |
| Texto completo / selección / comentarios progresivos | [PantallaDetalle.kt:22](../app/src/main/java/com/leon/murocampus/ui/PantallaDetalle.kt#L22) |
| Editor / validación / error compacto / salida | [FormularioPost.kt:22](../app/src/main/java/com/leon/murocampus/ui/FormularioPost.kt#L22) |
| Borrador / Atrás / confirmar descarte | [MuroCampusApp.kt:67](../app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt#L67) |
| Compartir / función propia / menú de Android | [CompartirPublicacion.kt:5](../app/src/main/java/com/leon/murocampus/ui/CompartirPublicacion.kt#L5) |
| Vidrio / capas / presión / controles | [GlassControls.kt:41](../app/src/main/java/com/leon/murocampus/ui/GlassControls.kt#L41) |
| Remove animations / escala cero / Android | [GlassControls.kt:27](../app/src/main/java/com/leon/murocampus/ui/GlassControls.kt#L27) |
| Colores / Manrope / tema oscuro | [Theme.kt:28](../app/src/main/java/com/leon/murocampus/ui/theme/Theme.kt#L28) |
| Dependencias / librerías / Room Retrofit Compose | [app/build.gradle.kts:24](../app/build.gradle.kts#L24) |

## Recorrido breve para exponer

1. En `PostApi`, mostrar las rutas y distinguir `@Query`, `@Path` y `@Body`. En `RetrofitClient`, señalar URL base, conversión JSON e interceptor Accept. Las escrituras de JSONPlaceholder son simuladas aunque la respuesta HTTP sea correcta.
2. Seguir la consulta desde `PostsViewModel` hasta el contrato Repository. La implementación remota llama a Retrofit; la implementación con caché decide cuándo usar Room. Compose no llama al servicio.
3. Mostrar las seis mejoras: búsqueda local, PATCH, DELETE, Accept JSON, Room y páginas de veinte. Buscar no hace una llamada por cada letra; cambiar de persona sí consulta su conjunto. El conteo distingue resultados de descargas.
4. Mostrar la organización social: avatar lateral, nombre, username, título y cuerpo originales. Sus medidas están en `FilaPublicacion`; la barra inferior está en `NavegacionSocial` y el filtro de la cabecera abre la hoja Personas. La identidad llega de `GET users` y un mapa por `userId`; un fallo de autores conserva los nombres conocidos y no impide cargar publicaciones.
5. Pulsar comentarios desde una fila: `MuroCampusApp` pasa `mostrarComentarios=true` al detalle. Abrir la lectura normal conserva comentarios a demanda. El post se publica antes de terminar comentarios. La sección puede fallar o reintentar por separado. El asunto `Comentario.name` se presenta como asunto; la identidad disponible es el correo.
6. Distinguir los dos almacenamientos: Room conserva respuestas leídas del servicio; Guardadas guarda snapshots elegidos por la persona como JSON local. Guardadas funciona de verdad en el dispositivo y no declara sincronización remota.
7. En el editor, explicar Autor de ejemplo, enviar y mostrar el título retornado junto al aviso de simulación y Volver al muro. Salir con cambios pide decidir si se conserva el borrador. Editar/eliminar muestran recibo sin fingir un cambio persistente del servidor.

## Conceptos que no son equivalentes

Room es la biblioteca; `MuroDatabase` construye la base. Entity define una tabla, DAO define consultas, singleton conserva una instancia y `version` expresa el esquema. `MIGRACION_1_2` crea la tabla de usuarios sin borrar las otras tablas. `paginas` registra pertenencia exacta para no inventar continuidad de identificadores o un listado de usuarios a partir de consultas sueltas.

`sinConexion` indica que el resultado se recuperó de una copia, no que la app esté monitorizando la radio o el estado físico de red. Lista, detalle y comentarios tienen procedencia independiente. Un fallo de SQLite al guardar no convierte una respuesta de red correcta en un error de conexión.

`SavedStateHandle` restaura filtro y búsqueda al reconstruir el ViewModel con el estado de Android. No significa que se guarden publicaciones, borradores o una cuenta en el servidor.

## Funciones propias y pruebas

Compartir existe desde el avance 11 de fase 1 y sigue enviando título, texto y enlace a la hoja nativa, sin seleccionar un destinatario automáticamente. Guardadas es la función propia añadida en fase 2: marcador, vista Guardadas, lectura sin red y persistencia local completa.

Los tests de HTTP usan MockWebServer; los de ViewModel usan repositorios controlados. Room y preferencias tienen pruebas instrumentadas de cerrar/reabrir y persistencia. `ServicioRealTest` consulta JSONPlaceholder por HTTPS y demuestra los límites de escritura de ese servicio. Los resultados actuales y su alcance están en [Verificación social](VERIFICACION_SOCIAL.md), con 77 pruebas aprobadas; la [verificación histórica de los nueve avances](VERIFICACION_FASE2.md) conserva sus 75 casos. La [galería actual](GALERIA_SOCIAL.md) reúne los 19 estados de esta organización.

Para señalar un avance exacto, abre Git → Log y revisa los archivos de ese commit. Las líneas anteriores describen el cierre final, no el estado de cada paso histórico. Los nueve primeros avances de fase 2 están publicados después de los doce de main y mantienen su evidencia histórica. La extensión social corresponde al décimo avance; su galería y sus comprobaciones deben identificarse por separado.

La búsqueda mantiene el campo compuesto y el foco mientras se escribe; no fuerza un desplazamiento por cada pulsación. Las filas tienen claves estables. Cambiar de persona o abrir la búsqueda restablece el inicio por intención de la persona; el cierre limpia la consulta, el foco y el teclado.
