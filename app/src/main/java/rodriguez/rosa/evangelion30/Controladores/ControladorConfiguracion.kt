package rodriguez.rosa.evangelion30.Controladores

import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloConfiguracion
import rodriguez.rosa.evangelion30.Modelo.ModeloHome
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn

class ControladorConfiguracion {

    companion object {

        private var instance: ControladorConfiguracion? = null

        fun getInstance(): ControladorConfiguracion {

            if(instance == null){
                instance = ControladorConfiguracion()
            }

            return instance!!
        }

    }

    fun filteredFetch() {
        ModeloConfiguracion.getInstance().filteredFetch()
    }

    fun orderList() {
        ModeloConfiguracion.getInstance().filteredFetch()
    }

    fun orderPriority(){
        ModeloConfiguracion.getInstance().orderPriority()
    }
    fun orderAscendentDates(){
        ModeloConfiguracion.getInstance().orderAscendentDates()
    }
    fun orderDescendent(){
        ModeloConfiguracion.getInstance().orderDescententDates()
    }
}