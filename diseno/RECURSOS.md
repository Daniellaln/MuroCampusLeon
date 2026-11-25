# Recursos visuales

Dos imágenes generadas con la herramienta integrada image_gen. Los prompts exactos están en IMAGENES.json y en los metadatos PNG. Son recursos de ambientación: no muestran una ubicación real verificada ni contenido asociado a los posts de la API.

- `app/src/main/res/drawable-nodpi/campus_atrio.png`: foto luminosa de atrio, cabecera de muro y franja de formulario. 1859 × 846; recorte Crop controlado desde Compose.
- `app/src/main/res/drawable-nodpi/conversaciones_vacio.png`: ilustración de dos conversaciones de vidrio, fondo transparente RGBA; vacíos y errores. 1536 × 1024.
- `app/src/main/res/font/manrope.ttf`: familia variable Manrope, ejes de peso definidos en el tema. Fuente de https://github.com/google/fonts/tree/main/ofl/manrope; licencia SIL OFL en licencias/OFL-Manrope.txt.
- `diseno/iconos/*.svg`: 19 iconos propios, editables. Trazo 1.7, extremos redondos, vista 24 × 24. Los XML equivalentes en `res/drawable` son los que usa `painterResource`; no se intenta cargar SVG con painterResource.

Las imágenes viven en nodpi para evitar reescalado automático de densidad; el tamaño visible se decide en dp en Compose. No se usan rutas del Escritorio ni descargas en tiempo de ejecución. Iconos y logo fueron dibujados en código para mantener geometría y peso coherentes.
