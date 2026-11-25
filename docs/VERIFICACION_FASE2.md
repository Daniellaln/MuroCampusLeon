# Verificación histórica · nueve avances de fase 2

Este documento conserva el cierre de los nueve primeros avances, anterior a la extensión social. La verificación posterior tiene su propio [registro](VERIFICACION_SOCIAL.md).

**75 pruebas aprobadas: 47 unitarias y 28 en Android**, sin fallos, errores ni omitidas. Los conteos se comprobaron directamente en los XML de esta ejecución y se copiaron a [pruebas_fase2](pruebas_fase2/). El [resumen JSON](pruebas_fase2/resumen.json) registra los archivos y totales.

Compilación y lint finalizaron con `BUILD SUCCESSFUL`: [verificación de UI](pruebas_fase2/verificacion_ui_final.log) y [compilación final del producto](pruebas_fase2/compilacion_producto_final.log). Lint registra **0 errores y 19 advertencias**; el [XML de lint](pruebas_fase2/lint.xml) conserva su detalle. Las advertencias no se presentan como fallos ni se omite su existencia.

## Pruebas unitarias · 47

| Clase | Casos aprobados |
| --- | ---: |
| AcceptJsonTest | 1 |
| AutoresViewModelTest | 6 |
| FaseDosHttpTest | 3 |
| FaseDosViewModelTest | 8 |
| GuardadosViewModelTest | 8 |
| ModelosTest | 4 |
| PostRepositoryTest | 7 |
| PostsViewModelTest | 7 |
| UsuarioRepositoryTest | 3 |

Modelos verifica nombres JSON, campos obligatorios y campos extra. MockWebServer comprueba rutas, parámetros `_start`, `_limit` y `userId`, Accept en todas las operaciones, cuerpo de POST/PATCH y códigos DELETE. HTTP fallido o JSON incompleto producen `Result.failure`; la cancelación se propaga.

Los ViewModels prueban búsqueda sin solicitudes por pulsación, combinación con el filtro, páginas de veinte, fin de lista, reintento sin duplicados y la carrera entre una página cancelada y una lista nueva. `SavedStateHandle` se reconstruye con búsqueda/persona y aplica el filtro desde la primera página. Autores lentos no bloquean posts; se conserva el mapa conocido al fallar y se reintenta por separado. El detalle se publica mientras cargan comentarios, los errores secundarios no borran la lectura y cerrar cancela trabajos pendientes.

Guardadas comprueba carga asíncrona de snapshots completos, alternancia antes de terminar la carga, dos toques pendientes, eliminación idempotente y un fallo de persistencia que conserva el estado anterior. No se toma una lista de identificadores por una copia de lectura completa.

## Pruebas en Android · 28

| Clase | Casos aprobados |
| --- | ---: |
| GuardadosPreferencesTest | 3 |
| MuroCampusTest | 14 |
| RoomCacheTest | 10 |
| ServicioRealTest | 1 |

Room prueba guardar, cerrar, reabrir y leer publicaciones, páginas, comentarios y usuarios. La migración 1→2 conserva posts/comentarios/páginas y añade autores. Los marcadores no permiten inventar una página intermedia ni un listado completo a partir de un usuario suelto. Se verifica el snapshot de cero comentarios, la procedencia independiente de lista/detalle/comentarios y un fallo SQLite real provocado con triggers: los datos de red siguen disponibles y el aviso se limpia al recuperar la escritura.

Las preferencias se prueban con una nueva instancia que recupera `userId`, `id`, título y cuerpo. Quitar persiste; alternar antes de cargar no reemplaza las otras lecturas. Datos ilegibles producen error sin borrarse silenciosamente.

La UI comprueba lista, filtro, regreso conservando lectura, búsqueda local, validación y recuperación del editor, respuesta de editar sin sustituir el original del servidor, confirmación de borrado, salida tras publicar y protección del borrador. Guardar permite abrir y quitar un snapshot sin red. Compartir verifica título, texto y enlace y no envía nada a terceros.

## Servicio real y alcance

`ServicioRealTest` pasó contra JSONPlaceholder por HTTPS: 100 posts, diez por cada usuario, cinco comentarios en las lecturas consultadas, dos páginas distintas de veinte, filtro paginado, 404, POST con id 101, PATCH y DELETE 200. Recargar después de editar/eliminar recuperó la publicación original, confirmando la simulación del servicio. Solo se enviaron datos ficticios de práctica. `GET users` y su mapeo se cubren además con MockWebServer y el recorrido de la app.

Crear, editar y eliminar no prueban persistencia real en el backend. Guardadas sí prueba persistencia real del dispositivo; Room conserva respuestas leídas y no convierte las escrituras simuladas en respuestas remotas auténticas.

El [registro del recorrido](pruebas_fase2/recorrido_producto.json) enumera las 29 capturas y registra la restauración de red, fuente 1.0, orientación vertical y escala de animación 1 al terminar. Se conserva también el log y logcat de ese recorrido.

El entorno fue **Windows y emulador Pixel 8 Pro Android API 37**. No se afirma prueba en Mac, teléfono físico o una sesión de Gemini; no se midió semejanza porcentual con otra herramienta. [La galería](GALERIA_FASE2.md) reúne el recorrido visual; una captura fija acredita el aspecto de un estado y no demuestra por sí sola rendimiento, vibración o duración de una animación.

Los archivos con prefijo `fase2_` en la carpeta de evidencia son registros anteriores conservados. El cierre actual se identifica por los nueve XML unitarios, `instrumentadas.xml`, `resumen.json`, `verificacion_ui_final.log` y `compilacion_producto_final.log`.
