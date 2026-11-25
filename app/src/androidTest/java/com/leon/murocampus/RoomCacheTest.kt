package com.leon.murocampus
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.leon.murocampus.data.*
import com.leon.murocampus.data.local.*
import com.leon.murocampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import org.junit.Assert.*
import java.io.IOException
import java.util.UUID
class RoomCacheTest {
    private class Remoto:PostRepository {
        override val sinConexion=MutableStateFlow(false);var fallo:Exception?=null
        var comentariosVacios=false
        val posts=(1..100).map { Post((it-1)/10+1,it,"Título $it","Texto $it") }
        private fun <T> devolver(valor:T):Result<T> = fallo?.let { Result.failure(it) } ?: Result.success(valor)
        override suspend fun listar()=devolver(posts)
        override suspend fun pagina(inicio:Int,limite:Int,usuario:Int?)=devolver(posts.filter { usuario==null || it.userId==usuario }.drop(inicio).take(limite))
        override suspend fun detalle(id:Int)=devolver(posts.first { it.id==id })
        override suspend fun comentarios(id:Int)=devolver(if(comentariosVacios)emptyList() else listOf(Comentario(id,1,"Respuesta","a@campus.test","Comentario guardado")))
        override suspend fun porUsuario(userId:Int)=devolver(posts.filter { it.userId==userId })
        override suspend fun crear(nuevo:NuevoPost)=devolver(Post(1,101,nuevo.title,nuevo.body))
        override suspend fun editar(id:Int,cambios:EditarPost)=devolver(Post(1,id,cambios.title,cambios.body))
        override suspend fun eliminar(id:Int)=devolver(200)
    }
    private fun prueba(block:suspend (Context,String,Remoto)->Unit)=runBlocking {
        val c=ApplicationProvider.getApplicationContext<Context>();val nombre="prueba_muro_${UUID.randomUUID()}.db"
        try { block(c,nombre,Remoto()) } finally { c.deleteDatabase(nombre) }
    }
    @Test fun roomPersistePaginasDetalleYComentariosAlCerrarYReabrir()=prueba { c,n,remoto ->
        var db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            assertEquals(20,repo.pagina(0,20,null).getOrThrow().size)
            repo.detalle(1).getOrThrow();repo.comentarios(1).getOrThrow()
        } finally { db.close() }
        remoto.fallo=IOException();db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            assertEquals((1..20).toList(),repo.pagina(0,20,null).getOrThrow().map { it.id });assertTrue(repo.sinConexion.value)
            assertEquals("Título 1",repo.detalle(1).getOrThrow().title);assertEquals("Comentario guardado",repo.comentarios(1).getOrThrow().first().body)
        } finally { db.close() }
    }
    @Test fun noInventaPaginaIntermediaAunqueExistanOtrosIdsGuardados()=prueba { c,n,remoto ->
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            repo.pagina(0,20,null).getOrThrow();repo.pagina(0,20,9).getOrThrow()
            remoto.fallo=IOException()
            assertTrue(repo.pagina(20,20,null).isFailure)
            assertEquals((81..90).toList(),repo.pagina(0,20,9).getOrThrow().map { it.id })
        } finally { db.close() }
    }
    @Test fun cacheVaciaNoOcultaFalloNiUnaRespuestaInvalida()=prueba { c,n,remoto ->
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db);remoto.fallo=IOException();assertTrue(repo.pagina(0,20,null).isFailure);assertFalse(repo.sinConexion.value)
            remoto.fallo=null;repo.pagina(0,20,null).getOrThrow()
            remoto.fallo=IllegalArgumentException("Respuesta inválida");assertTrue(repo.pagina(0,20,null).isFailure);assertFalse(repo.sinConexion.value)
        } finally { db.close() }
    }
    @Test fun comentariosVaciosConsultadosSeConservanSinResucitarLosAnteriores()=prueba { c,n,remoto ->
        var db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            assertEquals(1,repo.comentarios(1).getOrThrow().size)
            remoto.comentariosVacios=true
            assertTrue(repo.comentarios(1).getOrThrow().isEmpty())
        } finally { db.close() }
        remoto.fallo=IOException();db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            assertTrue(repo.comentarios(1).getOrThrow().isEmpty())
            assertTrue(repo.origenComentarios.value)
            assertFalse(repo.sinConexion.value)
        } finally { db.close() }
    }
    private class AutoresRemotos:UsuarioRepository {
        var fallo:Exception?=null
        private val usuarios=listOf(Usuario(1,"Leanne Graham","Bret","leanne@example.test"),Usuario(2,"Ervin Howell","Antonette","ervin@example.test"))
        override suspend fun listar():Result<List<Usuario>> = fallo?.let { Result.failure(it) } ?: Result.success(usuarios)
        override suspend fun usuario(id:Int):Result<Usuario> = fallo?.let { Result.failure(it) } ?: Result.success(usuarios.first { it.id==id })
    }
    @Test fun autoresPersistenAlCerrarYReabrirYNoOcultanRespuestasInvalidas()=prueba { c,n,_ ->
        val remoto=AutoresRemotos();var db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try { UsuarioRepositoryConCache(remoto,db).listar().getOrThrow() } finally { db.close() }
        remoto.fallo=IOException();db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=UsuarioRepositoryConCache(remoto,db)
            assertEquals(listOf("Leanne Graham","Ervin Howell"),repo.listar().getOrThrow().map { it.name })
            assertTrue(repo.sinConexion.value)
            assertEquals("Bret",repo.usuario(1).getOrThrow().username)
            remoto.fallo=IllegalArgumentException("JSON inválido")
            assertTrue(repo.listar().isFailure)
            assertFalse(repo.sinConexion.value)
        } finally { db.close() }
    }
    @Test fun usuarioSueltoNoInventaUnListadoCompletoSinRed()=prueba { c,n,_ ->
        val remoto=AutoresRemotos();val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=UsuarioRepositoryConCache(remoto,db)
            repo.usuario(1).getOrThrow();remoto.fallo=IOException()
            assertEquals("Leanne Graham",repo.usuario(1).getOrThrow().name)
            assertTrue(repo.listar().isFailure)
        } finally { db.close() }
    }
    @Test fun migracionUnoADosConservaPostsComentariosYPaginasYPermiteAutores()=prueba { c,n,_ ->
        c.openOrCreateDatabase(n,Context.MODE_PRIVATE,null).use { anterior ->
            anterior.execSQL("CREATE TABLE publicaciones (id INTEGER NOT NULL PRIMARY KEY, userId INTEGER NOT NULL, title TEXT NOT NULL, body TEXT NOT NULL)")
            anterior.execSQL("CREATE TABLE comentarios (id INTEGER NOT NULL PRIMARY KEY, postId INTEGER NOT NULL, name TEXT NOT NULL, email TEXT NOT NULL, body TEXT NOT NULL)")
            anterior.execSQL("CREATE INDEX index_comentarios_postId ON comentarios(postId)")
            anterior.execSQL("CREATE TABLE paginas (clave TEXT NOT NULL PRIMARY KEY, ids TEXT NOT NULL)")
            anterior.execSQL("INSERT INTO publicaciones VALUES (1,1,'Título guardado','Texto guardado')")
            anterior.execSQL("INSERT INTO comentarios VALUES (1,1,'Asunto','a@example.test','Respuesta guardada')")
            anterior.execSQL("INSERT INTO paginas VALUES ('0:0:20','1')")
            anterior.version=1
        }
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).addMigrations(MuroDatabase.MIGRACION_1_2).build()
        try {
            assertEquals("Título guardado",db.posts().detalle(1)?.title)
            assertEquals("Respuesta guardada",db.posts().comentarios(1).single().body)
            assertEquals("1",db.posts().pagina("0:0:20")?.ids)
            val repo=UsuarioRepositoryConCache(AutoresRemotos(),db)
            assertEquals(2,repo.listar().getOrThrow().size)
        } finally { db.close() }
    }
    @Test fun origenDeListaDetalleYComentariosNoSePisanEntreConsultas()=prueba { c,n,remoto ->
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            val repo=PostRepositoryConCache(remoto,db)
            repo.pagina(0,20,null).getOrThrow();repo.detalle(1).getOrThrow();repo.comentarios(1).getOrThrow()
            remoto.fallo=IOException();repo.detalle(1).getOrThrow()
            assertTrue(repo.origenDetalle.value);assertFalse(repo.sinConexion.value)
            remoto.fallo=null;repo.comentarios(1).getOrThrow()
            assertTrue(repo.origenDetalle.value);assertFalse(repo.origenComentarios.value)
            remoto.fallo=IOException();repo.pagina(0,20,null).getOrThrow();repo.comentarios(1).getOrThrow()
            assertTrue(repo.sinConexion.value);assertTrue(repo.origenComentarios.value)
            remoto.fallo=null;repo.detalle(1).getOrThrow();repo.pagina(20,20,null).getOrThrow()
            assertFalse(repo.origenDetalle.value)
            assertTrue(repo.origenComentarios.value);assertTrue(repo.sinConexion.value)
        } finally { db.close() }
    }
    @Test fun falloSqlAlGuardarNoOcultaDatosRemotosYElAvisoSeLimpiaAlRecuperarCache()=prueba { c,n,remoto ->
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            db.openHelper.writableDatabase.execSQL("CREATE TRIGGER fallar_cache_post BEFORE INSERT ON publicaciones BEGIN SELECT RAISE(FAIL, 'cache no disponible'); END")
            val repo=PostRepositoryConCache(remoto,db)
            assertEquals(20,repo.pagina(0,20,null).getOrThrow().size)
            assertNotNull(repo.avisoCache.value);assertFalse(repo.sinConexion.value)
            assertNull(db.posts().detalle(1))
            db.openHelper.writableDatabase.execSQL("DROP TRIGGER fallar_cache_post")
            repo.comentarios(1).getOrThrow()
            assertNotNull(repo.avisoCache.value)
            repo.pagina(0,20,null).getOrThrow()
            assertNull(repo.avisoCache.value);assertNotNull(db.posts().detalle(1))
        } finally { db.close() }
    }
    @Test fun falloSqlAlGuardarAutoresConservaNombresDeRedYAdvierteLaCopiaAusente()=prueba { c,n,_ ->
        val db=Room.databaseBuilder(c,MuroDatabase::class.java,n).build()
        try {
            db.openHelper.writableDatabase.execSQL("CREATE TRIGGER fallar_cache_usuario BEFORE INSERT ON usuarios BEGIN SELECT RAISE(FAIL, 'cache no disponible'); END")
            val repo=UsuarioRepositoryConCache(AutoresRemotos(),db)
            assertEquals("Leanne Graham",repo.listar().getOrThrow().first().name)
            assertNotNull(repo.avisoCache.value);assertFalse(repo.sinConexion.value)
            assertNull(db.usuarios().usuario(1))
            db.openHelper.writableDatabase.execSQL("DROP TRIGGER fallar_cache_usuario")
            repo.listar().getOrThrow()
            assertNull(repo.avisoCache.value);assertNotNull(db.usuarios().usuario(1))
        } finally { db.close() }
    }
}
