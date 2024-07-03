package rodriguez.rosa.evangelion30.red

import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.DAO.UserDAO.Companion
import rodriguez.rosa.evangelion30.dominio.Tasks
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.SubscriptorProxy
import rodriguez.rosa.evangelion30.util.Topics
import java.lang.reflect.Proxy

class ProxyTasks : SubscriptorProxy {

    private var subscriptores: HashMap<String, ArrayList<Subscriptor>> = hashMapOf(

        Topics.FILTER_TASKS.toString() to ArrayList(),
        Topics.REFRESH_TASKS.toString() to ArrayList(),
        Topics.ORDER_LISTS.toString() to ArrayList(),

    )

    companion object {
        private var instance: ProxyTasks? = null

        fun getInstance(): ProxyTasks {
            if (instance == null) {
                instance = ProxyTasks()
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

    fun addTask(title: String, description: String, category: String, priority:Int){
        UserDAO.getInstances().addSubcriber(this, Topics.ADD_TASK)
        UserDAO.getInstances().addTask(title, description, category, priority)
    }


    fun filteredFetch(){
        TasksDAO.getInstances().addSubcriber(this, Topics.FILTER_TASKS)
        TasksDAO.getInstances().fetchTasks()
    }


    fun refreshTasks(){
        TasksDAO.getInstances().addSubcriber(this, Topics.REFRESH_TASKS)
        TasksDAO.getInstances().fetchTasks()
    }


    override fun notificar(data: NotificacionesUsuario, topic: Topics) {
        for (sub in this.subscriptores.get(topic.toString())!!) {
            sub.notificar(data)        }
    }
}