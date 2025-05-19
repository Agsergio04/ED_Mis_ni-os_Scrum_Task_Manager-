package org.example.datos

import org.example.dominio.Usuario

class UsuarioRepository : IUsuarioRepository {
    private val usuarios = mutableListOf<Usuario>()

    override fun crearUsuario(nombre: String): Usuario {
        val usuario = Usuario(nombre)
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
