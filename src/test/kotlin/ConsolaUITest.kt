import io.mockk.*
import org.junit.jupiter.api.*
import java.io.*
import org.example.datos.*
import org.example.presentacion.*
import org.example.dominio.*
import org.example.aplicacion.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class ConsolaUITest {

    // Clases fake sin interfaces
    class FakeActividadService {
        var ultimoNombreCreado: String? = null
        fun crearUsuario(nombre: String): Usuario {
            ultimoNombreCreado = nombre
            return Usuario(nombre, 1)
        }
        // Implementa sólo los métodos necesarios para la prueba
    }

    class FakeDashboardService {
        // Dummy, no hace nada
    }

    @Test
    fun testCrearUsuario() {
        val fakeService = FakeActividadService().crearUsuario("luque")
        val fakeDashboard = FakeDashboardService()
        val consola = ConsolaUI(fakeService, fakeDashboard)

        // Simula la entrada del usuario
        val input = "Juan Perez\n"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // Captura la salida
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        consola.crearUsuario()

        val output = outputStream.toString()

        assertTrue(output.contains("Usuario creado con ID: 1"), "Salida esperada no encontrada")
        assertEquals("Juan Perez", fakeService.ultimoNombreCreado)

        // Restaurar System.in y System.out (muy recomendable para evitar side effects)
        System.setIn(System.`in`)
        System.setOut(System.out)
    }
}
