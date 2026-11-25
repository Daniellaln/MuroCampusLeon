---
version: 1
slug: "a-com-leon-murocampus-ui-murocampusapp-kt-bd6709b8"
primary_target: "app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt"
related_targets: ["app/src/main/java/com/leon/murocampus/ui/PantallaLista.kt","app/src/main/java/com/leon/murocampus/ui/PantallaDetalle.kt","app/src/main/java/com/leon/murocampus/ui/Componentes.kt","app/src/main/java/com/leon/murocampus/ui/NavegacionSocial.kt","app/src/main/java/com/leon/murocampus/ui/FormularioPost.kt"]
---

# MuroCampus · extensión social de fase 2

Modo Operate, Android nativo. Leer el muro y abrir una publicación es la tarea principal. Dirección confirmada por Daniella: blanco fresco, verde suave, rosa ligero, Manrope y controles de vidrio inspirado. Construcción directa en Compose.

## Direction contract

THESIS: Un muro universitario organizado como conversación, donde la autoría compacta y el cuerpo original facilitan decidir qué leer, guardar o compartir.

OWN-WORLD: Blanco frío, tinta verde profunda, selección verde agua, rosa en acentos pequeños y Manrope local. Filas abiertas y divisores suaves. Controles de al menos 48 dp y navegación inferior con superficie de vidrio inspirado. Las conversaciones de vidrio ambientan vacíos, errores y editor; el atrio se conserva como recurso de exploración anterior, sin hero en el muro.

STORY: El estudiante ve publicaciones, busca por título, filtra por persona desde la cabecera y abre la lectura completa. Puede entrar directamente a comentarios, guardar en el dispositivo o compartir con Android desde una fila. Publicar abre el editor; editar y eliminar siguen en el menú del detalle. Cada escritura remota explica el carácter simulado de la API y cada copia indica su alcance local.

FIRST VIEWPORT: Cabecera compacta de marca con filtro, actualización e información; renglón de alcance y contador. Feed abierto de una columna con avatar de 36 dp en el carril izquierdo y, a su derecha, nombre de 14 sp, username de 12 sp, título de 16 sp hasta dos líneas y cuerpo de 15 sp hasta cinco. Comentarios, guardar y compartir tienen áreas de 48 dp. No hay hero de campus, franja de cuatro avatares ni portadas por publicación. La navegación inferior contiene Muro, Buscar, Publicar y Guardadas con selección visible; se oculta ante el teclado. Buscar conserva el alcance de descargadas o guardadas. Personas es una hoja con diez autores y Todas las personas. El detalle conserva el texto completo seleccionable; el editor conserva validación, protección de borrador y recibo de simulación.

FORM: Las capturas aportadas por Daniella de X y Threads son autoridad para la organización del feed y la familiaridad de navegación. No cambian la identidad cromática confirmada. Se conserva el mundo Atrio de campus con vidrio y se aplica una composición de conversación a esta superficie. No se importa fotografía por post, métrica social, fecha o perfil que JSONPlaceholder no proporciona.

SIGNATURE: El avatar forma un carril constante mientras el texto y las acciones se alinean en el segundo. La cápsula inferior reúne el recorrido habitual. El vidrio se comprime desde el toque y vuelve con resorte; la fila aplica una presión breve y discreta. Las transiciones y la inserción de páginas respetan Remove animations. No se promete refracción óptica ni API de Apple.

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance

## Historial de composición

La primera composición se rechazó por parecer fase 1 con otra paleta y demasiado texto. La cuadrícula de portadas repetidas también se descartó. Los primeros nueve avances de fase 2 registran el feed abierto con hero, pestañas superiores y títulos de tres líneas. Esta extensión es un décimo avance y conserva esas capturas como historial; su organización vigente procede de las referencias X/Threads.

## Fuentes y auditoría documental

- `MuroCampusApp.kt:31`: estado de vistas; `:82` cabecera, `:102` navegación inferior y `:122` entrada a comentarios desde la fila.
- `PantallaLista.kt:29`: lista; `:73` hoja Personas, `:104` alcance compacto y `:130` búsqueda a demanda.
- `Componentes.kt:60`: fila social; `:76` avatar lateral, `:81` autor, `:91` título, `:95` cuerpo y `:99` acciones. `:135` carga esqueleto de la misma organización.
- `NavegacionSocial.kt:20`: superficie inferior, selección, semántica y presión; `PantallaDetalle.kt:22` conserva lectura y acepta abrir comentarios.
- `Comunidad.kt:35`: iniciales; `:43` marcador con estado Guardada en este dispositivo.
- `FormularioPost.kt:22`, `GlassControls.kt:41` y `theme/Theme.kt:28`: editor, controles y tema conservados.

Comprobación por lectura de fuentes: autoría de la API con fallback identificable; cuerpo y título originales; alcance local explícito; snapshots completos de Guardadas; detalle y comentarios independientes; borrador protegido; escrituras remotas identificadas como simulación. Backend, caché Room, seis mejoras de la guía y funciones propias se conservan.

El cierre conserva 19 capturas propias de la extensión en `docs/GALERIA_SOCIAL.md`, incluidas fuente a 1.3, tema oscuro, horizontal, teclado y escala de animación cero. `docs/VERIFICACION_SOCIAL.md` registra 77 pruebas aprobadas, compilación correcta y lint sin errores con 19 advertencias. Las capturas fijas documentan los estados; la comprobación de acciones se apoya en el recorrido y las pruebas. Las evidencias de los nueve avances previos siguen siendo históricas y no se atribuyen a esta composición.

Los cinco espejos actuales de revisión usan el prefijo `social-` en `.impeccable/review`; su [índice](../review/README.md) los distingue del conjunto `phone*` conservado del avance 9.
