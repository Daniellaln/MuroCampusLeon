# Prompts

Estos prompts se pueden utilizar uno por uno, siguiendo este orden, para replicar los resultados. Parte del cierre de main. Los recursos necesarios están en esta rama; incorpora primero los recursos del paso 7 antes de ejecutar el prompt visual.

Son instrucciones preparadas para reproducción con Gemini; la implementación y las revisiones de esta entrega fueron realizadas con asistencia de Codex. No se afirma una ejecución de Gemini ni un porcentaje de parecido medido.

## 1. Encabezado json

```text
Compórtate como un experto en Android con Retrofit y OkHttp. Ayúdame a agregar Accept: application/json a todas las solicitudes de MuroCampus mediante AcceptJsonInterceptor en data/remote. Conéctalo al único cliente de RetrofitClient, conserva HTTPS, tiempos de espera y logging solo en debug. No añadas encabezados desde las pantallas. Prueba con MockWebServer que el interceptor se usa y no cambia el cuerpo de las solicitudes. Compila antes de continuar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 1 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Añadí el encabezado Accept JSON a todas las peticiones con un interceptor de OkHttp.

**Reflexión:** Un interceptor evita repetir el encabezado en cada ruta y cubre también las operaciones nuevas.

**Qué comprobar:** GET, POST, PATCH y DELETE contienen Accept: application/json; el cuerpo no cambia.

## 2. Busqueda

```text
Compórtate como un experto en MVVM y StateFlow. Ayúdame a buscar por título en las publicaciones ya descargadas de MuroCampus, sin llamar a la API al escribir. Usa búsqueda sin distinguir mayúsculas, conserva el filtro de usuario y expón resultados desde PostsViewModel. Añade un campo etiquetado con limpiar, un vacío con acción para quitar la búsqueda y un indicador del alcance descargado. Conserva lista, detalle, crear y Compartir. Comprueba que escribir no produce nuevas solicitudes.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 2 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Agregué la búsqueda por título sobre las publicaciones descargadas y la combiné con el filtro de usuario.

**Reflexión:** Buscar en memoria evita una solicitud por tecla; el alcance descargado debe quedar claro.

**Qué comprobar:** Escribir no hace llamadas de red; limpiar restaura lo descargado y el filtro sigue activo.

## 3. Editar

```text
Compórtate como un experto en REST y Jetpack Compose. Necesito editar título y contenido de una publicación existente mediante PATCH posts/{id}. Añade EditarPost serializable, PostApi.editar, PostRepository.editar y el estado de edición en PostsViewModel. La pantalla reutiliza los campos etiquetados, carga el texto actual, deshabilita blancos/doble envío, conserva el borrador ante error y muestra la respuesta real al guardar. No finjas que JSONPlaceholder guarda cambios: informa que es una simulación y al recargar reaparece el original. Retrofit y HttpException solo en data.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 3 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Añadí la edición del título y contenido con PATCH, validación y respuesta visible del servidor.

**Reflexión:** Mostrar la respuesta de PATCH no significa que el servidor de práctica guardó el cambio.

**Qué comprobar:** Campos vacíos deshabilitan guardar; el error conserva texto y el éxito muestra la respuesta.

## 4. Eliminar

```text
Compórtate como un experto en Kotlin, Retrofit y UX de acciones destructivas. Ayúdame a eliminar desde el detalle con confirmación Material 3, Cancelar y Eliminar. PostApi debe usar DELETE posts/{id} y Response<Unit>; el Repository verifica isSuccessful, devuelve Result<Int> con el código real o un error, y relanza CancellationException. PostsViewModel expone carga, éxito y error; la vista nunca recibe Response ni llama al servicio. Tras éxito muestra el código y explica que la API simula la eliminación. No ocultes permanentemente datos que el servidor volverá a entregar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 4 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Agregué eliminar una publicación con confirmación y comprobación del código de respuesta DELETE.

**Reflexión:** Confirmar la acción y revisar el código protege de un borrado accidental y de falsos éxitos.

**Qué comprobar:** Cancelar no envía DELETE; 200/204 son éxito y una respuesta 403 sigue siendo error.

## 5. Cache room

```text
Compórtate como un experto en Room y Repository. Ayúdame a guardar en Room los posts descargados y los comentarios de los detalles ya abiertos. Crea entidades, DAO suspend, MuroDatabase singleton versión 1 y PostRepositoryConCache: consulta red primero y usa la copia local solo ante fallo de conexión, sin ocultar errores HTTP ni de conversión. Indica cuando se está leyendo sin conexión; conserva título/contenido completos y filtros. No almacenes como persistentes las escrituras simuladas de JSONPlaceholder. Prueba cerrar/reabrir Room y consultar offline. Ninguna referencia a Android Context o Room debe llegar al ViewModel.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 5 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Guardé las publicaciones y comentarios consultados en Room para poder leerlos sin conexión.

**Reflexión:** La caché permite seguir leyendo; no debe ocultar errores HTTP ni inventar páginas no consultadas.

**Qué comprobar:** Cierra y reabre Room sin red: solo se leen las páginas y comentarios que ya existían.

## 6. Paginacion

```text
Compórtate como un experto en paginación REST con Kotlin. Ayúdame a cargar veinte posts por página con _start y _limit, combinados con userId cuando corresponda. Añade PostApi.pagina, Repository y PostsViewModel; reinicia offset al cambiar usuario, cancela la petición anterior, conserva lo ya leído al fallar Cargar más, permite reintentar sin duplicar y detén al llegar al final. Buscar filtra solo lo descargado, con esa limitación visible y posibilidad de cargar más. La caché debe cubrir páginas y filtros sin inventar continuidad cuando faltan registros.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 6 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Cambié la carga del muro a páginas de veinte con filtros, fin de lista y reintento sin duplicados.

**Reflexión:** Al fallar una página se conserva lo que ya se estaba leyendo y se reintenta el mismo inicio.

**Qué comprobar:** Carga 20, luego 40, cambia usuario y prueba un fallo de Cargar más sin duplicados.

## 7. Recursos visuales

```text
Compórtate como un experto en recursos de Android. Ayúdame a integrar los recursos versionados de MuroCampus: campus_atrio.png, conversaciones_vacio.png, conversacion_verde.png y conversacion_rosa.png en app/src/main/res/drawable-nodpi; Manrope local en res/font; SVG editables en diseno/iconos y sus VectorDrawable en drawable. Consulta diseno/RECURSOS.md para nombres, procedencia y prompts de imágenes. No sustituyas la foto por un gradiente ni uses emojis para iconos. La ilustración transparente corresponde a vacíos/errores; la foto es ambientación del campus, no la imagen de una publicación de la API. Conserva las licencias de la fuente.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 7 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Creé las imágenes del campus y del estado vacío, los iconos SVG y la tipografía local del rediseño.

**Reflexión:** La foto ambienta el campus; no se atribuye a publicaciones que no incluyen imágenes en la API.

**Qué comprobar:** Los dos PNG se cargan localmente, el vacío conserva transparencia y los iconos son VectorDrawable.

## 8. Diseno animaciones

```text
Compórtate como un experto en diseño Android, Material 3 y MVVM. Necesito replicar la versión final de MuroCampus siguiendo DESIGN.md, docs/capturas_fase2 y docs/AUDITORIA_APLICADA.md. Usa Manrope y la paleta blanco fresco/verde suave/rosa ligero del tema. El muro es un feed de lectura con nombres de GET users, username e iniciales, títulos legibles de hasta tres líneas y marcador Guardar para leer. No repitas portadas ni extractos. Usa campus_atrio como ambiente compacto y conversaciones_vacio para vacíos. Personas filtra por autor; las pestañas Muro/Guardadas separan lo remoto de snapshots locales. Buscar usa solo lo descargado y muestra resultados y alcance. Implementa usuarios en Room versión 2 con MIGRACION_1_2; separa fallos de autores y comentarios sin ocultar posts. GuardadosRepository con JSON completo, IO y Mutex permite volver a leer offline. MainActivity conecta ambos ViewModels; LocalAutores/LocalGuardados/LocalGuardar distribuyen estado y acciones. El detalle muestra título y cuerpo completos, comentarios desplegables y Compartir. El editor protege borradores, reintenta con una sola acción y permite volver al muro tras éxito; PATCH/DELETE/POST siguen siendo simulados y lo explican. GlassButton/GlassIconButton tienen reflejo, presión y resorte, GuardarButton tiene semántica, vibración y animación breve. Respeta 48 dp, contraste, teclado, fuente130%, oscuro, horizontal y animaciones0; ningún movimiento permanente ni refracción inventada. Usa recursos versionados en drawable-nodpi, drawable, font y SVG de diseno/iconos. Prueba persistencia real y cada recorrido antes de continuar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 8 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Convertí el muro en una comunidad con autores reales, guardadas sin conexión y lectura clara, protegí borradores y añadí vidrio y respuestas al tacto.

**Reflexión:** La misma jerarquía y los mismos controles deben cuidar también el detalle y los estados de error.

**Qué comprobar:** Recorre todos los estados con teclado, fuente 130 %, oscuro y animaciones desactivadas.

## 9. Pruebas documentacion

```text
Compórtate como un experto en pruebas y documentación Android. Ayúdame a verificar MuroCampus en mejora-ia: interceptor, búsqueda sin red, PATCH/DELETE y códigos, Room persistente/offline, paginación, cancelación, autores, guardadas locales, protección de borradores, estados de UI y Compartir. Usa MockWebServer y datos ficticios, no envíes nombres personales a la API de práctica. Captura la aplicación real en emulador, incluido tema oscuro, letra grande y sin conexión. Actualiza README, docs/GUIA_CODIGO_FASE2.md con rutas y líneas verificadas, docs/VERIFICACION_FASE2.md, la galería y PROMPTS.md; conserva las respuestas de Para pensar y las capturas de fase 1. Identifica al autor fijo del editor como Autor de ejemplo. No inventes una ejecución de Gemini ni garantías de similitud visual.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 9 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Apliqué la auditoría final, aclaré el autor de ejemplo y documenté las 75 pruebas, las capturas, los prompts y las rutas del código.

**Reflexión:** Compilar no demuestra cada función; las pruebas y las capturas reales documentan su alcance.

**Qué comprobar:** Ejecuta las pruebas documentadas y comprueba que las capturas corresponden al código de esta rama.


## 10. Publicaciones y navegación social

```text
Compórtate como un experto en diseño de aplicaciones Android y experiencia de uso para redes sociales. Ayúdame a reorganizar MuroCampus siguiendo la composición de Threads y X y las capturas de docs/capturas_social, manteniendo el blanco fresco, verde suave, rosa ligero, Manrope y los controles de vidrio de DESIGN.md. Trabaja sobre el cierre anterior de mejora-ia, sin tocar main ni las funciones REST existentes.

Las publicaciones deben ser lo primero que se vea. Quita del inicio el banner de campus, las pestañas superiores y la fila de cuatro personas. En MuroCampusApp usa una cabecera de 56 dp con marca pequeña, filtro de persona bajo demanda y actualizar/información. PantallaLista muestra solo un encabezado de alcance compacto (todas las personas o autor seleccionado y número descargado/resultados) y el feed. No pongas títulos enormes ni portadas decorativas en los posts.

En Componentes.kt reorganiza FilaPublicacion: avatar de iniciales de 36 dp a la izquierda; columna derecha con nombre a 14 sp y usuario a 12 sp; título a 16 sp, semibold y máximo dos líneas; cuerpo a 15 sp y máximo cinco líneas. Usa separación fina entre publicaciones. Autor, título y cuerpo permiten abrir el detalle completo. Debajo alinea tres acciones con áreas de 48 dp: Ver comentarios, Guardar para leer y Compartir publicación. El callback de comentarios abre PantallaDetalle con mostrarComentarios=true; compartir usa el helper nativo existente y maneja ActivityNotFoundException sin cerrar la app. Conserva el snapshot completo persistente de Guardadas. No inventes likes, seguidores, fechas, vistas, recuentos, fotos ni videos que JSONPlaceholder posts no entrega.

Crea NavegacionSocial.kt con una barra inferior de vidrio claro, selección visible y cuatro acciones reales: Muro, Buscar, Publicar y Guardadas. Buscar abre el campo por título dentro del alcance actual, con limpiar y cerrar; Muro cierra la búsqueda sin hacer otra descarga. Publicar abre el editor. Guardadas recupera lecturas del dispositivo. Conserva posiciones de scroll independientes al cambiar entre Muro y Guardadas. La barra se oculta con teclado y no tapa el último post. Usa los SVG de diseno/iconos y VectorDrawable de res/drawable; ic_inicio es la nueva casa de trazo 1.7 y vista 24x24. El vidrio y sus respuestas se dibujan en Compose, no en PNG.

Mantén todos los recorridos existentes: filtro, detalle, comentarios, crear, editar, eliminar con confirmación, caché sin conexión, paginación, borrador protegido y respuestas simuladas honestas. Comprueba que el cuerpo aparece desde el feed, los comentarios se abren directamente desde su acción y buscar/cerrar no vuelve a descargar. Prueba también oscuro, fuente 130%, horizontal, teclado y escala de animaciones cero. Actualiza las capturas reales, guía de rutas y líneas, README, PRODUCT, DESIGN y su sidecar. No presentes capturas de la versión anterior como si fueran de esta organización.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 10 | Pendiente de ejecutar por la estudiante | Implementación y revisión hechas con Codex; se retiraron el banner y los títulos grandes, se incorporó el cuerpo y acciones reales por post | Dar prioridad a las publicaciones y reproducir la organización pedida sin inventar funciones del servicio |

**Qué comprobar:** el primer post aparece bajo la cabecera; su cuerpo es visible, los tres botones funcionan y la barra inferior conserva el contexto de lectura. Consulta docs/GALERIA_SOCIAL.md y docs/VERIFICACION_SOCIAL.md.
