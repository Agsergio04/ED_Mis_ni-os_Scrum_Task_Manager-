package org.example.dominio

data class FiltroActividadDTO(
    val tipo: String? = null,
    val estado: EstadoTarea? = null,
    val etiquetas: List<String>? = null,
    val nombreUsuario: String? = null,
    val fechaFiltro: String? = null
)
