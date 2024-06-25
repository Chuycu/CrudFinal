package co.com.crudclase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.com.crudclase.databinding.ActivityMainBinding
import io.github.muddz.styleabletoast.StyleableToast

open class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var SQLiteHelper: SQLHelper
    private var adapter: MedAdapter? = null
    private var medModel: MedModel? = null


    open fun metodoHeredado() {
        // Implementación del método en la clase base
        println("Método heredado ejecutado desde MainActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        SQLiteHelper = SQLHelper(this)
        initRecyclerView()
        with(binding) {

            btnGuardar.setOnClickListener {
                val nombreMed = etNombreProducto.text.toString()
                val precioMed = etPrecioMedicamento.text.toString()
                val dosisMed = etDosisMedicamento.text.toString()

                addEstudent(nombreMed, precioMed, dosisMed)
            }

            btnListar.setOnClickListener {
                getMedPersona()
                viewSwitcher.displayedChild = 1// Mostrar la segunda vista (RecyclerView)

            }

            btnLimpiar.setOnClickListener {
                clearText()
            }

            btnActualizar.setOnClickListener{
                val nombreMed = etNombreProducto.text.toString()
                val precioMed = etPrecioMedicamento.text.toString()
                val dosisMed = etDosisMedicamento.text.toString()

                updateMedicamento(nombreMed, precioMed, dosisMed)
            }


            adapter?.setOnClickItem {
                //esto se ejecuta cuando se hace clic en un ítem del RecyclerView
                Toast.makeText(this@MainActivity, it.nombre, Toast.LENGTH_SHORT).show()

                with(binding) {
                    btnGuardar.isEnabled = false
                    etNombreProducto.setText(it.nombre)
                    etPrecioMedicamento.setText(it.precio)
                    etDosisMedicamento.setText(it.dosis)
                    // Guarda toda la información del estudiante en estModel
                    medModel = it
                }
                viewSwitcher.displayedChild = 0
            }

            adapter?.setOnClickDeleteItem {
                deleteMedicamento(it.id)
            }

            btnHome.setOnClickListener {
                viewSwitcher.displayedChild = 0 // Mostrar la primera vista (Formulario)
            }

        }
    }

    private fun updateMedicamento(nombreMed: String, precioMed: String, dosisMed: String) {
        if(nombreMed.equals(medModel?.nombre) && precioMed.equals(medModel?.precio) && dosisMed.equals(medModel?.dosis)){
            StyleableToast.makeText(
                this,
                "No se actualiza el medicamento.",
                R.style.error_toast
            ).show()
            clearText()
            return
        }
        if(medModel == null){
            StyleableToast.makeText(
                this,
                "Debe dar click a un medicamento de la lista",
                R.style.error_toast
            ).show()
            return
        }

        if (nombreMed.isEmpty() || precioMed.isEmpty() || dosisMed.isEmpty()) {
            StyleableToast.makeText(
                this,
                "Debe ingresar nombre, precio y cantidad de productos.",
                R.style.error_toast
            ).show()
        }

        val medAct = MedModel(medModel?.id,  nombreMed, precioMed, dosisMed)
        val status = SQLiteHelper.actMedicamento(medAct)
        if (status >- 1) {
            clearText()
            getMedPersona()
            StyleableToast.makeText(
                this,
                "Actualizado correctamente.",
                R.style.success_toast
            ).show()
        } else {
            StyleableToast.makeText(
                this,
                "Error al actualizar el estudiante.",
                R.style.error_toast
            ).show()
        }
    }

    private fun deleteMedicamento(id: Int?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Desea eliminar Permanentemente el medicamento?")
        builder.setCancelable(true)
        builder.setPositiveButton("Si"){dialog, _ ->
            SQLiteHelper.deleteMedicamentoById(id)
            getMedPersona()
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()

    }

    private fun clearText() {
        with(binding) {
            btnGuardar.setEnabled(true)
            etNombreProducto.setText("")
            etPrecioMedicamento.setText("")
            etDosisMedicamento.setText("")
            etNombreProducto.requestFocus()
        }
    }

    private fun initRecyclerView() {
        binding.rvListmed.layoutManager = LinearLayoutManager(this)
        adapter = MedAdapter()
        binding.rvListmed.adapter = adapter
    }


    private fun addEstudent(nombreMed: String, precioMed: String, dosisMed: String) {

        if (nombreMed.isEmpty() || precioMed.isEmpty() || dosisMed.isEmpty()) {
            StyleableToast.makeText(
                this,
                "Ingrese nombre del medicamento.",
                R.style.error_toast
            ).show()
        }else if (dosisMed.length <0) {
            StyleableToast.makeText(
                this,
                "El producto tiene que tener un valor mayor a 0 pesos",
                R.style.error_toast
            ).show()
        }else {
            val med = MedModel(null, nombreMed, precioMed, dosisMed)
            val status = SQLiteHelper.insertMedPer(med)
            if (status > -1) {
                clearText()
                getMedPersona()
                StyleableToast.makeText(
                    this,
                    "Agregado correctamente.",
                    R.style.success_toast
                ).show()
            } else {
                StyleableToast.makeText(
                    this,
                    "Error al guardar medicamento.",
                    R.style.error_toast
                ).show()
            }
        }
    }

    private fun getMedPersona() {
        val MedList = SQLiteHelper.getMedPersona()
        adapter?.addItems(MedList)
    }

    internal fun registerUser(user:String, email: String, password: String  ) {
        if (email.isEmpty() || password.isEmpty() || user.isEmpty() ) {
            StyleableToast.makeText(
                this,
                "Debe ingresar nombre, correo y contraseña.",
                R.style.error_toast
            ).show()
        }else{
            val user = MedModelUser(null, user, email, password )
            val status = SQLiteHelper.insertUser(user)
            if (status > -1) {
                StyleableToast.makeText(
                    this,
                    "Agregado correctamente.",
                    R.style.success_toast
                ).show()
            } else {
                StyleableToast.makeText(
                    this,
                    "Error al guardar usuario.",
                    R.style.error_toast
                ).show()
            }
        }
    }
    internal fun autentificathionUser(user: String, password: String): Boolean {
        var status =  false
        if ( password.isEmpty() || user.isEmpty() ) {
            StyleableToast.makeText(
                this,
                "Debe ingresar nombre, correo y contraseña.",
                R.style.error_toast
            ).show()
        }else {
            val userLogin = MedModelUser(null, user, null, password )
            status = SQLiteHelper.Authentification(userLogin)
            if (status) {
                StyleableToast.makeText(
                    this,
                    "Bienvenido",
                    R.style.success_toast).show()

            }
            return status
        }
    return status
    }
}

