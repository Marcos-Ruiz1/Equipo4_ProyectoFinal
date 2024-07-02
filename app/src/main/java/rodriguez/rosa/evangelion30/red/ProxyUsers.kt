package rodriguez.rosa.evangelion30.red

import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.DAO.UserDAO.Companion
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.SubscriptorProxy
import rodriguez.rosa.evangelion30.util.Topics
import java.lang.reflect.Proxy

class ProxyUsers : SubscriptorProxy {

    private var subscriptores: HashMap<String, ArrayList<Subscriptor>> = hashMapOf(
        Topics.REGISTRO.toString() to ArrayList(),
        Topics.LOGIN.toString() to ArrayList(),
        Topics.LOGOUT.toString() to ArrayList(),
        Topics.RESET_PASSWORD.toString() to ArrayList(),
        Topics.ADD_TASK.toString() to ArrayList()
    )

    companion object {
        private var instance: ProxyUsers? = null

        fun getInstance(): ProxyUsers {
            if (instance == null) {
                instance = ProxyUsers()
            }
            return instance!!
        }
    }

    fun addSubcriber(sub: Subscriptor, topic: Topics) {

        if (!instance!!.subscriptores.containsKey(topic.toString())) {
            throw RuntimeException("EL TOPIC NO EXISTE")
        }

        if(!instance!!.subscriptores.get(topic.toString())!!.contains(sub)){
            instance!!.subscriptores.get(topic.toString())!!.add(sub)
        }

    }

    fun crearUsuario(email: String, password: String): Unit {
        UserDAO.getInstances().addSubcriber(this, Topics.REGISTRO)
        UserDAO.getInstances().createUser(email, password)
    }

    fun inciarSesion(email: String, password: String) {
        UserDAO.getInstances().addSubcriber(this, Topics.LOGIN)
        UserDAO.getInstances().logInUser(email, password)
    }

    fun restaurarPassword(email: String) {
        UserDAO.getInstances().addSubcriber(this, Topics.RESET_PASSWORD)
        UserDAO.getInstances().resetPassword(email)
    }

    fun cerrarSesion(): Unit {
        UserDAO.getInstances().addSubcriber(this, Topics.LOGOUT)
        UserDAO.getInstances().logOutUser()
    }

    fun addTask(title: String, description: String, category: String, priority:Int){
        UserDAO.getInstances().addSubcriber(this, Topics.ADD_TASK)
        UserDAO.getInstances().addTask(title, description, category, priority)
    }


    override fun notificar(data: NotificacionesUsuario, topic: Topics) {
        for (sub in this.subscriptores.get(topic.toString())!!) {
            sub.notificar(data)        }
    }
}