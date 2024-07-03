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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import rodriguez.rosa.evangelion30.Add_Task_Activity
import rodriguez.rosa.evangelion30.Configuracion
import rodriguez.rosa.evangelion30.Controladores.ControladorHome
import rodriguez.rosa.evangelion30.DescriptionTask
import rodriguez.rosa.evangelion30.R
import rodriguez.rosa.evangelion30.databinding.FragmentHomeBinding
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.dominio.Tasks

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var adaptador: AdaptadorTasks? = null
    private val authManager = AuthManager
    private val userId = authManager.currentUserId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val configuracionButton: Button = binding.configuracionButton
        configuracionButton.setOnClickListener {
            val intent = Intent(requireContext(), Configuracion::class.java)
            startActivity(intent)
        }

        val agregarButton = binding.btnAgregarTarea
        agregarButton.setOnClickListener {
            val intent = Intent(requireContext(), Add_Task_Activity::class.java)
            startActivity(intent)
        }

        fillTasks()

        adaptador = AdaptadorTasks(requireContext(), Tasks.getInstance().getTasks())
        val tasksGridView: GridView = binding.tasksGridView
        tasksGridView.adapter = adaptador
    }

    override fun onResume() {
        super.onResume()
        adaptador?.tasks = ControladorHome.getInstance().refreshTasks()
        adaptador?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fillTasks() {
        val userID: String? = AuthManager.currentUserId

        if (userID != null) {
            val taskReference = FirebaseDatabase.getInstance().reference
                .child("User")
                .child(userID)
                .child("Tasks")

            taskReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Tasks.getInstance().clearTasks()

                    for (taskSnapshot in snapshot.children) {
                        try {
                            val taskMap = taskSnapshot.value as HashMap<*, *>

                            val id = taskMap["id"] as String
                            val titulo = taskMap["titulo"] as String
                            val descripcion = taskMap["descripcion"] as String
                            val fecha = taskMap["fecha"] as String
                            val categoria = taskMap["categoria"] as String
                            val prioridad = (taskMap["prioridad"] as Long).toInt()
                            val terminado = taskMap["terminado"] as Boolean

                            val task = Task(id, titulo, descripcion, fecha, categoria, prioridad, terminado)
                            Tasks.getInstance().agregarTarea(task)
                        } catch (e: Exception) {
                            Log.e("Task Conversion", "Error converting Task", e)
                        }
                    }

                    adaptador?.tasks = ControladorHome.getInstance().refreshTasks()
                    adaptador?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
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
                intent.putExtra("id", task.id)
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