---
version: 1
slug: "a-com-leon-murocampus-ui-murocampusapp-kt-bd6709b8"
primary_target: "app/src/main/java/com/leon/murocampus/ui/MuroCampusApp.kt"
related_targets: ["app/src/main/java/com/leon/murocampus/ui/PantallaLista.kt","app/src/main/java/com/leon/murocampus/ui/PantallaDetalle.kt","app/src/main/java/com/leon/murocampus/ui/PantallaCrear.kt"]
---

# MuroCampus · rediseño fase 2

Modo Operate, Android. Leer el muro y abrir una publicación es la tarea principal. Cambios de red y API siguen detrás del Repository. Dirección confirmada por Daniella: blanco fresco, verde suave, rosa ligero, vidrio e imagen universitaria. App nativa: construcción directa en código, sin maqueta pintada.

## Direction contract

THESIS: Un muro universitario que prioriza leer y responder al contenido. Se sustituye el azul de práctica por claridad y una identidad cuidada; sin inventar imágenes o contenido para las publicaciones.

OWN-WORLD: Blanco frío, tinta verde profunda, selección verde agua, rosa en acentos pequeños, Manrope. Foto luminosa de un atrio universitario y dos globos de conversación de vidrio para estados vacíos. Controles Material de 48 dp, bordes brillantes y tacto con resorte.

STORY: El estudiante ve el muro, busca por título, filtra, abre el detalle y comparte. Editar y eliminar aparecen como opciones secundarias. La lectura offline se distingue y cada escritura explica el carácter simulado de la API.

FIRST VIEWPORT: App bar con marca MuroCampus e icono propio. Foto horizontal de campus, encabezado breve del muro y contador real descargado. Campo de búsqueda, filtro de usuario compacto; las publicaciones ocupan la zona de lectura principal con autor, título y dos líneas del cuerpo. FAB extendido Publicar abajo, sin barra de navegación vacía. En ancho expandido el contenido mantiene medida legible. Detalle con título completo, contenido y comentarios; formulario de creación y edición de la misma familia.

FORM: Lista de propuestas: revista universitaria, señalética de biblioteca, feria estudiantil, atrio de campus con vidrio, muestras de taller de diseño, jardín de investigación, sala de lectura de museo. Se desarrolla la cuarta: atrio claro, semilla 3ce3c2a6, dentro de la dirección confirmada por Daniella. Referencias alternativas de panel ferroviario, pase de embarque, mapa de jardín y muro de streaming: descartadas como identidad por contradecir la tarea/paleta; se conserva su disciplina de identidad estable al reordenar y prioridad de datos. La aplicación de consumo es competitiva en familiaridad, pero el fondo cálido contradice el blanco fresco confirmado. Su disciplina conservada es una sola acción principal.

SIGNATURE: El filtro cambia de selección sin perder contexto; el vidrio se comprime desde el toque y vuelve con resorte, con reflejo que responde a la presión. La publicación se abre mediante transición breve compartida en dirección, la respuesta de éxito aparece en su lugar, y Cargar más inserta entradas sin reiniciar lo leído. No se promete refracción óptica ni API nativa de Apple. Se respeta Remove animations.

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance
