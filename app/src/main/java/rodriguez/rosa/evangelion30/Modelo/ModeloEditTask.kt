package rodriguez.rosa.evangelion30.Modelo

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.red.ProxyTasks
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloEditTask: Subscriptor {
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloEditTask? = null


        fun getInstance(): ModeloEditTask {
            if (instance == null) {
                instance = ModeloEditTask()
            }
            return instance!!
        }

    }

    init {
        ProxyTasks.getInstance().addSubcriber(
            this, Topics.EDIT_TASK
        )
    }

    fun editTask(task:Task) {
        ProxyTasks.getInstance().addSubcriber(this, Topics.EDIT_TASK)
        ProxyTasks.getInstance().editTask(task)
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