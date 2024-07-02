package rodriguez.rosa.evangelion30.dominio

class Tasks {

    private val tareas: ArrayList<Task> = ArrayList()

    companion object {
        val instance: Tasks = Tasks()
    }

    fun agregarTarea(task: Task) {
        this.tareas.add(task)
    }

    fun eliminarTarea(task: Task) {
         tareas.remove(task)
    }
}