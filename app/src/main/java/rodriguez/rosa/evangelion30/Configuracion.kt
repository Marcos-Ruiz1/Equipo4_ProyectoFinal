package rodriguez.rosa.evangelion30

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import rodriguez.rosa.evangelion30.Controladores.ControladorConfiguracion
import rodriguez.rosa.evangelion30.Controladores.ControladorHome
import rodriguez.rosa.evangelion30.util.FiltersManager

class Configuracion : AppCompatActivity(){

    private var adaptador: Configuracion.AdaptadorConf? = null
    var options  = ArrayList<String>()


    var categorias  = ArrayList<String>()
    private var categoriesList = mutableListOf<String>()
    private lateinit var llCategories: LinearLayout
    private lateinit var showFinishedTasks : CheckBox
    var showingCategories = false
    var creatingButtons = false


    val filterManager = FiltersManager.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_configuracion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setUp()
        categoriesList = loadCategories()

        //Instancia del adaptador y establecerlo al GridView para que muestras las tasks
        adaptador = Configuracion.AdaptadorConf(this, options,categorias)
        val tasksGridView: GridView = findViewById(R.id.tasksGridViewConf)
        tasksGridView.adapter = adaptador
        val categoriaInput: EditText = findViewById(R.id.categoryInput)
        val search: Button = findViewById(R.id.addCategoryButton)
        val categorySwitch : Button = findViewById(R.id.categorySwitch)


        llCategories = findViewById(R.id.layoutCat)

        val notFinishedTaskCheckBox: CheckBox = findViewById(R.id.showFinishedTasks)


        search.setOnClickListener {
            filterManager.setCategoryFilter(categoriaInput.text.toString())
            ControladorHome.getInstance().refreshTasks()
            Log.e(null,"Si")

            val homeActivity: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(homeActivity)
            this.finish()


        }







        categorySwitch.setOnClickListener{

            if(!showingCategories){
                if(!creatingButtons){
                    addCheckboxesFromCategories(this,llCategories)
                    creatingButtons = true
                }

                llCategories.isVisible=true
                showingCategories = true
            }

            else if(showingCategories){
                llCategories.isVisible=false
                showingCategories = false
            }
        }


    }



    private fun loadCategories(): MutableList<String> {
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val categoriesSet = sharedPreferences.getStringSet("categories", emptySet())
        return (categoriesSet?.toList() ?: emptyList()).toMutableList()
    }

    private fun addCheckboxesFromCategories(context: Context, layout: LinearLayout) {
        val categories = loadCategories()

        for (category in categories) {
            val checkBox = CheckBox(context)
            checkBox.text = category

            layout.addView(checkBox)


        }
    }

    fun setUp(){
        options.add("Todas")
        options.add("Fecha ascendente")
        options.add("Fecha descendente")
        options.add("Prioridad")
        options.add("Sin tareas terminadas")

    }

    private class AdaptadorConf(
        private val contexto: Context,
        private val options: ArrayList<String>,
        private val categorias: ArrayList<String>
    ) : BaseAdapter() {

        override fun getCount(): Int {
            return options.size
        }

        override fun getItem(position: Int): Any {
            return options[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val conf = options[position]
            val inflater = LayoutInflater.from(contexto)
            var vista = convertView

            if (vista == null) {
                vista = inflater.inflate(R.layout.configuracion_padre, parent, false)
            }


            val layout = vista?.findViewById<LinearLayout>(R.id.groupItemLayout)
            val textView = vista?.findViewById<TextView>(R.id.groupItemTextView)
            textView?.text = conf  // Set text based on your data


            layout?.isClickable = true
            layout?.setOnClickListener {


                when (position) {
                    0 -> {
                        // Action for "Todas"
                        FiltersManager.getInstance().turnEverythingOff()
                        ControladorHome.getInstance().refreshTasks()


                    }
                    1 -> {
                        // Fecha ascendente

                        FiltersManager.getInstance().turnOnFechaAscendenteFilter()
                        ControladorConfiguracion.getInstance().orderAscendentDates()
                    }
                    2 -> {
                        // Fecha descendente
                        FiltersManager.getInstance().turnOnFechaDescendenteFilter()
                        ControladorConfiguracion.getInstance().orderDescendent()
                    }
                    3 -> {
                        // Prioridad
                        FiltersManager.getInstance().turnOnPriorityFilter()
                        ControladorConfiguracion.getInstance().orderPriority()
                    }
                    4 -> {
                        // Sin tareas terminadas
                        FiltersManager.getInstance().turnOnTareasTerminadasFilter()
                        ControladorConfiguracion.getInstance().orderList()
                    }
                    else -> {
                        // Default action
                        Toast.makeText(contexto, "Unknown option selected", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            return vista!!
        }
    }






}