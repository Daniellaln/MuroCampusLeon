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

FIRST VIEWPORT: Cabecera de campus de altura mínima 116 dp, integrada en una superficie blanca fresca con velo horizontal; Comunidad y Explora, guarda y comparte. Pestañas Muro/Guardadas y cuatro personas con iniciales reales; Ver todos abre la hoja de los diez autores. Las herramientas muestran descargadas, resultados dentro de lo descargado o guardadas del dispositivo; la búsqueda aparece al pedirla. Feed abierto de una columna: autor real, usuario, marcador y título original hasta tres líneas; sin extractos ni portadas por publicación. El detalle muestra el título completo y el cuerpo completo seleccionable, con comentarios desplegables de carga/error independientes. Crear/editar abren el editor; Atrás protege el borrador, y el éxito ofrece Volver al muro. Guardadas conserva copias JSON completas y persistentes en el dispositivo, sin atribuirlas a una cuenta remota.

FORM: Lista de propuestas: revista universitaria, señalética de biblioteca, feria estudiantil, atrio de campus con vidrio, muestras de taller de diseño, jardín de investigación, sala de lectura de museo. Se desarrolla la cuarta: atrio claro, semilla 3ce3c2a6, dentro de la dirección confirmada por Daniella. Referencias alternativas de panel ferroviario, pase de embarque, mapa de jardín y muro de streaming: descartadas como identidad por contradecir la tarea/paleta; se conserva su disciplina de identidad estable al reordenar y prioridad de datos. La aplicación de consumo es competitiva en familiaridad, pero el fondo cálido contradice el blanco fresco confirmado. Su disciplina conservada es una sola acción principal.

SIGNATURE: El filtro cambia de selección sin perder contexto; el vidrio se comprime desde el toque y vuelve con resorte, con reflejo que responde a la presión. La publicación se abre mediante una transición breve de navegación en la dirección del recorrido, la respuesta de éxito aparece en su lugar, y Cargar más inserta entradas sin reiniciar lo leído. No se promete refracción óptica ni API nativa de Apple. Se respeta Remove animations.

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance


## Corrección solicitada por Daniella

La primera versión se rechazó por parecer fase 1 con otra paleta y demasiado texto. La posterior cuadrícula de portadas repetidas también fue rechazada. La decisión vigente es un feed abierto de una columna con autores reales, iniciales y títulos de tres líneas; el texto completo queda en el detalle. La imagen del campus aporta contexto y las ilustraciones de conversaciones se reservan como ambientación, vacío o error: no son contenido remoto. Se mantienen la paleta, Manrope y las funciones.


## Decisión y auditoría final

Decisión de composición: la versión vigente sustituye la cuadrícula y la repetición de imágenes por filas abiertas, identidad de autor y marcador. Se documentan las fuentes finales `PantallaLista.kt`, `PantallaDetalle.kt`, `Componentes.kt`, `Comunidad.kt`, `FormularioPost.kt`, `GlassControls.kt` y `Theme.kt`; `DESIGN.md` fija tokens nativos y `.impeccable/design.json` incluye muestras portables. Las muestras HTML son equivalencias visuales a escala base, no una implementación de Android.

Auditoría de verdad visual y funcional por lectura del código:
- Un solo carril de títulos originales de hasta tres líneas, sin portadas ni extractos por fila.
- Autores obtenidos de la API; iniciales y fallback Usuario identificables, sin fotos o perfiles inventados.
- Conteos de descargas, resultados y guardadas con alcance explícito; el filtro no promete buscar datos remotos que no estén cargados.
- Guardadas mantiene snapshots completos en JSON local; el marcador anuncia Guardada en este dispositivo.
- Detalle con cuerpo completo seleccionable; carga, vacío, error y reintento de comentarios independientes.
- Borrador protegido al salir del editor; resultados de escritura identificados como simulación y salidas claras al muro.
- Vidrio inspirado: degradados, borde de luz y resortes; sin promesa de refracción ni API de Apple.
- Campus e ilustraciones generados con procedencia en `diseno/IMAGENES.json`; las propuestas 3D son dirección artística y no datos de la API.

Criterios del cierre visual: áreas táctiles Material de al menos 48 dp, contraste de las parejas de tinta, tema oscuro, fuente a 1.3 y escala Android de animación a cero. Esta auditoría documental acredita correspondencia con las fuentes; no sustituye una comprobación visual ejecutada en emulador o dispositivo ni declara un resultado de pruebas que no haya sido observado.
