package rodriguez.rosa.evangelion30.Modelo

import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.red.ProxyTasks
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.ui.home.HomeFragment
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloDashBoard: Subscriptor {
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloDashBoard? = null


        fun getInstance(): ModeloDashBoard {
            if (instance == null) {
                instance = ModeloDashBoard()
            }
            return instance!!
        }

    }

    fun getAllTasks(): ArrayList<Task> {

        ProxyTasks.getInstance().addSubcriber(this, Topics.GET_TASKS)
        return ProxyTasks.getInstance().getAllTasks()

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