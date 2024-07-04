package rodriguez.rosa.evangelion30

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import com.loper7.date_time_picker.number_picker.NumberPicker
import rodriguez.rosa.evangelion30.Controladores.ControladorAddTask

import rodriguez.rosa.evangelion30.DAO.TasksDAO
import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloEditTask



import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class Add_Task_Activity : AppCompatActivity(), Subscriptor{


    data class fechas(val dia: Int, val mes: Int, val anio: Int)

    //    Atributos de la pantalla
    private lateinit var tituloActividad: TextView
    private lateinit var textoTitulo: EditText
    private lateinit var textoDescripcion: EditText
    private lateinit var spinner: Spinner
    private lateinit var textoCategoria: EditText
    private lateinit var botonAgregarCategoria: Button
    private lateinit var layoutCategorias: GridLayout
    private lateinit var seleccionadorAnio: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var seleccionadorMes: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var seleccionadorDia: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var checkBoxSinFecha: CheckBox
    private lateinit var botonAgregar: Button
    private lateinit var llCategories: LinearLayout

    private var categoriesList = mutableListOf<String>()
    private val selectedCategories = mutableSetOf<String>()
//    fin de los atributos de la pantalla

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidThreeTen.init(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        botonAgregar          = findViewById(R.id.botonAgregarTarea)
        textoTitulo           = findViewById<EditText>(R.id.tituloTarea)
        textoDescripcion      = findViewById<EditText>(R.id.descripcionTarea)
        spinner               = findViewById<Spinner>(R.id.prioritySpinner)
        textoCategoria        = findViewById<EditText>(R.id.textoCategoria)
        botonAgregarCategoria = findViewById<Button>(R.id.botonAgregarCategoria)
        layoutCategorias      = findViewById(R.id.layoutCategorias)
        seleccionadorAnio     = findViewById(R.id.np_datetime_year)
        seleccionadorMes      = findViewById(R.id.np_datetime_month)
        seleccionadorDia      = findViewById(R.id.np_datetime_day)
        checkBoxSinFecha      = findViewById<CheckBox>(R.id.checkboxDeadline)
        tituloActividad       = findViewById<TextView>(R.id.tituloActividad)
        llCategories = findViewById(R.id.llCategories)

        //Modificación del texto del boton y el titulo de la actividad
        botonAgregar.text = "AGREGAR TAREA"
        tituloActividad.text = "Agregar tarea"

        setearDatos()
        setearBotones()

        ModeloAddTask.getInstance().addSubscriber(this)
        categoriesList = loadCategories()
        addCheckboxesFromCategories(this,llCategories)
        botonAgregar.setOnClickListener {


            Log.e(null,"WORKING?")

            //subscribir la vista al modelo
            ModeloAddTask.getInstance().addSubscriber(this)

            val title = textoTitulo.text.toString()
            val description = textoDescripcion.text.toString()
            val category = textoCategoria.text.toString()
            val priority = (spinner.selectedItem as String).toInt()

            val day = seleccionadorDia.value
            val month = seleccionadorMes.value
            val year = seleccionadorAnio.value

            val date = "%02d/%02d/%04d".format(day, month, year)

            val hasLimitDate = !checkBoxSinFecha.isChecked





            if (isAnyFieldBlank(title, description, category, priority)) {
                Toast.makeText(
                    this, "Favor de llenar los campos de manera correcta",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }


            ControladorAddTask.getInstance().addTask(
                title, description, category, priority, date
            )
        }


        botonAgregarCategoria.setOnClickListener {
            val newCategory = textoCategoria.text.toString().trim()
            if (newCategory.isNotEmpty() && !categoriesList.contains(newCategory)) {
                addCategoryCheckBox(newCategory)
                categoriesList.add(newCategory)
                saveCategories(categoriesList,this)
            }
        }

    }

    private fun isAnyFieldBlank(title: String, description: String, category: String, priority: Int ): Boolean {
        return title.isBlank() || description.isBlank()|| category.isBlank()|| priority.toString().isBlank()

    }

    private fun setearDatos(): Unit {

        ArrayAdapter.createFromResource(
            this, R.array.taskPriorities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val timestamp = org.threeten.bp.LocalDate.now()

        seleccionadorAnio.maxValue = 2050
        seleccionadorAnio.minValue = timestamp.year

        seleccionadorDia.minValue = 1
        seleccionadorDia.maxValue = timestamp.month.maxLength()

        seleccionadorMes.minValue = 1
        seleccionadorMes.maxValue = 12

        seleccionadorMes.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromMonth(picker, oldVal, newVal)
        }

        seleccionadorAnio.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromYear(picker, oldVal, newVal)
        }

    }

    private fun updateDateFromMonth(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (newVal < 10) {
            month = month.plus("0")
        }
        month = month.plus(newVal)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${seleccionadorAnio.value}-${month}-01")
        seleccionadorDia.maxValue = fechaReferencia.month.maxLength()
        seleccionadorDia.minValue = 1
    }

    private fun updateDateFromYear(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (seleccionadorMes.value < 10) {
            month = month.plus("0")
        }
        month = month.plus(seleccionadorMes.value)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${newVal}-${month}-01")

//        por alguna razon toma todos los anios como leapYear asi q le tenemos que restar uno
//        al mes de febrero
        var monthValue = fechaReferencia.month.maxLength()
        if (!fechaReferencia.isLeapYear && fechaReferencia.month.value == 2) {
            monthValue -= 1
        }

        seleccionadorDia.maxValue = monthValue
        seleccionadorDia.minValue = 1
    }

    private fun setearBotones(): Unit {

        botonAgregar.setOnClickListener {


            Log.e(null,"WORKING?")

            //subscribir la vista al modelo
            ModeloAddTask.getInstance().addSubscriber(this)

            val title = textoTitulo.text.toString()
            val description = textoDescripcion.text.toString()
            val category = textoCategoria.text.toString()
            val priority = (spinner.selectedItem as String).toInt()

            val day = seleccionadorDia.value
            val month = seleccionadorMes.value
            val year = seleccionadorAnio.value

            val date = "%04d-%02d-%02d".format(year, month, day)

            if (isAnyFieldBlank(title, description, category, priority)) {
                Toast.makeText(
                    this, "Favor de llenar los campos de manera correcta",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            ControladorAddTask.getInstance().addTask(
                title, description, category, priority, date
            )

        }

    }


    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }


    private fun addCategoryCheckBox(category: String) {
        val checkBox = CheckBox(this).apply {
            text = category

            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedCategories.add(category)
                } else {
                    selectedCategories.remove(category)
                }
            }
        }
        llCategories.addView(checkBox)
    }




    override fun notificar(data: NotificacionesUsuario) {

        if (data == NotificacionesUsuario.TAREA_AGREGADA_CON_EXITO) {
            mostrarMensaje("Tarea agregada con exito!")
        } else if (data == NotificacionesUsuario.ERROR_AL_AGREGAR_TAREA) {
            mostrarMensaje("Error al agregar la tarea")
        }

        val homeActivity: Intent = Intent(this, MainActivity::class.java)
        this.startActivity(homeActivity)
        this.finish()
    }

    fun loadCategories(): MutableList<String> {
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val categoriesSet = sharedPreferences.getStringSet("categories", emptySet())
        return (categoriesSet?.toList() ?: emptyList()).toMutableList()
    }

    fun saveCategories(categories: List<String>, context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("categories", categories.toSet())
        editor.apply()
    }

    private fun addCheckboxesFromCategories(context: Context, layout: LinearLayout) {
        val categories = loadCategories()

        for (category in categories) {
            val checkBox = CheckBox(context)
            checkBox.text = category

            llCategories.addView(checkBox)
        }
    }

}