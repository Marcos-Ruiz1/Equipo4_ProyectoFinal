package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn

class ControladorAddTask {

    companion object {

        private var instance: ControladorAddTask? = null

        fun getInstance(): ControladorAddTask {

            if(instance == null){
                instance = ControladorAddTask()
            }

            return instance!!
        }

    }

    fun addTask(title: String, description: String, category: String, priority: Int) {
        ModeloAddTask.getInstance().addTask(title, description, category, priority)
    }

}