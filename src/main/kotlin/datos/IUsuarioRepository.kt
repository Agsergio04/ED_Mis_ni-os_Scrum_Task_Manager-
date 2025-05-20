package org.example.datos
@file:Suppress("ktlint:standard:no-trailing-spaces")

import org.example.dominio.Usuario

interface IUsuarioRepository {
    fun crearUsuario(nombre: String): Usuario
    fun obtenerTodos(): List<Usuario>
    fun obtenerPorId(id: Int): Usuario?
}
