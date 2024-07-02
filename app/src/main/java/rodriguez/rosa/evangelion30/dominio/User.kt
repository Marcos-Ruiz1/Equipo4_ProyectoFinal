package rodriguez.rosa.evangelion30.dominio

class User {
    var id: String = ""
    var tasksList: MutableList<Task> = mutableListOf() // Renaming tasks to tasksList

    constructor() {}

    constructor(id: String, tasks: MutableList<Task>?) {
        this.id = id
        if (tasks != null) {
            this.tasksList = tasks // Adjusting tasks to tasksList
        }
    }

    fun getTasks(): MutableList<Task> { // Renaming getTasks() method
        return this.tasksList
    }

    fun addTask(task: Task) {
        this.tasksList.add(task) // Adjusting usage accordingly
    }
}
