package co.com.crudclase
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.com.crudclase.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding){

            tvTextoChiquito.setOnClickListener{

                viewSwitcher.displayedChild = 1 // Segunda vista
            }

            tvTextoChiquitoRegi.setOnClickListener{
                viewSwitcher.displayedChild = 0 // Primera vista
            }

            btnIngresarRegi.setOnClickListener{
                with(mainActivity){
                    val user = etUserRegi.text.toString()
                    val password = etPasswordRegi.text.toString()
                    val correo = etCorreoRegi.text.toString()

                    registerUser(user, password, correo)
                }
            }

            btnIngresar.setOnClickListener{
                with(mainActivity){
                    val user = etUser.text.toString()
                    val password = etPassword.text.toString()

                    val status = autentificathionUser(user, password)
                    if (status) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}