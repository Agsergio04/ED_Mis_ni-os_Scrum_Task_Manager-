package org.example.datos

import org.example.dominio.Usuario


class UsuarioRepository : IUsuarioRepository {
    private val usuarios = mutableListOf<Usuario>()
    private var contadorId = 1

    override fun crearUsuario(nombre: String): Usuario {
        val usuario = crearUsuario(nombre)
        usuarios.add(usuario)
        return usuario
    }

    override fun obtenerTodos(): List<Usuario> {
        return usuarios.toList()
    }

    override fun obtenerPorId(id: Int): Usuario? {
        return usuarios.find { it.obtenerId() == id }
    }
}