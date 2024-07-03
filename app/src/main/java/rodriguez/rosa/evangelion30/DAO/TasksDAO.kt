package rodriguez.rosa.evangelion30.DAO

import AuthManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.SubscriptorProxy
import rodriguez.rosa.evangelion30.util.Topics
import org.threeten.bp.LocalDate
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.format.DateTimeFormatter
import rodriguez.rosa.evangelion30.dominio.Tasks
import rodriguez.rosa.evangelion30.util.DataBaseManager
import rodriguez.rosa.evangelion30.util.DataBaseManager.firebaseDatabase
import rodriguez.rosa.evangelion30.util.FiltersManager

class TasksDAO {

    private var filtersManager=FiltersManager.getInstance()

    private var subscriptores: HashMap<String, ArrayList<SubscriptorProxy>> = hashMapOf(
        Topics.ADD_TASK.toString() to ArrayList()
    )

    companion object {

        private var instance: TasksDAO? = null


        fun getInstances(): TasksDAO {
            if(instance == null){
                instance = TasksDAO()
            }

            return instance!!
        }


    }

    fun addSubcriber(sub: SubscriptorProxy, topic: Topics) {

        if (!instance!!.subscriptores.containsKey(topic.toString())) {
            throw RuntimeException("EL TOPIC NO EXISTE")
        }

        if(!instance!!.subscriptores.get(topic.toString())!!.contains(sub)){
            instance!!.subscriptores.get(topic.toString())!!.add(sub)
        }

    }

    fun fetchTasks(): ArrayList<Task> {
        val tasks = Tasks.getInstance().getTasks()
        if (filtersManager.getCategoryFilter().isBlank()) {
            // No filters applied, return all tasks
            return tasks
        }

        filterByCategory()

        Log.e(null ,"AAAAAAAAAAAAAAAAAAA"+Tasks.getInstance().getShowingTasks())

        return Tasks.getInstance().getShowingTasks()
    }

    // Filter tasks by category
    private fun filterByCategory() {
        val categoryFilter = filtersManager.getCategoryFilter()
        val showingTasks = Tasks.getInstance().getShowingTasks()

        showingTasks.clear()
        showingTasks.addAll(Tasks.getInstance().getTasks().filter {
            it.categoria.equals(categoryFilter, ignoreCase = true)
        })

        Log.e("FilteredTasks", "Filtered tasks: ${showingTasks.map { it.titulo }}")
    }

    fun orderPriority() {
        val showingTasks = Tasks.getInstance().getTasks()
                showingTasks.sortByDescending { it.prioridad }
        Log.e("FilteredTasks", "Filtered tasks: ${showingTasks.map { it.titulo }}")
        }


    fun orderDescendentDates() {
        val showingTasks = Tasks.getInstance().getTasks()
        showingTasks.sortBy { it.fecha }

    }

    fun orderAscendentDates() {
        val showingTasks = Tasks.getInstance().getTasks()
        showingTasks.sortByDescending { it.fecha }

    }
    }
