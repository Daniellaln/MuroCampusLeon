# Código para exponer

En Android Studio: app → kotlin+java → com.leon.murocampus. En Project la carpeta es app/src/main/java/com/leon/murocampus/. Las rutas siguientes parten de la raíz del proyecto.

| Qué puede preguntar | Archivo | Línea |
|---|---|---|
| Dependencias / librerías | app/build.gradle.kts | 34 |
| Internet / permiso | app/src/main/AndroidManifest.xml | 2 |
| Entidad JSON / modelo | app/src/main/java/com/leon/murocampus/model/Modelos.kt | 5 |
| API / endpoints | app/src/main/java/com/leon/murocampus/data/remote/PostApi.kt | 4 |
| Singleton / cliente de red | app/src/main/java/com/leon/murocampus/data/remote/RetrofitClient.kt | 10 |
| Conversor / serialización | app/src/main/java/com/leon/murocampus/data/remote/RetrofitClient.kt | 18 |
| Repository / manejo de fallo | app/src/main/java/com/leon/murocampus/data/PostRepository.kt | 18 |
| Lista / carga de publicaciones | app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt | 21 |
| Detalle / comentarios | app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt | 35 |
| Filtro / Query | app/src/main/java/com/leon/murocampus/data/remote/PostApi.kt | 8 |
| Creación / envío | app/src/main/java/com/leon/murocampus/viewmodel/PostsViewModel.kt | 58 |
| Función propia / compartir | app/src/main/java/com/leon/murocampus/ui/CompartirPublicacion.kt | 5 |
| Error / mensaje legible | app/src/main/java/com/leon/murocampus/data/Errores.kt | 5 |

Para mostrar un avance: Git → Log → main → selecciona el commit → Show Diff. Las líneas anteriores son del cierre de main, no las de cada archivo histórico.

“Retrofit describe las peticiones. OkHttp hace la conexión. Serialization convierte elJSON. El Repository devuelve Result, el ViewModel lo transforma en UiState y Compose dibuja ese estado.”

La función propia usa el menú nativo Android. Abrirlo no significa que la app haya enviado un mensaje. Los POST del laboratorio tampoco se guardan realmente en JSONPlaceholder.
