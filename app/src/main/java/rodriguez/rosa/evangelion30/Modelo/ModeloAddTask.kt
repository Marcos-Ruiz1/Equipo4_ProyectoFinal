package rodriguez.rosa.evangelion30.Modelo

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class ModeloAddTask: Subscriptor {
    private var subscriptores = ArrayList<Subscriptor>()


    companion object {



        private var instance: ModeloAddTask? = null


        fun getInstance(): ModeloAddTask {
            if (instance == null) {
                instance = ModeloAddTask()
            }
            return instance!!
        }

    }

    fun addTask(title: String, description: String, category: String, priority: Int) {
        ProxyUsers.getInstance().addSubcriber(this, Topics.ADD_TASK)
        UserDAO.getInstances().addTask(title, description, category, priority)
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