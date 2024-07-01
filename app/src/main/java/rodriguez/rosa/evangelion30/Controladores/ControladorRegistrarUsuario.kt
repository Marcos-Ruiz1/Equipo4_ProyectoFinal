package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloRegistarUsuario
import rodriguez.rosa.evangelion30.Modelo.ModeloRegistarUsuario.Companion
import rodriguez.rosa.evangelion30.util.Subscriptor

class ControladorRegistrarUsuario {

    companion object {

        private var instance: ControladorRegistrarUsuario? = null

        fun getInstance(): ControladorRegistrarUsuario {

            if(instance == null){
                instance = ControladorRegistrarUsuario()
            }

            return instance!!
        }

    }

    fun crearUsuario(email: String, password: String) {
        ModeloRegistarUsuario.getInstance().crearUsuario(email, password)
    }

    fun cerrarSesion() {
        ModeloRegistarUsuario.getInstance().cerrarSesion()
    }

}