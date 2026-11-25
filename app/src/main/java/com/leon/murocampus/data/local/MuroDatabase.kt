package com.leon.murocampus.data.local

import android.content.Context
import androidx.room.*
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
    @Query("SELECT * FROM comentarios WHERE postId=:postId ORDER BY id") suspend fun comentarios(postId:Int):List<ComentarioEntity>
    @Upsert suspend fun guardarPagina(pagina:PaginaEntity)
    @Query("SELECT * FROM paginas WHERE clave=:clave") suspend fun pagina(clave:String):PaginaEntity?
}

@Database(entities=[PostEntity::class,ComentarioEntity::class,PaginaEntity::class],version=1,exportSchema=true)
abstract class MuroDatabase:RoomDatabase() {
    abstract fun posts():PostDao
    companion object {
        @Volatile private var instancia:MuroDatabase?=null
        fun obtener(contexto:Context):MuroDatabase=instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(contexto.applicationContext,MuroDatabase::class.java,"muro_campus.db")
                .build().also { instancia=it }
        }
    }
}
