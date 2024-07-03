package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.red.ProxyTasks
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.ui.home.HomeFragment
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloHome: Subscriptor {
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloHome? = null


        fun getInstance(): ModeloHome {
            if (instance == null) {
                instance = ModeloHome()
            }
            return instance!!
        }

    }

    fun refreshTasks(): ArrayList<Task> {

        ProxyTasks.getInstance().addSubcriber(this, Topics.REFRESH_TASKS)
        return ProxyTasks.getInstance().filteredFetch()

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