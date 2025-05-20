package org.example.dominio

/**
 * Representa un usuario en el sistema con un identificador único.
 *
 * Cada instancia de esta clase se le asigna un ID único generado automáticamente.
 *
 * @property nombre Nombre del usuario.
 * @property id Identificador único del usuario.
 */
class Usuario(
    val nombre: String,
    private val id: Int = incrementarId()
) {
    /**
     * Obtiene el identificador único del usuario.
     *
     * @return ID del usuario.
     */
    fun obtenerId(): Int {
        return id
    }

    companion object {
        private var identificador = 0

        /**
         * Incrementa y devuelve el siguiente identificador único.
         *
         * @return Nuevo ID único.
         */
        fun incrementarId(): Int {
            identificador += 1
            return identificador
        }
    }
}

