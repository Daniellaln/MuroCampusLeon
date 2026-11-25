---
name: MuroCampus
description: Lectura universitaria clara, verde suave y controles de vidrio inspirado.
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
  circle: "50%"
spacing:
  compact: "8dp"
  author: "10dp"
  notice: "12dp"
  reading: "14dp"
  group: "16dp"
  gutter: "20dp"
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
  chip-person-selected:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    rounded: "{rounded.circle}"
    size: "48dp"
  button-saved-selected:
    backgroundColor: "{colors.secondary-container}"
    textColor: "{colors.on-secondary-container}"
    rounded: "{rounded.circle}"
    size: "48dp"
  nav-tabs:
    backgroundColor: "{colors.background}"
    textColor: "{colors.primary}"
  feed-row:
    textColor: "{colors.on-surface}"
    typography: "{typography.title-large}"
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

Un espacio de lectura universitario luminoso y sereno. El blanco fresco y la tinta verde dejan respirar los títulos; el verde agua y el rosa aparecen en selecciones, iniciales y controles. Manrope mantiene una voz cercana sin perder claridad. La identidad confirmada pide cuidado femenino sutil, sin saturación ni fondo beige.

El contenido remoto conserva sus autores y sus textos originales. Las imágenes generadas aportan ambiente: el atrio no representa una ubicación verificada y las conversaciones de vidrio no son contenido de la API. La primera sustitución cromática y la posterior cuadrícula repetitiva fueron rechazadas; el muro vigente se expresa mediante lectura abierta y personas reconocibles. La composición específica del muro vive en su brief, no impone una plantilla a cada pantalla futura.

**Key Characteristics:**
- Blanco fresco, tinta verde profunda y rosa ligero en pequeñas superficies de estado.
- Tipografía Manrope, títulos claros y texto completo en la lectura.
- Controles de vidrio inspirado con respuesta breve al tacto.
- Autoría real, alcance de búsqueda explícito y guardadas identificadas como copias del dispositivo.
- Tema oscuro y respeto por las preferencias de tamaño de texto y movimiento de Android.

## Colors

La paleta combina blancos fríos con verdes de campus; el rosa distingue estados personales sin teñir la lectura.

### Primary
- **Verde de lectura** (`primary`): acciones, flechas y énfasis funcional; `on-primary` asegura una pareja propia para selecciones sólidas.
- **Verde agua** (`primary-container`): avatar, botón principal de vidrio y superficies de éxito; su tinta es `on-primary-container`.

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

**Character:** Una sola familia equilibra títulos compactos y lectura amplia. Los pesos se declaran en el archivo de tema; los tamaños en `sp` respetan el ajuste de fuente de Android. El frontmatter conserva unidades nativas: `dp` para geometría y `sp` para texto. Las muestras HTML del sidecar son traducciones visuales a píxeles a escala base, no sustitutos de Compose.

### Hierarchy
- **Headline medium:** cabecera Comunidad.
- **Headline small:** título completo de publicación y encabezado de Personas; el detalle lo marca como encabezado semántico.
- **Title large:** título del feed; la marca aplica además el peso ExtraBold disponible en la familia.
- **Title medium / small:** comentarios, resultados, cantidades y nombres secundarios.
- **Body large:** cuerpo completo, seleccionable en detalle.
- **Body medium / small:** comentarios, estados, identificadores y alcance de búsqueda.
- **Label large / medium / small:** acciones, iniciales y filtros; sin transformar el contenido de la API a mayúsculas.

**The Reading First Rule.** Limitar el título del feed a tres líneas; al abrirlo mostrar título y cuerpo completos, sin exigir una expansión adicional.

## Layout

La aplicación usa una sola región de contenido centrada, con ancho máximo de 760 dp. La navegación superior es Material y las pantallas se desplazan verticalmente. El feed tiene un solo carril de filas abiertas: no usa tarjetas de portada ni extractos. Sus márgenes laterales son el token `gutter`; detalle y formulario usan `page`. No se ha implementado un cambio a cuadrícula al ampliar el ancho.

En el muro, el atrio se recorta dentro de una cabecera de altura mínima de 116 dp, con velo horizontal del color de superficie. Siguen las pestañas Muro/Guardadas, las cuatro personas de acceso rápido, el acceso Ver todos y las herramientas de búsqueda. La cabecera crece si el texto lo necesita. El selector completo es una hoja inferior con selección visible; no se inventan fotografías de los usuarios.

Las filas separan autor, marcador y título. El título ocupa el ancho restante junto a una flecha discreta; las divisiones, no las sombras, establecen el ritmo. Los estados de carga conservan esta estructura mediante dos filas esqueleto. El detalle separa lectura, compartir, respuesta de eliminación y comentarios. El editor abre sus campos directamente y deja espacio al teclado mediante los insets de Android.

**Alcance real.** Los rótulos distinguen descargadas, resultados en lo descargado y guardadas en el dispositivo. Una búsqueda local no anuncia un total remoto que no se ha consultado.

## Elevation & Depth

El contenido es plano por defecto. El vidrio de los controles se construye con degradados semitransparentes, borde de luz y sombra teñida de verde. El texto permanece nítido. No hay muestreo ni refracción óptica del fondo, desenfoque de texto ni dependencia de una API de Apple.

### Shadow Vocabulary
- **Botón de vidrio:** elevación nativa de 7 dp en reposo y 2 dp al presionar; luz ambiente primaria al 9% y sombra focal al 13%.
- **Icono de vidrio:** elevación nativa de 3 dp; luz ambiente primaria al 7% y sombra focal al 10%.
- **Fila del muro:** sin elevación; separación por línea tonal y espacio.

**The Honest Glass Rule.** Describir el material como vidrio inspirado; reservar la profundidad para los controles y no atribuir refracción real a una capa transparente.

La presión del botón usa un resorte de amortiguación 0.78 y rigidez 680; reduce la escala horizontal un 3.5% y la vertical un 6.5%. Iconos, filas y marcador tienen resortes propios más discretos. `MotionSettings` observa `ANIMATOR_DURATION_SCALE`: al llegar a cero, las animaciones usan cambios inmediatos. La vibración del marcador comunica el cambio de guardado y no afirma éxito remoto.

## Shapes

Los controles principales son cápsulas suaves (`glass`); los iconos, filtros y avatares usan círculos. Campos, cabecera y resultados usan esquinas de `field`; los avisos usan `notice`. Las filas de publicación no tienen marco ni fondo individual. El borde de vidrio mide 1 dp y pasa por una luz de superficie, una línea tonal parcial y superficie; no convertirlo en un contorno oscuro uniforme.

## Components

### Buttons
- **Vidrio principal:** superficie degradada hacia el contenedor verde y tinta de ese contenedor; altura mínima de 52 dp, espacio interior de 22 dp horizontal y 14 dp vertical. Publicar es la acción principal del muro y el envío es la del editor.
- **Vidrio secundario:** tinta primaria sobre vidrio de superficie; se usa en reintento, cargar más y salidas de confirmación.
- **Icono de vidrio:** área circular de 48 dp con icono de 22 dp y descripción accesible. La búsqueda cambia de lupa a cierre y ofrece limpiar el texto.
- **Carga / desactivado:** el progreso reemplaza al icono, el estado se anuncia como Cargando y el botón no admite otro envío.
- **Compartir:** acción de texto en detalle; invoca la hoja de Android con título, cuerpo y enlace.
- **Foco:** los componentes nativos conservan su interacción y accesibilidad Material; no se declara un anillo personalizado inexistente. Las muestras web añaden foco visible para su propio entorno.

### Chips
- **Personas:** cuatro círculos con iniciales reales; seleccionar aplica el filtro y repetir la selección lo quita. Ver todos abre los diez autores con nombre, usuario y marca de selección. Si el autor aún no está disponible, se muestra Usuario y su identificador. El muro ofrece Reintentar nombres de autores sin sustituir publicaciones ya leídas y avisa si no pudo conservarlos en caché.
- **Filtro activo:** InputChip con texto Quitar filtro de persona y cierre explícito; altura mínima de 48 dp.

### Cards / Containers
- **Aviso:** tono de contenedor, icono y mensaje; expresa caché, ausencia de conexión y explicación de error.
- **Resultado:** contenedor verde parcial, marca de confirmación y rótulo de simulación. No implica persistencia en el servidor.
- **Estado vacío / error:** ilustración pequeña de conversaciones de vidrio, mensaje concreto y una salida o reintento. La ilustración tiene semántica decorativa y no reemplaza el mensaje.
- **Arte existente:** los recursos 3D de conversaciones se conservan como ambientación. El feed y el detalle actuales no los usan como portadas de publicaciones. La procedencia y los prompts se registran en `diseno/IMAGENES.json`.

### Inputs / Fields
- **Búsqueda:** campo delineado que aparece al pedirlo, con etiqueta Buscar por título y limpieza explícita. Busca entre las descargas o las guardadas según la pestaña; el rótulo conserva ese alcance.
- **Editor:** título y contenido sobre superficie clara u oscura del tema; esquinas `field`, capitalización de frase y envío habilitado solo con ambos valores no vacíos. Durante el envío se desactivan los campos.
- **Salida:** Volver y Atrás del sistema protegen el borrador modificado mediante Descartar o Seguir escribiendo; tras éxito aparece Volver al muro.

### Navigation
- **Pestañas:** Muro y Guardadas, con indicador Material y estado seleccionado. Mantienen estados de desplazamiento distintos.
- **Guardadas:** el marcador usa rosa al seleccionar, descripción Guardada en este dispositivo y respuesta háptica. Se guardan copias completas de la publicación en JSON local; no se presenta una cuenta ni sincronización remota.
- **Detalle:** autor y marcador preceden el título y cuerpo completos. Los comentarios se expanden con control propio; carga, vacío, fallo y reintento de comentarios no sustituyen la lectura ya disponible.
- **Opciones:** editar y eliminar quedan en el menú secundario. Eliminar requiere confirmación que identifica la simulación; el resultado ofrece volver al muro.

## Do's and Don'ts

### Do:
- **Do** conservar Manrope, blanco fresco, verde suave y rosa ligero con sus parejas de tinta de los dos temas.
- **Do** mantener superficies táctiles de al menos 48 dp y comprobar la interfaz con fuente a 1.3, tema oscuro y escala de animación a cero.
- **Do** mostrar autoría de la API, iniciales coherentes y los títulos originales; identificar el fallback Usuario cuando falte el nombre.
- **Do** mantener el cuerpo completo seleccionable y los estados de comentarios independientes de la lectura.
- **Do** describir el alcance local de búsqueda, la persistencia de Guardadas en el dispositivo y el carácter simulado de las escrituras remotas.
- **Do** conservar la procedencia de cada imagen que se distribuya y tratar campus e ilustraciones como ambientación generada.

### Don't:
- **Don't** volver a la sustitución de paleta de fase 1 ni a la cuadrícula repetitiva rechazada para el muro actual.
- **Don't** añadir extractos, portadas decorativas por post o un CTA repetido a cada fila del feed vigente.
- **Don't** fabricar noticias, fotos de autores, instituciones o datos que la API no suministra.
- **Don't** convertir el vidrio inspirado en una promesa de refracción, transparencia óptica real o tecnología de Apple.
- **Don't** ocultar el cuerpo del detalle tras Leer más ni bloquearlo cuando fallen los comentarios.
- **Don't** teñir el fondo de beige, saturar el rosa o aplicar transparencia al texto para simular vidrio.
