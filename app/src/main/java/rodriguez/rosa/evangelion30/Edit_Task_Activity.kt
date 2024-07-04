package rodriguez.rosa.evangelion30

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import rodriguez.rosa.evangelion30.Controladores.ControladorEditTask
import rodriguez.rosa.evangelion30.Modelo.ModeloEditTask
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class Edit_Task_Activity : AppCompatActivity(), Subscriptor {

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
    private lateinit var checkBoxSinFehca: CheckBox
    private lateinit var botonAgregar: Button
    private lateinit var checkboxContainer : LinearLayout
    private lateinit var categoryList : MutableList<String>
    private lateinit var id: String
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
        checkBoxSinFehca      = findViewById<CheckBox>(R.id.checkboxDeadline)
        tituloActividad       = findViewById<TextView>(R.id.tituloActividad)
        checkboxContainer     = findViewById(R.id.checkboxContainer)
        id = intent.getStringExtra("id").toString()

        if (this.intent.extras != null) {
            setExtras(this.intent.extras!!)
        }

        setearDatos()
        setearBotones()

        categoryList = loadCategories()
        addCheckboxesFromCategories(this,checkboxContainer)

        ModeloEditTask.getInstance().addSubscriber(this)
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

        seleccionadorAnio.maxValue = timestamp.year + 50
        seleccionadorAnio.minValue = timestamp.year

        seleccionadorDia.minValue = 1
        seleccionadorDia.maxValue = timestamp.month.maxLength()

        seleccionadorMes.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromMonth(picker, oldVal, newVal)
        }

        seleccionadorAnio.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromYear(picker, oldVal, newVal)
        }

        seleccionadorMes.minValue = 1
        seleccionadorMes.maxValue = 12
    }

    private fun updateDateFromMonth(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (newVal < 10) {
            month = month.plus("0")
        }
        month = month.plus(newVal)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${seleccionadorAnio.value}-${month}-01")
        month.plus(seleccionadorMes.value)

//        por alguna razon toma todos los anios como leapYear asi q le tenemos que restar uno
//        al mes de febrero
        var monthValue = fechaReferencia.month.maxLength()
        if (!fechaReferencia.isLeapYear && fechaReferencia.month.value == 2) {
            monthValue -= 1
        }

        seleccionadorDia.maxValue = monthValue
        seleccionadorDia.minValue = 1
    }

    private fun updateDateFromYear(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (seleccionadorMes.value < 10) {
            month = month.plus("0")
        }
        month = month.plus(seleccionadorMes.value)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${newVal}-${month}-01")
        seleccionadorDia.maxValue = fechaReferencia.month.maxLength()
        seleccionadorDia.minValue = 1
    }

    private fun setearBotones(): Unit {

        botonAgregar.setOnClickListener {
//            toda la logica para guardar la tarea o verificar que la tarea tiene un formato adecuado
            val intent: Intent = Intent(this, MainActivity::class.java)
            val day = seleccionadorDia.value
            val month = seleccionadorMes.value
            val year = seleccionadorAnio.value
            val date = "%02d/%02d/%04d".format(day, month, year)

            val selectedCategories = mutableListOf<String>()
            for (i in 0 until checkboxContainer.childCount) {
                val view = checkboxContainer.getChildAt(i)
                if (view is CheckBox && view.isChecked) {
                    selectedCategories.add(view.text.toString())
                }
            }



            val task = Task(
                id = id,
                titulo = textoTitulo.text.toString(),
                descripcion = textoDescripcion.text.toString(),
                fecha = date,
                categoria = listToString(selectedCategories),
                prioridad =  (spinner.selectedItem as String).toInt(),
                terminado = false
            )

            ControladorEditTask.getInstance().editTask(task)
        }

        botonAgregarCategoria.setOnClickListener{
            val category = textoCategoria.text.toString().trim()

            if (category.isNotEmpty()) {
                if (!categoryList.contains(category)) {
                    addCategoryCheckBox(category)
                    categoryList.add(category)
                    saveCategories(categoryList)  // Guardar categorías en el archivo local
                    Toast.makeText(this, "Categoría guardada: $category", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "La categoría ya existe: $category", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ingrese una categoría válida", Toast.LENGTH_SHORT).show()
            }

            // Limpiar el campo después de agregar
            textoCategoria.text.clear()
        }
    }

    private fun setExtras(extras: Bundle): Unit {

        val titulo = extras.getString("titulo") ?: "TAREA SIN TITULO"
        val descripcion = extras.getString("descripcion") ?: "TAREA SIN DESCRIPCION"
        val categoria = extras.getString("categoria") ?: "TAREA SIN CATEGORIA"
        val fecha = getFecha(extras.getString("fecha") ?: "01/01/2000")

        textoTitulo.setText(titulo)
        textoDescripcion.setText(descripcion)
        textoCategoria.setText(categoria)
        seleccionadorDia.value = fecha.dia
        seleccionadorMes.value = fecha.mes
        seleccionadorAnio.value = fecha.anio

        botonAgregar.text = resources.getText(R.string.tituloEditarTarea)
        tituloActividad.text = resources.getText(R.string.tituloEditarTarea)

    }

    private fun getFecha(fecha: String): fechas {
        val values = fecha.split("/")
        return fechas(values[0].toInt(), values[1].toInt(), values[2].toInt())
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun notificar(data: NotificacionesUsuario) {

        if (data == NotificacionesUsuario.TAREA_EDITADA_CON_EXITO) {
            mostrarMensaje("Tarea editada con exito!")
        } else if (data == NotificacionesUsuario.ERROR_AL_EDITAR_TAREA) {
            mostrarMensaje("Error al editar la tarea")
        }

        this.startActivity(intent)
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    fun saveCategories(categories: List<String>) {
        val sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("categories", categories.toSet())
        editor.apply()
    }


    fun loadCategories(): MutableList<String> {
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

    private fun addCategoryCheckBox(category: String) {
        val checkBox = CheckBox(this).apply {
            text = category

        }
        checkboxContainer.addView(checkBox)
    }

    fun listToString(list: MutableList<String>): String {
        return list.joinToString(separator = ", ")
    }


}