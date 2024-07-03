package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloConfiguracion
import rodriguez.rosa.evangelion30.Modelo.ModeloDashBoard
import rodriguez.rosa.evangelion30.Modelo.ModeloHome
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.dominio.Task

class ControladorDashBoard {

    companion object {

        private var instance: ControladorDashBoard? = null

        fun getInstance(): ControladorDashBoard {

            if(instance == null){
                instance = ControladorDashBoard()
            }

            return instance!!
        }

    }

    fun getAllTasks():ArrayList<Task> {
        return ModeloDashBoard.getInstance().getAllTasks()
    }


}