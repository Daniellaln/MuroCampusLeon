# Verificación de fase1

Windows, JDK21, Pixel_8_Pro_2 API37. Cada uno de los12avances se compila antes de publicarse. Pruebas JUnit y registros en pruebas/.

## Comprobaciones

18pruebas unitarias: modelos y camposJSON, tolerancia a campos extras y rechazo de campos faltantes; GET, Path, Query, POST y Body mediante servidor controlado;404/JSONinválido/IO/cancelación; estados del ViewModel, filtro, detalle, regreso y creación. Las pruebas de red controlada usan MockWebServer; no se confunden con el servicio externo.

8pruebas instrumentadas: lista, navegación de detalle sin recarga, filtro, formulario vacío y limpieza tras éxito, texto conservado ante error y reintento, recuperación de lista y contenido del intent de compartir. La octava consulta el servicio real:100posts, diez publicaciones para cada usuario1–10, cinco comentarios en varias publicaciones,404 en posts/9999 y creación con id101. Solo se enviaron textos ficticios.

La comprobación temporal del cliente en MainActivity registró Llegaron100publicaciones en Logcat y el Usuario1 de ejemplo. Ese código se retiró antes del commit5; solo se conserva el registro como evidencia. El interceptor BODY funciona únicamente en debug.

El recorrido real de la app cubre lista, detalle, filtro, compartir sin enviar, creación101, red apagada y recuperación en lista/detalle/formulario. El formulario conserva lo escrito cuando falla. Se comprueban también horizontal y texto1.3. Las imágenes y el JSON del recorrido están en capturas/ y pruebas/.

El error de lint inicial fue una ruta local de SDK sin escapar; se corrigió y se volvió a ejecutar lint. No se incluyó local.properties en ningún commit. El informe final de lint no contiene errores; conserva advertencias de versiones/target y de librerías, sin ocultarlas con un baseline.

## Límites

No se probó en Mac, teléfono físico ni API24. Las llamadas dependen de la disponibilidad del servicio y la red. JSONPlaceholder simula la creación: POST201 no prueba que un post quede guardado. El mapa de arquitectura y las carpetas se revisaron: no hay llamadas a la API desde un Composable ni dependencia de Retrofit/PostApi/HttpException/Context en los ViewModels.

El cambio de paquetes a com.leon se documenta; la reconstrucción no se presenta como un proyecto base ejecutable entregado por el docente. La ayuda real fue Codex. La fase2 no está implementada todavía.
