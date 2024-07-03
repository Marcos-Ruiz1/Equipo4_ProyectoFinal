package rodriguez.rosa.evangelion30.dominio

import android.util.Log
import rodriguez.rosa.evangelion30.red.ProxyUsers
import rodriguez.rosa.evangelion30.util.FiltersManager

class Tasks {

    private val tareas: ArrayList<Task> = ArrayList()
    private var showingTasks: ArrayList<Task> = ArrayList()
    private var instance: Tasks? = null

    companion object {
        private var instance: Tasks? = null

        fun getInstance(): Tasks {
            if (instance == null) {
                instance = Tasks()
            }
            return instance!!
        }
    }

    fun getShowingTasks():ArrayList<Task>{
        return showingTasks
    }



    fun agregarTarea(task: Task) {
        this.tareas.add(task)
    }

    fun eliminarTarea(task: Task) {
         tareas.remove(task)
    }

    fun getTasks(): ArrayList<Task>{
        return tareas
    }



    fun clearFilter(){
        showingTasks.clear()
    }

    fun clearTasks(){
        tareas.clear()
        showingTasks.clear()
    }



}