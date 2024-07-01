package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloRegistarUsuario : Subscriptor {

    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloRegistarUsuario? = null


        fun getInstance(): ModeloRegistarUsuario {
            if (instance == null) {
                instance = ModeloRegistarUsuario()
            }
            return instance!!
        }


    }


    fun crearUsuario(email: String, password: String) {
        ProxyUsers.getInstance().addSubcriber(this, Topics.REGISTRO)
        ProxyUsers.getInstance().crearUsuario(email, password)
    }


    fun cerrarSesion() {
        ProxyUsers.getInstance().addSubcriber(this, Topics.LOGOUT)
        ProxyUsers.getInstance().cerrarSesion()
    }

    fun addSubscriber(sub: Subscriptor) {
        if(!subscriptores.contains(sub)) {
            subscriptores.add(sub)
        }
    }

    override fun notificar(data: NotificacionesUsuario) {
        for (sub in subscriptores) {
            sub.notificar(data)
        }
    }

}