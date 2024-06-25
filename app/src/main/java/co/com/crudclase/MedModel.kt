package co.com.crudclase

data class MedModel(
    var id: Int?,
    val nombre: String = "",
    val precio: String = "",
    var dosis : String = ""
)

data class MedModelUser(
    var id: Int?,
    val usuario: String = "",
    val correo: String? = "",
    var password : String = ""
)
