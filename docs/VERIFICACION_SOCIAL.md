# Verificación · diseño final de fase 2 de fase 2

**77 pruebas aprobadas: 47 unitarias y 30 en Android**, sin fallos, errores ni omitidas. Los conteos se comprobaron directamente en los nueve XML unitarios y el XML instrumentado de esta ejecución. Las copias están en [pruebas_social](pruebas_social/); el [resumen JSON](pruebas_social/resumen.json) registra clases, archivos y fechas.

Compilación y lint finalizaron con `BUILD SUCCESSFUL`: [compilación y pruebas unitarias](pruebas_social/social_build.log) y [pruebas instrumentadas y lint](pruebas_social/social_android.log). El [XML de lint](pruebas_social/lint.xml) contiene **0 errores y 19 advertencias**. Esta evidencia corresponde a la versión final de la fase 2; la [verificación histórica de 75 pruebas](VERIFICACION_FASE2.md) se conserva.

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

Se conservan las comprobaciones de modelos JSON, rutas y cuerpos HTTP, Accept, búsqueda local sin llamada por pulsación, filtro combinado, páginas de veinte, cancelación y restauración con SavedStateHandle. Los autores cargan sin bloquear publicaciones; los comentarios tienen carga y error independientes. Guardadas prueba snapshots completos, carga asíncrona, alternancias pendientes y conservación del estado anterior ante una falla de persistencia.

## Pruebas en Android · 30

| Clase | Casos aprobados |
| --- | ---: |
| GuardadosPreferencesTest | 3 |
| MuroCampusTest | 16 |
| RoomCacheTest | 10 |
| ServicioRealTest | 1 |

Dos casos añadidos acreditan la organización social: `elMuroMuestraElCuerpoYLaAccionAbreComentarios` comprueba cuerpo visible y entrada directa a comentarios; `buscarDesdeLaBarraYVolverAlMuroConservaLaDescarga` comprueba búsqueda desde la barra inferior y regreso sin otra descarga. Los otros recorridos de UI mantienen lista, filtro, lectura y regreso, validación, errores del editor, editar sin sustituir el original del servidor, confirmación de eliminación, salida tras publicar, protección del borrador, guardadas sin red y contenido preparado para compartir.

Room comprueba guardar, cerrar, reabrir y leer publicaciones, páginas, comentarios y usuarios; migración 1→2 sin perder datos; pertenencia exacta de páginas; snapshot vacío de comentarios y procedencia independiente. También provoca una falla SQLite mediante triggers para comprobar que la lectura de red se conserva y el aviso se limpia al recuperar la escritura. Preferencias comprueba snapshots completos con una nueva instancia, eliminación persistente, alternancia antes de cargar y manejo de datos ilegibles.

## Servicio real y alcance

`ServicioRealTest` consulta JSONPlaceholder por HTTPS y comprueba consultas, páginas, filtro, comentarios, 404 y respuestas POST/PATCH/DELETE. Recargar después de editar o eliminar recupera el original: el servicio simula escrituras. Crear, editar y eliminar no acreditan persistencia real en el backend. Guardadas sí comprueba persistencia del dispositivo; Room conserva respuestas leídas. Compartir abre o verifica la hoja nativa con título, cuerpo y enlace, sin enviar mensajes a terceros.

El entorno es **Windows y emulador Pixel 8 Pro Android API 37**. No se afirma prueba en Mac, teléfono físico o una sesión de Gemini. La [galería actual](GALERIA_SOCIAL.md) reúne 19 capturas propias: muro, búsqueda, vacío, personas, filtro, comentarios, detalle, compartir, editar, eliminar, guardadas, lectura local sin red, crear, teclado, borrador, fuente a 1.3, oscuro, horizontal y animación cero. El [registro del recorrido](pruebas_social/social_recorrido.json) confirma la restauración de red, fuente 1.0, orientación vertical y escala de animación 1; se conserva también su log y logcat, sin FATAL EXCEPTION. Las 29 capturas de la organización anterior siguen siendo históricas. Una captura fija acredita aspecto, no demuestra por sí sola rendimiento, vibración ni duración de animaciones.

Antes de publicar el avance 10 se repitieron compilación, pruebas unitarias y lint. El [registro de cierre](pruebas_social/social_cierre_build.log) conserva esa comprobación adicional.
