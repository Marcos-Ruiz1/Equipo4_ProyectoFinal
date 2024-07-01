package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloRestaurarPassword: Subscriptor{
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloRestaurarPassword? = null


        fun getInstance(): ModeloRestaurarPassword {
            if (instance == null) {
                instance = ModeloRestaurarPassword()
            }
            return instance!!
        }


    }


    fun resetPassword(email: String) {
        ProxyUsers.getInstance().addSubcriber(this, Topics.RESET_PASSWORD)
        ProxyUsers.getInstance().restaurarPassword(email)
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