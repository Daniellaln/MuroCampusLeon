package com.leon.murocampus.ui
import android.content.Context
import android.content.Intent
import com.leon.murocampus.model.Post
fun intentCompartir(post:Post):Intent=Intent(Intent.ACTION_SEND).apply {
    type="text/plain"
    putExtra(Intent.EXTRA_SUBJECT,post.title)
    putExtra(Intent.EXTRA_TEXT,"${post.title}\n\n${post.body}\n\nhttps://jsonplaceholder.typicode.com/posts/${post.id}")
}
fun compartirPublicacion(contexto:Context,post:Post) {
    contexto.startActivity(Intent.createChooser(intentCompartir(post),"Compartir publicación"))
}
