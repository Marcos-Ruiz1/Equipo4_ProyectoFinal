package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class ModeloUsuarios : Subscriptor {

    companion object {

        private var subscriptores = ArrayList<Subscriptor>()

        val instance = ModeloUsuarios()

        fun crearUsuario(email: String, password: String) {
            ProxyUsers.instance.crearUsuario(email, password)
        }

        fun iniciarSesion(email: String, password: String) {
            ProxyUsers.instance.inciarSesion(email, password)
        }

        fun cerrarSesion() {
            ProxyUsers.instance.cerrarSesion()
        }

    }

    override fun notificar(data: NotificacionesUsuario) {
        for (sub in subscriptores) {
            sub.notificar(data)
        }
    }

}