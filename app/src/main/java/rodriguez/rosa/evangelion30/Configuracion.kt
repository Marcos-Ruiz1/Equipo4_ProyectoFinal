package rodriguez.rosa.evangelion30

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Configuracion : AppCompatActivity() {

    private var adaptador: Configuracion.AdaptadorConf? = null
    var options  = ArrayList<String>()
    var categorias  = ArrayList<String>()

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

        //Instancia del adaptador y establecerlo al GridView para que muestras las tasks
        adaptador = Configuracion.AdaptadorConf(this, options,categorias)
        val tasksGridView: GridView = findViewById(R.id.tasksGridViewConf)
        tasksGridView.adapter = adaptador

    }

    fun setUp(){
        options.add("Todas")
        options.add("Fecha ascendente")
        options.add("Fecha descendente")
        options.add("Prioridad")
        options.add("Categoría")
        options.add("Matemáticas")
        options.add("Escuela")
    }

    private class AdaptadorConf: BaseAdapter {

        var options =  ArrayList<String>()
        var contexto: Context? = null
        var categorias  = ArrayList<String>()

        constructor(contexto: Context, options:ArrayList<String>, categorias: ArrayList<String>){
            this.options=options
            this.categorias=categorias
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return options.size
        }

        override fun getItem(position: Int): Any {
            return options[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        /**
         * Métoodo que se encarga de cargar la vista con la lista de tareas en el GridView
         */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val conf = options[position]
            val inflater = LayoutInflater.from(contexto)
            var vista = convertView

            if (vista == null) {
                vista = inflater.inflate(R.layout.configuracion_padre, parent, false)
            }

            // Now you can find views within your inflated layout and set their properties
            val textView = vista?.findViewById<TextView>(R.id.groupItem)
            if (textView != null) {
                textView.text = conf
            }  // Set text based on your data

            return vista!!
        }

    }

}