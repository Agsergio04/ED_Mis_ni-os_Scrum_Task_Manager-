package org.example.dominio

class Usuario (
    val nombre : String,
    private val id : Int = incrementarId()
) {
    fun obtenerId() : Int{
        return id
    }

    companion object{
        private var identificador = 0

        fun incrementarId() : Int{
            return identificador + 1
        }

    }
}