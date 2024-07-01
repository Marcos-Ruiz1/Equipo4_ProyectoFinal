package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloUsuarios

class Controlador {

    companion object {

        fun crearUsuario(email: String, password: String) {
            ModeloUsuarios.crearUsuario(email, password)
        }

        fun iniciarSesion(email: String, password: String) {
            ModeloUsuarios.iniciarSesion(email, password)
        }

        fun cerrarSesion() {
            ModeloUsuarios.cerrarSesion()
        }

    }

}