package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.Modelo.ModeloRegistarUsuario

class ControladorLogIn {


    companion object {

        private var instance: ControladorLogIn? = null

        fun getInstance(): ControladorLogIn {

            if(instance == null){
                instance = ControladorLogIn()
            }

            return instance!!
        }

    }

    fun iniciarSesion(email: String, password: String) {
        ModeloLogIn.getInstance().iniciarSesion(email, password)
    }
}