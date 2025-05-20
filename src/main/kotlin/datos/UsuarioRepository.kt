package org.example.datos

import org.example.dominio.Usuario

/**
 * Implementación en memoria del repositorio de usuarios.
 * Gestiona la creación y consulta de usuarios registrados en el sistema.
 */
class UsuarioRepository : IUsuarioRepository {

    /**
     * Lista mutable interna que almacena los usuarios creados.
     */
    private val usuarios = mutableListOf<Usuario>()

    /**
     * Crea un nuevo usuario con el nombre proporcionado, lo almacena y lo devuelve.
     *
     * @param nombre nombre del usuario a crear
     * @return instancia de Usuario recién creada con su identificador único asignado
     */
    override fun crearUsuario(nombre: String): Usuario {
        val usuario = Usuario(nombre)
        usuarios.add(usuario)
        return usuario
    }

    /**
     * Recupera todos los usuarios actualmente registrados.
     *
     * @return lista inmutable con todos los usuarios
     */
    override fun obtenerTodos(): List<Usuario> {
        return usuarios.toList()
    }

    /**
     * Busca y devuelve un usuario por su identificador.
     *
     * @param id identificador único del usuario
     * @return instancia de Usuario si existe, o null si no se encuentra
     */
    override fun obtenerPorId(id: Int): Usuario? {
        return usuarios.find { it.obtenerId() == id }
    }
}
