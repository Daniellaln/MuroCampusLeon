package com.leon.murocampus.model
import kotlinx.serialization.Serializable
@Serializable data class Usuario(val id:Int,val name:String,val username:String,val email:String)
