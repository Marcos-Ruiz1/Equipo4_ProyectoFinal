package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.dominio.Tasks
import rodriguez.rosa.evangelion30.red.ProxyTasks
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloConfiguracion: Subscriptor {
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloConfiguracion? = null


        fun getInstance(): ModeloConfiguracion {
            if (instance == null) {
                instance = ModeloConfiguracion()
            }
            return instance!!
        }

    }

    fun filteredFetch() {

        ProxyTasks.getInstance().addSubcriber(this, Topics.FILTER_TASKS)
        ProxyTasks.getInstance().refreshTasks()

    }

    fun orderPriority() {

        ProxyTasks.getInstance().addSubcriber(this, Topics.ORDER_LISTS)
        ProxyTasks.getInstance().orderPriority()

    }

    fun orderAscendentDates() {

        ProxyTasks.getInstance().addSubcriber(this, Topics.ORDER_LISTS)
        ProxyTasks.getInstance().orderAscendent()

    }

    fun orderDescententDates() {

        ProxyTasks.getInstance().addSubcriber(this, Topics.ORDER_LISTS)
        ProxyTasks.getInstance().orderDescendent()
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