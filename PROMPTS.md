# Prompts

Estos prompts se pueden utilizar uno por uno, siguiendo este orden, para replicar los resultados. Parte del cierre de main. Los recursos necesarios están en esta rama; incorpora primero los recursos del paso 7 antes de ejecutar el prompt visual.

Son instrucciones preparadas para reproducción con Gemini; la implementación y las revisiones de esta entrega fueron realizadas con asistencia de Codex. No se afirma una ejecución de Gemini ni un porcentaje de parecido medido.

## 1. Encabezado json

```text
Compórtate como un experto en Android con Retrofit y OkHttp. Ayúdame a agregar Accept: application/json a todas las solicitudes de MuroCampus mediante AcceptJsonInterceptor en data/remote. Conéctalo al único cliente de RetrofitClient, conserva HTTPS, tiempos de espera y logging solo en debug. No añadas encabezados desde las pantallas. Prueba con MockWebServer que el interceptor se usa y no cambia el cuerpo de las solicitudes. Compila antes de continuar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 1 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Añadí el encabezado Accept JSON a todas las peticiones con un interceptor de OkHttp.

**Reflexión:** La mejora conserva la separación entre vista, ViewModel y Repository.

**Qué comprobar:** ejecuta el criterio del bloque y compila antes del siguiente.

## 2. Busqueda

```text
Compórtate como un experto en MVVM y StateFlow. Ayúdame a buscar por título en las publicaciones ya descargadas de MuroCampus, sin llamar a la API al escribir. Usa búsqueda sin distinguir mayúsculas, conserva el filtro de usuario y expón resultados desde PostsViewModel. Añade un campo etiquetado con limpiar, un vacío con acción para quitar la búsqueda y un indicador del alcance descargado. Conserva lista, detalle, crear y Compartir. Comprueba que escribir no produce nuevas solicitudes.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 2 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Agregué la búsqueda por título sobre las publicaciones descargadas y la combiné con el filtro de usuario.

**Reflexión:** La mejora conserva la separación entre vista, ViewModel y Repository.

**Qué comprobar:** ejecuta el criterio del bloque y compila antes del siguiente.

## 3. Editar

```text
Compórtate como un experto en REST y Jetpack Compose. Necesito editar título y contenido de una publicación existente mediante PATCH posts/{id}. Añade EditarPost serializable, PostApi.editar, PostRepository.editar y el estado de edición en PostsViewModel. La pantalla reutiliza los campos etiquetados, carga el texto actual, deshabilita blancos/doble envío, conserva el borrador ante error y muestra la respuesta real al guardar. No finjas que JSONPlaceholder guarda cambios: informa que es una simulación y al recargar reaparece el original. Retrofit y HttpException solo en data.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 3 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Añadí la edición del título y contenido con PATCH, validación y respuesta visible del servidor.

**Reflexión:** La mejora conserva la separación entre vista, ViewModel y Repository.

**Qué comprobar:** ejecuta el criterio del bloque y compila antes del siguiente.

## 4. Eliminar

```text
Compórtate como un experto en Kotlin, Retrofit y UX de acciones destructivas. Ayúdame a eliminar desde el detalle con confirmación Material 3, Cancelar y Eliminar. PostApi debe usar DELETE posts/{id} y Response<Unit>; el Repository verifica isSuccessful, devuelve Result<Int> con el código real o un error, y relanza CancellationException. PostsViewModel expone carga, éxito y error; la vista nunca recibe Response ni llama al servicio. Tras éxito muestra el código y explica que la API simula la eliminación. No ocultes permanentemente datos que el servidor volverá a entregar.
```

| Prompt | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Bloque 4 | Pendiente de ejecutar por la estudiante | Se implementó y comprobó con Codex; consulta el commit de este avance | Mantener un registro honesto, sin atribuir respuestas inexistentes a Gemini |

**Diferencia frente a la base:** Agregué eliminar una publicación con confirmación y comprobación del código de respuesta DELETE.

**Reflexión:** La mejora conserva la separación entre vista, ViewModel y Repository.

**Qué comprobar:** ejecuta el criterio del bloque y compila antes del siguiente.

