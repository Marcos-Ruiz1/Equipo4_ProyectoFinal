package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloConfiguracion
import rodriguez.rosa.evangelion30.Modelo.ModeloHome
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.dominio.Task

class ControladorHome {

    companion object {

        private var instance: ControladorHome? = null

        fun getInstance(): ControladorHome {

            if(instance == null){
                instance = ControladorHome()
            }

            return instance!!
        }

    }

    fun refreshTasks():ArrayList<Task> {
        return ModeloHome.getInstance().refreshTasks()
    }

    fun orderPriority(){
        ModeloHome.getInstance().orderPriority()
    }
    fun orderAscendentDates(){
        ModeloHome.getInstance().orderAscendentDates()
    }
    fun orderDescendent(){
        ModeloHome.getInstance().orderDescententDates()
    }
}