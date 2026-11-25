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

