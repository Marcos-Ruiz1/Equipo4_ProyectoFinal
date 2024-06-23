package Equipo4.proyectofinal.equipo4_proyectofinal

import Dominio.Task
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TasksHome : AppCompatActivity() {

    //Adaptador y tareas
    private var adaptador: AdaptadorTasks? = null
    var tasks  = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Botones
        val configuracionButton: Button = findViewById(R.id.configuracionButton)
        val agregarTaskButton: Button = findViewById(R.id.agregarTaskButton)
        val estadisticasButton: Button = findViewById(R.id.estadisticasButton)

        //Click listeners a los botones
        configuracionButton.setOnClickListener{
            val intent = Intent(this, Configuracion::class.java)
            this.startActivity(intent)
        }

        agregarTaskButton.setOnClickListener {
            val intent = Intent(this, AgregarTask::class.java)
            this.startActivity(intent)
        }

        estadisticasButton.setOnClickListener {
            val intent = Intent(this, Estadisticas::class.java)
            this.startActivity(intent)
        }



        //cargar objetos Task
        fillTasks()

        //Instancia del adaptador y establecerlo al GridView para que muestras las tasks
        adaptador = AdaptadorTasks(this, tasks)
        var tasksGridView: GridView = findViewById(R.id.tasksGridView)
        tasksGridView.adapter = adaptador

    }




    //Método que instancia objetos Task, que luego se mostrarán en pantalla con el GridView
    fun fillTasks(){

        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefe", "10/06/2024"))
        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe", "10/06/2024"))
        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe", "10/06/2024"))
        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe", "10/06/2024"))
        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe", "10/06/2024"))
    }



    //Clase privada creada para cargar en el GridView las tasks
    private class AdaptadorTasks: BaseAdapter{

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

            var tituloTask: TextView = vista.findViewById(R.id.tituloTaskTextView)
            var descripcionTask: TextView = vista.findViewById(R.id.descripcionTaskTextView)
            var fechaTask: TextView = vista.findViewById(R.id.fechaTaskTextView)

            tituloTask.setText(task.titulo)
            descripcionTask.setText(task.descripcion)
            fechaTask.setText(task.fecha)


            return vista

        }

    }


}