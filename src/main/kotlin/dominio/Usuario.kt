package org.example.dominio

class Usuario private constructor(
    val nombre : String,
    private val id : Int
) {
    fun obtenerId() : Int{
        return id
    }

    companion object{
        private var identificador = 0

        fun incrementarId() : Int{
            return identificador + 1
        }

        fun crearUsuario(nombre : String) : Usuario{
            return Usuario(nombre,incrementarId())

        }

    }
}