package rodriguez.rosa.evangelion30.ui.home

import AuthManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import rodriguez.rosa.evangelion30.Add_Task_Activity
import rodriguez.rosa.evangelion30.Configuracion
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.DescriptionTask
import rodriguez.rosa.evangelion30.Edit_Task_Activity
import rodriguez.rosa.evangelion30.R
import rodriguez.rosa.evangelion30.databinding.FragmentHomeBinding
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.DataBaseManager.databaseReference

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    del equipo

    private var adaptador: AdaptadorTasks? = null

    //Instances the authManager
    private val authManager = AuthManager
    //Gets the current user's ID
    val userId = authManager.currentUserId

    var tasks  = ArrayList<Task>()





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        config

//Botones
        val configuracionButton: Button = root.findViewById(R.id.configuracionButton)

        //Click listeners a los botones
        configuracionButton.setOnClickListener{
            val intent = Intent(this.context, Configuracion::class.java)
            this.startActivity(intent)
        }

        val agregarButton = root.findViewById<Button>(R.id.btnAgregarTarea)

        agregarButton.setOnClickListener {
            val intent: Intent = Intent(root.context, Add_Task_Activity::class.java)
            root.context.startActivity(intent)
        }

        //Gets the logged user's ID


        //cargar objetos Task
        fillTasks()

        //Instancia del adaptador y establecerlo al GridView para que muestras las tasks
        adaptador = AdaptadorTasks(root.context, tasks)
        val tasksGridView: GridView = root.findViewById(R.id.tasksGridView)
        tasksGridView.adapter = adaptador

//        fin del config
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun fillTasks() {
        val userID: String? = AuthManager.currentUserId

        if (userID != null) {
            val taskReference = FirebaseDatabase.getInstance().reference
                .child("User")
                .child(userID)
                .child("Tasks")

            taskReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()

                    for (taskSnapshot in snapshot.children) {
                        try {
                            // Extract data from the snapshot as a HashMap
                            val taskMap = taskSnapshot.value as HashMap<*, *>

                            // Create a Task object manually from the HashMap
                            val titulo = taskMap["titulo"] as String
                            val descripcion = taskMap["descripcion"] as String
                            val fecha = taskMap["fecha"] as String
                            val categoria = taskMap["categoria"] as String
                            val prioridad = (taskMap["prioridad"] as Long).toInt() // Firebase returns Long for Integers
                            val terminado = taskMap["terminado"] as Boolean

                            val task = Task(titulo, descripcion, fecha, categoria, prioridad, terminado)
                            tasks.add(task)
                        } catch (e: Exception) {
                            Log.e("Task Conversion", "Error converting Task", e)
                        }
                    }

                    // Update the adapter with the new list of tasks
                    adaptador?.tasks = tasks as ArrayList<Task>
                    adaptador?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancelled event
                    Log.e("Firebase Database", "Error fetching Tasks", error.toException())
                }
            })
        }
    }




    private class AdaptadorTasks: BaseAdapter {

        var tasks =  ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>){
            this.tasks = tasks
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): Any {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        /**
         * Métoodo que se encarga de cargar la vista con la lista de tareas en el GridView
         */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var task = tasks[position]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view, null)


            if(task.terminado){
                vista = inflador.inflate(R.layout.task_finished_view, null)
            }

            val tituloTask: TextView = vista.findViewById(R.id.tituloTaskTextView)
            val descripcionTask: TextView = vista.findViewById(R.id.descripcionTaskTextView)
            val fechaTask: TextView = vista.findViewById(R.id.fechaTaskTextView)
            val terminarTurno: Button = vista.findViewById(R.id.terminarButton)
            val prioridadTask: TextView = vista.findViewById(R.id.prioridadTaskTextView)
            val categoriaTask: TextView = vista.findViewById(R.id.categoriaTaskTextView)

            tituloTask.setText(task.titulo)
            descripcionTask.setText(task.descripcion)
            fechaTask.setText(task.fecha)
            prioridadTask.setText(task.prioridad.toString())
            categoriaTask.setText(task.categoria)

            terminarTurno.setOnClickListener {

                val intent = Intent(contexto, DescriptionTask::class.java)
                intent.putExtra("titulo", task.titulo)
                intent.putExtra("descripcion", task.descripcion)
                intent.putExtra("fecha", task.fecha)
                intent.putExtra("categoria", task.categoria)
                intent.putExtra("prioridad", task.prioridad)
                intent.putExtra("terminado", task.terminado)
                contexto?.startActivity(intent)
            }



            return vista

        }




    }

}