package org.example.datos
import org.example.dominio.Usuario

interface IUsuarioRepository {
    fun crearUsuario(nombre: String): Usuario
    fun obtenerTodos(): List<Usuario>
    fun obtenerPorId(id: Int): Usuario?
}
