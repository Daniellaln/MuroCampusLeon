# Recursos visuales

Cuatro PNG generados con la herramienta integrada image_gen. Los prompts originales completos se conservan en [IMAGENES.json](IMAGENES.json) y en los metadatos PNG. Son ambientación de la app: no documentan una ubicación verificada ni forman parte de la respuesta de JSONPlaceholder.

| Recurso en `app/src/main/res/drawable-nodpi` | Intención y uso actual |
| --- | --- |
| `campus_atrio.png` · 1859 × 846 | Activo: contexto universitario en la cabecera del muro, recorte Crop con velo horizontal. No es fotografía verificada de una universidad concreta. |
| `conversaciones_vacio.png` · 1536 × 1024, RGBA | Activo: ilustración de conversaciones de vidrio para vacíos y errores. Su fondo es transparente y el texto del estado lleva la información. |
| `conversacion_rosa.png` | Generada originalmente como portada simbólica; uso actual: fondo tenue del editor a opacidad 25%, con un velo que preserva la lectura de los campos. No se atribuye al autor o al post. |
| `conversacion_verde.png` | Generada para la propuesta de portadas después descartada. Se conserva como recurso de esa exploración; no tiene una llamada activa en el feed o el detalle final. |

`PortadaPublicacion.kt` permanece como componente de la propuesta anterior, sin llamada desde las pantallas finales. El muro vigente usa `FilaPublicacion`: autor, marcador y título original. El detalle muestra lectura completa y comentarios sin portada decorativa.

Las imágenes están en nodpi para evitar reescalado automático por densidad; Compose decide su tamaño visible en dp. Son archivos locales del paquete: no se descargan al abrir una publicación ni usan rutas del Escritorio.

Manrope es una familia variable local en `app/src/main/res/font/manrope.ttf`, con pesos declarados en el tema. Procedencia: [Google Fonts / Manrope](https://github.com/google/fonts/tree/main/ofl/manrope). Licencia SIL OFL conservada en `licencias/OFL-Manrope.txt`.

Los SVG propios en `diseno/iconos` mantienen trazo redondo de 1.7 y vista 24 × 24. Sus equivalentes VectorDrawable en `res/drawable` son los que carga `painterResource`; Compose no intenta abrir los SVG. El símbolo de MuroCampus es propio y no se presenta como un logotipo institucional autorizado.

Los controles de vidrio son capas y respuesta de Compose, independientes de los PNG. No hay refracción real del fondo ni API de Apple. Las esculturas 3D son una dirección artística generada, no evidencia del servicio.
