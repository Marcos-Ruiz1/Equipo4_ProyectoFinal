package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloRegistarUsuario
import rodriguez.rosa.evangelion30.Modelo.ModeloRestaurarPassword

class ControladorRestaurarPassword {

    companion object {

        private var instance: ControladorRestaurarPassword? = null

        fun getInstance(): ControladorRestaurarPassword {

            if(instance == null){
                instance = ControladorRestaurarPassword()
            }

            return instance!!
        }

    }

    fun restaurarPassword(email: String) {
        ModeloRestaurarPassword.getInstance().resetPassword(email)
    }

}