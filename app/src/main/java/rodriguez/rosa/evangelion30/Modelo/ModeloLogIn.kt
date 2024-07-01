package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloLogIn: Subscriptor {

    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloLogIn? = null


        fun getInstance(): ModeloLogIn {
            if (instance == null) {
                instance = ModeloLogIn()
            }
            return instance!!
        }


    }



    fun iniciarSesion(email: String, password: String) {
        ProxyUsers.getInstance().addSubcriber(this, Topics.LOGIN)
        ProxyUsers.getInstance().inciarSesion(email, password)
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
