# Fase 2 — MuroCampus

Alcance confirmado: conservar fase 1 en main y trabajar en mejora-ia. Misma API, arquitectura y función propia Compartir. Sin Firebase ni login.

1. Encabezado Accept: application/json mediante interceptor y prueba de todas las rutas.
2. Búsqueda por título en memoria, combinada con usuario; vacío recuperable.
3. Edición con PATCH, validación, error/reintento y respuesta visible. No fingir persistencia.
4. Eliminar con DELETE Response<Unit>, confirmación y código comprobado.
5. Caché Room: posts y comentarios consultados, repositorio red/local y señal offline. Sin datos de creación ficticiamente guardados en el servidor.
6. Paginación real _start/_limit de 20, reinicio con filtro, duplicados y fin de lista. Búsqueda sobre lo descargado con alcance explícito.
7. Recursos: fotografía universitaria y estado vacío generados; iconos SVG propios con VectorDrawable, fuente local y licencias/procedencia.
8. Rediseño integral: lista, detalle, formulario, editar, confirmación, vacíos, cargas y errores; Material 3, blancos frescos, verde suave, acento rosa ligero. Vidrio con reflejos y respuesta física al toque; sin afirmar refracción nativa de Apple. Animaciones de navegación, selección y éxito, sin partículas permanentes. Tema oscuro, texto grande e insets.
9. Pruebas, capturas reales, README, mapa para exponer y PROMPTS.md secuencial para reproducir con Gemini. Declarar asistencia real de Codex; no inventar conversaciones de Gemini. Publicar y entregar copia completa con historial y carpetas acumulativas por commit.

Cada avance se compila antes de publicar. Verificar API con MockWebServer, Room persistente y offline, ViewModel con cancelación, y recorridos reales en emulador. Una inspección visual agrupada, una corrección y confirmación; revisión final independiente y documentación del diseño. No PDF solicitado por ahora.
