---
name: MuroCampus
description: Muro social universitario claro, verde suave y controles de vidrio inspirado.
colors:
  primary: "#28634F"
  on-primary: "#FFFFFF"
  primary-container: "#D8EBE0"
  on-primary-container: "#173E32"
  secondary-container: "#F5E5ED"
  on-secondary-container: "#713C55"
  background: "#F4F8F6"
  surface: "#FFFFFF"
  on-surface: "#203D34"
  surface-variant: "#E8F0EB"
  on-surface-variant: "#53675D"
  surface-container: "#EDF4EF"
  outline-variant: "#D7E3DA"
  error: "#A63446"
  primary-dark: "#A7D9BF"
  on-primary-dark: "#153C2C"
  primary-container-dark: "#244F3B"
  on-primary-container-dark: "#CDEBD9"
  secondary-container-dark: "#503545"
  on-secondary-container-dark: "#F8DCEA"
  background-dark: "#101D18"
  surface-dark: "#192A22"
  on-surface-dark: "#E0EDE4"
  surface-variant-dark: "#293D31"
  on-surface-variant-dark: "#B6C9BC"
  surface-container-dark: "#1D3126"
  outline-variant-dark: "#3B5342"
  error-dark: "#FFB0BA"
typography:
  headline-medium:
    fontFamily: "Manrope"
    fontSize: "26sp"
    fontWeight: 700
    lineHeight: "33sp"
    letterSpacing: "-0.65sp"
  headline-small:
    fontFamily: "Manrope"
    fontSize: "24sp"
    fontWeight: 700
    lineHeight: "31sp"
    letterSpacing: "-0.65sp"
  title-large:
    fontFamily: "Manrope"
    fontSize: "21sp"
    fontWeight: 700
    lineHeight: "28sp"
    letterSpacing: "0sp"
  title-medium:
    fontFamily: "Manrope"
    fontSize: "17sp"
    fontWeight: 700
    lineHeight: "25sp"
    letterSpacing: "0sp"
  title-small:
    fontFamily: "Manrope"
    fontSize: "15sp"
    fontWeight: 600
    lineHeight: "23sp"
    letterSpacing: "0sp"
  body-large:
    fontFamily: "Manrope"
    fontSize: "16sp"
    fontWeight: 400
    lineHeight: "26sp"
    letterSpacing: "0sp"
  body-medium:
    fontFamily: "Manrope"
    fontSize: "14sp"
    fontWeight: 400
    lineHeight: "23sp"
    letterSpacing: "0sp"
  body-small:
    fontFamily: "Manrope"
    fontSize: "12sp"
    fontWeight: 400
    lineHeight: "19sp"
    letterSpacing: "0sp"
  feed-author:
    fontFamily: "Manrope"
    fontSize: "14sp"
    fontWeight: 700
    lineHeight: "19sp"
    letterSpacing: "0sp"
  feed-user:
    fontFamily: "Manrope"
    fontSize: "12sp"
    fontWeight: 400
    lineHeight: "17sp"
    letterSpacing: "0sp"
  feed-title:
    fontFamily: "Manrope"
    fontSize: "16sp"
    fontWeight: 600
    lineHeight: "21sp"
    letterSpacing: "0sp"
  feed-body:
    fontFamily: "Manrope"
    fontSize: "15sp"
    fontWeight: 400
    lineHeight: "21sp"
    letterSpacing: "0sp"
  label-large:
    fontFamily: "Manrope"
    fontSize: "15sp"
    fontWeight: 600
    lineHeight: "22sp"
    letterSpacing: "0sp"
  label-medium:
    fontFamily: "Manrope"
    fontSize: "13sp"
    fontWeight: 600
    lineHeight: "20sp"
    letterSpacing: "0sp"
  label-small:
    fontFamily: "Manrope"
    fontSize: "11sp"
    fontWeight: 600
    lineHeight: "17sp"
    letterSpacing: "0sp"
rounded:
  notice: "12dp"
  field: "16dp"
  glass: "28dp"
  social-nav: "30dp"
  social-destination: "25dp"
  circle: "50%"
spacing:
  compact: "8dp"
  author: "10dp"
  notice: "12dp"
  reading: "14dp"
  group: "16dp"
  gutter: "16dp"
  glass-inline: "22dp"
  page: "24dp"
components:
  button-glass-primary:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    typography: "{typography.label-large}"
    rounded: "{rounded.glass}"
    padding: "14dp 22dp"
    height: "52dp"
  button-glass-secondary:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    typography: "{typography.label-large}"
    rounded: "{rounded.glass}"
    padding: "14dp 22dp"
    height: "52dp"
  button-glass-icon:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    rounded: "{rounded.circle}"
    size: "48dp"
  input-search:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.field}"
  list-person-selected:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-surface}"
    height: "72dp"
  button-saved-selected:
    backgroundColor: "{colors.secondary-container}"
    textColor: "{colors.on-secondary-container}"
    rounded: "{rounded.circle}"
    size: "48dp"
  nav-social:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.primary}"
    rounded: "{rounded.social-nav}"
    padding: "5dp"
  nav-social-selected:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.primary}"
    typography: "{typography.label-small}"
    rounded: "{rounded.social-destination}"
    height: "56dp"
  feed-row:
    textColor: "{colors.on-surface}"
    typography: "{typography.feed-title}"
  feed-action:
    textColor: "{colors.on-surface-variant}"
    size: "48dp"
  notice:
    backgroundColor: "{colors.surface-container}"
    textColor: "{colors.on-surface-variant}"
    typography: "{typography.body-small}"
    rounded: "{rounded.notice}"
    padding: "12dp"
---

# Design System: MuroCampus

## Overview

**Creative North Star: "Atrio de campus con vidrio"**

Un espacio de lectura universitario luminoso y sereno. El blanco fresco y la tinta verde dejan respirar el contenido; el verde agua y el rosa aparecen en selecciones, iniciales y controles. Manrope mantiene una voz cercana sin perder claridad. La identidad confirmada pide cuidado femenino sutil, sin saturación ni fondo beige.

El contenido remoto conserva sus autores y sus textos originales. Las referencias de X y Threads orientan la organización de conversación: identidad compacta, lectura en una columna y acciones visibles. La identidad propia conserva su paleta y material. Las imágenes generadas aportan ambiente en estados y editor; no son contenido de la API. El atrio se conserva como recurso histórico de la dirección, sin una cabecera fotográfica en el muro actual. La composición específica del muro vive en su brief.

**Key Characteristics:**
- Blanco fresco, tinta verde profunda y rosa ligero en pequeñas superficies de estado.
- Tipografía Manrope, identidad compacta y título y cuerpo originales en el feed.
- Controles de vidrio inspirado con respuesta breve al tacto.
- Autoría real, alcance de búsqueda explícito y guardadas identificadas como copias del dispositivo.
- Tema oscuro y respeto por las preferencias de tamaño de texto y movimiento de Android.

## Colors

La paleta combina blancos fríos con verdes de campus; el rosa distingue estados personales sin teñir la lectura.

### Primary
- **Verde de lectura** (`primary`): acciones, iconos y énfasis funcional; `on-primary` asegura una pareja propia para selecciones sólidas.
- **Verde agua** (`primary-container`): avatar, destino seleccionado, botón principal de vidrio y superficies de éxito; su tinta es `on-primary-container`.

### Secondary
- **Rosa de guardado** (`secondary-container`): marcador seleccionado y parte de los avatares; su tinta es `on-secondary-container`. El rosa del avatar se asigna de forma estable por identificador, no expresa una identidad de género.

### Neutral
- **Blanco fresco** (`background`, `surface`): entorno y campos; el texto principal usa `on-surface`.
- **Verde de apoyo** (`surface-variant`, `surface-container`): esqueletos y avisos; `on-surface-variant` sostiene metadatos y mensajes secundarios.
- **Línea suave** (`outline-variant`): separadores y una parte del borde luminoso de los controles.
- **Rosa de error** (`error`): fallos y acción destructiva; siempre acompañado por texto.
- **Campus nocturno**: las parejas con sufijo `-dark` son valores reales del tema oscuro, seleccionados por Android. No invertir imágenes ni conservar texto oscuro sobre superficies oscuras.

**The Paired Ink Rule.** Usar cada contenedor con su tinta del tema; las transparencias decorativas no se aplican al texto.

## Typography

**Display Font:** Manrope variable local.
**Body Font:** Manrope variable local, con sustitución del sistema solo ante caracteres que la familia no cubra.

**Character:** Una sola familia equilibra identidad compacta y lectura amplia. Los pesos se declaran en el archivo de tema; los tamaños en `sp` respetan el ajuste de fuente de Android. Los roles `feed-*` registran las variantes locales de `FilaPublicacion`; el resto corresponde al tema. El frontmatter conserva unidades nativas: `dp` para geometría y `sp` para texto. Las muestras HTML del sidecar son traducciones visuales a píxeles a escala base, no sustitutos de Compose.

### Hierarchy
- **Headline small:** título completo de publicación y encabezado de Personas; el detalle lo marca como encabezado semántico.
- **Title large / medium / small:** estados vacíos, comentarios, resultados y navegación de pantallas.
- **Feed author / user:** nombre en negrita y username de apoyo; ambos ocupan una línea con elipsis cuando falta ancho.
- **Feed title / body:** título seminegrita de dos líneas como máximo y cuerpo de cinco líneas como máximo; la elipsis conserva la integridad del dato y la fila abre su lectura completa.
- **Body large:** cuerpo completo, seleccionable en detalle.
- **Body medium / small:** comentarios, estados, identificadores y alcance de búsqueda.
- **Label large / medium / small:** acciones, iniciales, contador compacto y navegación; sin transformar el contenido de la API a mayúsculas.

**The Reading First Rule.** Limitar el título del feed a dos líneas y el cuerpo a cinco; al abrirlo mostrar ambos completos, sin exigir una expansión adicional.

## Layout

La aplicación usa una sola región de contenido centrada, con ancho máximo de 760 dp y desplazamiento vertical. El feed tiene un solo carril de filas abiertas, con márgenes laterales `gutter`; detalle y formulario usan `page`. No se ha implementado un cambio a cuadrícula al ampliar el ancho.

La cabecera Material compacta del muro mide 56 dp en su región de contenido e incorpora filtro de persona, marca, actualización e información. Debajo aparece el alcance con un contador breve. Personas se abre como hoja inferior de selección visible; la búsqueda aparece desde Buscar. La composición vigente retira la cabecera fotográfica y los cuatro accesos rápidos de autores. No se inventan fotografías de los usuarios.

Cada fila reserva un carril izquierdo para el avatar de iniciales de 36 dp, separado 10 dp de la columna de identidad y lectura. Esta columna reúne nombre, username, título, cuerpo y tres acciones de 48 dp: comentarios, guardar y compartir. Las divisiones tonales establecen el ritmo. La carga conserva el avatar, los bloques de texto y las acciones mediante tres filas esqueleto.

La navegación inferior ofrece Muro, Buscar, Publicar y Guardadas en una cápsula, con selección visible. Sus destinos tienen altura mínima de 56 dp y la superficie respeta la barra del sistema. Se oculta con el teclado para dejar espacio a la búsqueda; detalle y editor conservan su navegación de regreso. El editor usa los insets de Android.

**Alcance real.** Los rótulos distinguen descargadas, resultados en lo descargado y guardadas en el dispositivo. Una búsqueda local no anuncia un total remoto que no se ha consultado.

## Elevation & Depth

El contenido es plano por defecto. El vidrio de los controles se construye con degradados semitransparentes, borde de luz y sombra teñida de verde. La navegación inferior usa superficie al 94%, borde tonal al 45% y elevación. El texto permanece nítido. No hay muestreo ni refracción óptica del fondo, desenfoque de texto ni dependencia de una API de Apple.

### Shadow Vocabulary
- **Botón de vidrio:** elevación nativa de 7 dp en reposo y 2 dp al presionar; luz ambiente primaria al 9% y sombra focal al 13%.
- **Icono de vidrio:** elevación nativa de 3 dp; luz ambiente primaria al 7% y sombra focal al 10%.
- **Navegación social:** elevación nativa de 7 dp para distinguir el control inferior del contenido.
- **Fila del muro:** sin elevación; separación por línea tonal y espacio.

**The Honest Glass Rule.** Describir el material como vidrio inspirado; reservar la profundidad para los controles y no atribuir refracción real a una capa transparente.

La presión del botón usa un resorte de amortiguación 0.78 y rigidez 680; reduce la escala horizontal un 3.5% y la vertical un 6.5%. Iconos, filas, marcador y destinos inferiores tienen resortes propios más discretos. `MotionSettings` observa `ANIMATOR_DURATION_SCALE`: al llegar a cero, las animaciones usan cambios inmediatos. La vibración del marcador comunica el cambio de guardado y no afirma éxito remoto.

## Shapes

Los controles principales son cápsulas suaves (`glass`); los iconos y avatares usan círculos. La navegación inferior usa `social-nav` y cada destino usa `social-destination`. Campos y resultados usan esquinas de `field`; los avisos usan `notice`. Las filas de publicación no tienen marco ni fondo individual. El borde de vidrio mide 1 dp y pasa por una luz de superficie, una línea tonal parcial y superficie; no convertirlo en un contorno oscuro uniforme.

## Components

### Buttons
- **Vidrio principal:** superficie degradada hacia el contenedor verde y tinta de ese contenedor; altura mínima de 52 dp, espacio interior de 22 dp horizontal y 14 dp vertical. Se usa para el envío del editor y acciones de confirmación cuando corresponde.
- **Vidrio secundario:** tinta primaria sobre vidrio de superficie; se usa en reintento, cargar más y salidas de confirmación.
- **Icono de vidrio:** área circular de 48 dp con icono de 22 dp y descripción accesible; conserva el regreso y las opciones del detalle.
- **Acciones de fila:** comentarios y compartir usan botones de icono de 48 dp; guardar usa un toggle de la misma área. Comentarios abre el detalle con la sección desplegada. Compartir invoca la hoja de Android con título, cuerpo y enlace, y comunica si no hay aplicación compatible.
- **Carga / desactivado:** el progreso reemplaza al icono, el estado se anuncia como Cargando y el botón no admite otro envío.
- **Foco:** los componentes nativos conservan su interacción y accesibilidad Material; no se declara un anillo personalizado inexistente. Las muestras web añaden foco visible para su propio entorno.

### Chips / Selection
- **Personas:** la hoja muestra Todas las personas y los diez autores con nombre, usuario, iniciales y marca de selección. Si el autor aún no está disponible, se muestra Usuario y su identificador. El muro ofrece Reintentar nombres de autores sin sustituir publicaciones ya leídas y avisa si no pudo conservarlos en caché.
- **Filtro activo:** el nombre seleccionado aparece en el alcance compacto y el control Quitar filtro de persona elimina la selección. La hoja conserva semántica de seleccionado.

### Cards / Containers
- **Fila social:** avatar en el carril izquierdo; identidad, título, cuerpo y acciones en el derecho. La región de lectura es un botón que abre la publicación; los tres controles tienen acciones independientes. No añadir un contador de reacciones, fecha o imagen que la API no entrega.
- **Aviso:** tono de contenedor, icono y mensaje; expresa caché, ausencia de conexión y explicación de error.
- **Resultado:** contenedor verde parcial, marca de confirmación y rótulo de simulación. No implica persistencia en el servidor.
- **Estado vacío / error:** ilustración pequeña de conversaciones de vidrio, mensaje concreto y una salida o reintento. La ilustración tiene semántica decorativa y no reemplaza el mensaje.
- **Arte existente:** los recursos 3D de conversaciones se conservan como ambientación; el editor conserva su fondo tenue. El feed y el detalle actuales no usan imágenes como portadas de publicaciones. El atrio queda como recurso de la exploración previa. La procedencia y los prompts se registran en `diseno/IMAGENES.json`.

### Inputs / Fields
- **Búsqueda:** campo delineado que aparece desde Buscar, con etiqueta Buscar por título y limpieza explícita. Busca entre las descargas o las guardadas según la vista; el rótulo conserva ese alcance. El cierre explícito y Atrás cierran la búsqueda y el teclado.
- **Editor:** título y contenido sobre superficie clara u oscura del tema; esquinas `field`, capitalización de frase y envío habilitado solo con ambos valores no vacíos. Durante el envío se desactivan los campos.
- **Salida:** Volver y Atrás del sistema protegen el borrador modificado mediante Descartar o Seguir escribiendo; tras éxito aparece Volver al muro.

### Navigation
- **Inferior:** Muro, Buscar y Guardadas tienen rol de pestaña y estado seleccionado; Publicar tiene rol de botón y abre el editor. Icono y texto pertenecen al mismo destino. Muro y Guardadas mantienen estados de desplazamiento distintos.
- **Guardadas:** el marcador usa rosa al seleccionar, descripción Guardada en este dispositivo y respuesta háptica. Se guardan copias completas de la publicación en JSON local; no se presenta una cuenta ni sincronización remota.
- **Detalle:** autor y marcador preceden el título y cuerpo completos. Los comentarios se expanden con control propio o llegan desplegados desde la acción de la fila; carga, vacío, fallo y reintento de comentarios no sustituyen la lectura ya disponible.
- **Opciones:** editar y eliminar quedan en el menú secundario. Eliminar requiere confirmación que identifica la simulación; el resultado ofrece volver al muro.

## Do's and Don'ts

### Do:
- **Do** conservar Manrope, blanco fresco, verde suave y rosa ligero con sus parejas de tinta de los dos temas.
- **Do** mantener superficies táctiles de al menos 48 dp y comprobar la interfaz con fuente a 1.3, tema oscuro y escala de animación a cero.
- **Do** mostrar autoría de la API, iniciales coherentes, título y cuerpo originales; identificar el fallback Usuario cuando falte el nombre.
- **Do** mantener el cuerpo completo seleccionable y los estados de comentarios independientes de la lectura.
- **Do** describir el alcance local de búsqueda, la persistencia de Guardadas en el dispositivo y el carácter simulado de las escrituras remotas.
- **Do** conservar la procedencia de cada imagen que se distribuya y tratar campus e ilustraciones como ambientación generada.

### Don't:
- **Don't** volver a la sustitución de paleta de fase 1 ni a la cuadrícula repetitiva rechazada para el muro actual.
- **Don't** reintroducir la cabecera fotográfica, las cuatro personas de acceso rápido o portadas decorativas por post en el feed vigente.
- **Don't** fabricar noticias, fotos de autores, fechas, reacciones, instituciones o datos que la API no suministra.
- **Don't** convertir el vidrio inspirado en una promesa de refracción, transparencia óptica real o tecnología de Apple.
- **Don't** ocultar el cuerpo del detalle tras Leer más ni bloquearlo cuando fallen los comentarios.
- **Don't** teñir el fondo de beige, saturar el rosa o aplicar transparencia al texto para simular vidrio.
