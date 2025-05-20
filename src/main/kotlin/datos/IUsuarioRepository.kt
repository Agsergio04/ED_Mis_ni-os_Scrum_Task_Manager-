package org.example.datos

import org.example.dominio.Usuario

/**
 * Interfaz para el repositorio de usuarios.
 * Define operaciones de creación y consultas de usuarios.
 */
interface IUsuarioRepository {

    /**
     * Crea un nuevo usuario con el nombre proporcionado.
     *
     * @param nombre nombre del usuario a crear
     * @return instancia de Usuario recién creada con identificador único
     */
    fun crearUsuario(nombre: String): Usuario

    /**
     * Recupera todos los usuarios registrados en el sistema.
     *
     * @return lista de instancias de Usuario
     */
    fun obtenerTodos(): List<Usuario>

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return instancia de Usuario si existe, o null si no se encuentra
     */
    fun obtenerPorId(id: Int): Usuario?
}
