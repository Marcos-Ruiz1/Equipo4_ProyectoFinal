package rodriguez.rosa.evangelion30.red

import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import java.lang.reflect.Proxy

class ProxyUsers : Subscriptor {

    private val subscriptores: ArrayList<Subscriptor> = ArrayList()

    companion object {
        val instance = ProxyUsers()
    }

    fun crearUsuario(email: String, password: String): Unit {
        val dao = UserDAO.instance
        dao.createUser(email, password)
    }

    fun inciarSesion(email: String, password: String) {
        val dao = UserDAO.instance
        dao.logInUser(email, password)
    }

    fun cerrarSesion(): Unit {
        val dao = UserDAO.instance
        dao.logOutUser()
    }

    override fun notificar(data: NotificacionesUsuario) {
        for (sub in subscriptores) {
            sub.notificar(data)
        }
    }

}