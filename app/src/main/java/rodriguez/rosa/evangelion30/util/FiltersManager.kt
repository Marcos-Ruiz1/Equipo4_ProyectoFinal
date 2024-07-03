package rodriguez.rosa.evangelion30.util


class FiltersManager {
    private var categoryFilter: String =""
    private var priority: Boolean = false
    private var fechaAscendente: Boolean = false
    private var fechaDescendente: Boolean = false
    private var tareasTerminadasFilter: Boolean = true


    companion object {
        private var instance: FiltersManager? = null

        fun getInstance(): FiltersManager {
            if (instance == null) {
                instance = FiltersManager()
            }
            return instance!!
        }
    }

    fun getCategoryFilter():String{
        return categoryFilter
    }

    fun setCategoryFilter(category: String){
        this.categoryFilter=category
    }

    fun getPriorityFilter():Boolean{
        return priority
    }

    fun turnOnPriorityFilter(){
        this.priority=true
        this.fechaAscendente=false
        this.fechaDescendente=false
        this.tareasTerminadasFilter=false
    }

    fun turnOffPriorityFilter(){
        this.priority=false
        this.fechaAscendente=false
        this.fechaDescendente=false
        this.tareasTerminadasFilter=false
    }

    fun getFechaAscendenteFilter():Boolean{
        return fechaAscendente
    }

    fun turnOnFechaAscendenteFilter(){
        this.fechaAscendente=true
        this.fechaDescendente=false
        this.priority=false
        this.tareasTerminadasFilter=false
    }

    fun turnOffFechaAscendenteFilter(){
        this.fechaAscendente=false
        this.fechaDescendente=false
        this.priority=false
        this.tareasTerminadasFilter=false
    }

    fun getFechaDescendenteFilter():Boolean{
        return fechaDescendente
    }

    fun turnOnFechaDescendenteFilter(){
        this.fechaDescendente=true
        this.fechaAscendente=false
        this.priority=false
        this.tareasTerminadasFilter=false
    }

    fun turnOffFechaDescendenteFilter(){
        this.fechaDescendente=false
        this.fechaAscendente=false
        this.priority=false
        this.tareasTerminadasFilter=false
    }

    fun getTareasTerminadasFilter():Boolean{
        return tareasTerminadasFilter
    }

    fun turnOnTareasTerminadasFilter(){
        this.tareasTerminadasFilter=true
        this.fechaDescendente=false
        this.fechaAscendente=false
        this.priority=false
    }

    fun turnOffTareasTerminadasFilter(){
        this.tareasTerminadasFilter=false
        this.fechaDescendente=false
        this.fechaAscendente=false
        this.priority=false
    }

    fun turnEverythingOff(){
        this.categoryFilter=""
        this.fechaDescendente=false
        this.fechaAscendente=false
        this.priority=false
        this.tareasTerminadasFilter=true
    }


    fun isAnyFilterApplied(): Boolean{
        return (!tareasTerminadasFilter || priority|| fechaAscendente || fechaDescendente || !categoryFilter.isBlank())
    }


}