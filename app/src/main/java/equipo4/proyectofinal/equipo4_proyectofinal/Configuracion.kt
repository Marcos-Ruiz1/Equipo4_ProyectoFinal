package equipo4.proyectofinal.equipo4_proyectofinal

import Equipo4.proyectofinal.equipo4_proyectofinal.R
import android.os.Bundle
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity

class Configuracion : AppCompatActivity() {

    private lateinit var expandableListView: ExpandableListView
    private lateinit var adapter: ExpandableListAdapter
    private lateinit var titleList: List<String>
    private lateinit var dataList: HashMap<String, List<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuracion)

        expandableListView = findViewById(R.id.expandableListView)
        initData()
        adapter = ExpandableListAdapter(this, titleList, dataList)
        expandableListView.setAdapter(adapter)

    }


    private fun initData() {
        titleList = listOf("Categorías", "Fecha ascendente", "Fecha descendente")

        val categorias = listOf("Escuela", "Matemáticas", "Hogar")
        val fechaA = listOf("")
        val fechaD = listOf("")

        dataList = hashMapOf(
            "Categorías" to categorias,
            "Fecha Ascendente" to fechaA,
            "Fecha Descendente" to fechaD
        )
    }
}