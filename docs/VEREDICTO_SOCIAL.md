# Revisión independiente · MuroCampus social

disposition: **ship**

Alcance: interfaz Android nativa de teléfono, evidencia de emulador Pixel 8 Pro, API 37, recibida el 25 de noviembre de 2026. Revisión de capturas y fuentes; no ejecuté la aplicación, Gradle ni acciones sobre Git.

## persistence

Leí PRODUCT.md, DESIGN.md, el brief de superficie y .impeccable/design.json vigentes. Los cuatro registran la decisión solicitada: organización de conversación inspirada en X/Threads, cabecera breve, avatar lateral, título y cuerpo visibles, acciones por publicación y navegación inferior. Conservan Manrope, blanco fresco, verde suave, rosa ligero y vidrio inspirado. La composición anterior queda identificada como historial, sin autoridad sobre el muro actual.

DESIGN.md conserva tokens nativos dp/sp, parejas claro/oscuro y variantes feed-author, feed-user, feed-title y feed-body. El sidecar incorpora muestras de fila y navegación acordes con Compose. Las fuentes separan Guardadas —snapshots JSON del dispositivo— de la caché Room. La captura 12 permite leer la publicación guardada con modo avión; no atribuyo persistencia remota a esa evidencia.

## fidelity

Abrí las tres imágenes aportadas por la usuaria, las 19 capturas originales 01–19 de capturas_social y los cuatro contactos. Todas muestran el estado que anuncia su nombre; no encontré capturas vacías, negras o a medio cargar. Los originales requeridos son 01_muro, 14_teclado, 16_texto_grande, 17_oscuro y 18_horizontal. No existe una maqueta aprobada para comparación exacta: X y Threads fijan organización, mientras sus colores, medios, cuentas y métricas no son requisitos.

| Criterio | Resultado observado |
| --- | --- |
| Contenido temprano | La primera fila sigue directamente a una cabecera compacta y un renglón de alcance. Desaparecen banner y franja de avatares. Se ven varias publicaciones en el primer viewport. |
| Composición social | Avatar de 36 dp a la izquierda; autor y username compactos; título de 16 sp y cuerpo de 15 sp alineados en el segundo carril. Los separadores establecen ritmo sin tarjetas repetidas. |
| Acciones reales | Comentarios, marcador y compartir aparecen juntos. Las capturas 06, 08 y 11 muestran comentarios recibidos, hoja de Android y selección guardada. El código fija áreas de 48 dp separadas por 12 dp y no inventa reacciones o fechas. |
| Navegación y filtro | Muro, Buscar, Publicar y Guardadas tienen selección distinguible. Las capturas 02, 04, 05 y 11 muestran búsqueda, hoja Personas, filtro y Guardadas. Fuentes y pruebas conservan el regreso y los estados de lista. |
| Variantes | En oscuro, fuente 1.3, horizontal e IME no observé texto de controles cortado ni superposición con barras o teclado. La elipsis del contenido del feed es deliberada; el detalle conserva texto completo. |

La nueva estructura responde materialmente a la corrección solicitada. La paleta y Manrope conservan identidad propia; la semejanza con las referencias está en la jerarquía y el recorrido.

## ceiling

La revisión cubre la matriz suministrada y lectura de MuroCampusApp, PantallaLista, Componentes, NavegacionSocial, PantallaDetalle, GlassControls, Comunidad y Theme. Comprobé pares de contraste de los tokens usados: texto principal claro 11.02:1 y secundario 5.66:1 sobre el fondo; principal oscuro 14.37:1 y secundario 9.97:1. Las selecciones verde y rosa también superan 4.5:1 con sus tintas.

social_build.log y social_android.log terminan en BUILD SUCCESSFUL. Los XML de JVM registran 47 pruebas sin fallos ni errores; el registro conectado completa 30 pruebas, sin fallos informados. Son 77 pruebas en total, además de lint completado. No repetí la ejecución.

La captura 19 acredita una composición completa con animaciones desactivadas; el respeto de ANIMATOR_DURATION_SCALE, los resortes y la llamada háptica se verificaron en código. No se ha comprobado sensación háptica, gestos, fluidez o frecuencia de refresco en un teléfono físico. No se revisó una clase de tableta. No corrió detector web porque este artefacto es Kotlin/Compose nativo.

## material_fixes

Ninguno requerido por esta revisión. No corresponde recapture, fix ni rebuild con la evidencia recibida. El veredicto ship se limita a la superficie y matriz Android descritas.

## keep

- El carril de avatar estable y el título/cuerpo juntos: mejoran la exploración sin inventar medios para llenar la pantalla.
- La cabecera breve, las tres acciones independientes y la selección inferior persistente.
- El rosa reservado al guardado y a iniciales, con tinta legible en ambos temas.
- Alcance local explícito, simulación remota honesta, comentarios independientes y protección del borrador.
