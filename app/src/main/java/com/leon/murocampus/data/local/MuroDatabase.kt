package com.leon.murocampus.data.local

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.leon.murocampus.model.*

@Entity(tableName="publicaciones")
data class PostEntity(@PrimaryKey val id:Int,val userId:Int,val title:String,val body:String) {
    fun modelo()=Post(userId,id,title,body)
}
fun Post.entidad()=PostEntity(id,userId,title,body)

@Entity(tableName="comentarios",indices=[Index("postId")])
data class ComentarioEntity(@PrimaryKey val id:Int,val postId:Int,val name:String,val email:String,val body:String) {
    fun modelo()=Comentario(postId,id,name,email,body)
}
fun Comentario.entidad()=ComentarioEntity(id,postId,name,email,body)

@Entity(tableName="usuarios")
data class UsuarioEntity(@PrimaryKey val id:Int,val name:String,val username:String,val email:String) {
    fun modelo()=Usuario(id,name,username,email)
}
fun Usuario.entidad()=UsuarioEntity(id,name,username,email)

/** Conserva la pertenencia exacta de cada página, no deduce continuidad de IDs sueltos. */
@Entity(tableName="paginas")
data class PaginaEntity(@PrimaryKey val clave:String,val ids:String)

@Dao interface PostDao {
    @Upsert suspend fun guardar(posts:List<PostEntity>)
    @Query("SELECT * FROM publicaciones ORDER BY id") suspend fun todas():List<PostEntity>
    @Query("SELECT * FROM publicaciones WHERE userId=:usuario ORDER BY id") suspend fun porUsuario(usuario:Int):List<PostEntity>
    @Query("SELECT * FROM publicaciones WHERE id=:id") suspend fun detalle(id:Int):PostEntity?
    @Query("SELECT * FROM publicaciones WHERE id IN (:ids)") suspend fun porIds(ids:List<Int>):List<PostEntity>
    @Upsert suspend fun guardarComentarios(comentarios:List<ComentarioEntity>)
    @Query("DELETE FROM comentarios WHERE postId=:postId") suspend fun borrarComentarios(postId:Int)
    @Query("SELECT * FROM comentarios WHERE postId=:postId ORDER BY id") suspend fun comentarios(postId:Int):List<ComentarioEntity>
    @Upsert suspend fun guardarPagina(pagina:PaginaEntity)
    @Query("SELECT * FROM paginas WHERE clave=:clave") suspend fun pagina(clave:String):PaginaEntity?
}

@Dao interface UsuarioDao {
    @Upsert suspend fun guardar(usuarios:List<UsuarioEntity>)
    @Query("SELECT * FROM usuarios WHERE id=:id") suspend fun usuario(id:Int):UsuarioEntity?
    @Query("SELECT * FROM usuarios WHERE id IN (:ids)") suspend fun porIds(ids:List<Int>):List<UsuarioEntity>
}

@Database(entities=[PostEntity::class,ComentarioEntity::class,PaginaEntity::class,UsuarioEntity::class],version=2,exportSchema=true)
abstract class MuroDatabase:RoomDatabase() {
    abstract fun posts():PostDao
    abstract fun usuarios():UsuarioDao
    companion object {
        val MIGRACION_1_2=object:Migration(1,2) {
            override fun migrate(db:SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `username` TEXT NOT NULL, `email` TEXT NOT NULL, PRIMARY KEY(`id`))")
            }
        }
        @Volatile private var instancia:MuroDatabase?=null
        fun obtener(contexto:Context):MuroDatabase=instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(contexto.applicationContext,MuroDatabase::class.java,"muro_campus.db")
                .addMigrations(MIGRACION_1_2)
                .build().also { instancia=it }
        }
    }
}
