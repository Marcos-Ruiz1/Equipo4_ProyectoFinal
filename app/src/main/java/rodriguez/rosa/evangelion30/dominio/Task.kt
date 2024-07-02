package rodriguez.rosa.evangelion30.dominio

data class Task(
    var titulo: String,
    var descripcion: String,
    var fecha: String,  //Date
    var categoria: String,//List
    var prioridad: Int,
    var terminado: Boolean)