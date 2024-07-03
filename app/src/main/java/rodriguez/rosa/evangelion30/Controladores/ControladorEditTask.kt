package rodriguez.rosa.evangelion30.Controladores

import android.content.Intent
import android.util.Log
import rodriguez.rosa.evangelion30.MainActivity
import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloEditTask
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.DataBaseManager

class ControladorEditTask {

    companion object {

        private var instance: ControladorEditTask? = null

        fun getInstance(): ControladorEditTask {

            if(instance == null){
                instance = ControladorEditTask()
            }

            return instance!!
        }

    }

    fun editTask(task: Task){
        ModeloEditTask.getInstance().editTask(task)
    }

}