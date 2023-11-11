package br.com.jpm.fitnessmap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import br.com.jpm.fitnessmap.model.App
import br.com.jpm.fitnessmap.model.Calc

class ImcActivity : AppCompatActivity() {
    private lateinit var editWeigth: EditText
    private lateinit var editHeigth: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imc)

        editWeigth = findViewById(R.id.edit_imc_weight)
        editHeigth = findViewById(R.id.edit_imc_height)

        val btnSend: Button = findViewById(R.id.btn_imc_send)
        btnSend.setOnClickListener {
            if (!validateForm()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val weight = editWeigth.text.toString().toInt()
            val height = editHeigth.text.toString().toInt()

            val result = calculateImc(weight, height)
//            Log.d("teste", "$result")
            val imcResponseId = imcResponse(result)
//            Toast.makeText(this,imcResponseId,Toast.LENGTH_LONG).show()
            val title = getString(R.string.imc_response, result)
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(imcResponseId)
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which ->
                    //                   criar logia para salvar na base do celular
                }
                .setNegativeButton(R.string.save) { _, _ ->
                    Thread{
                        val app = application as App
                        val dao = app.db.calcDao()
                        dao.insert(
                            Calc(
                                type = "imc",
                                res = result
                            )
                        )
                        runOnUiThread{
                            openListActivity()
                        }
                    }.start()
//                    Toast.makeText(this@ImcActivity, R.string.calc_saved,Toast.LENGTH_LONG).show()

                }
                .create()
                .show()
            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
    }

    private fun calculateImc(weigth: Int, height: Int): Double {
        // peso / (altura * altura)
        return weigth / ((height / 100.0) * (height / 100.0))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_search){
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity(){
        val intent = Intent(this@ImcActivity, ListCalcActivity::class.java)
        intent.putExtra("type","imc")
        startActivity(intent)
    }
    @StringRes
    private fun imcResponse(imc: Double): Int {
        return when {
            imc < 15.0 -> R.string.imc_severely_low_weight
            imc < 16.0 -> R.string.imc_very_low_weight
            imc < 18.5 -> R.string.imc_low_weight
            imc < 25.0 -> R.string.normal
            imc < 30.0 -> R.string.imc_high_weight
            imc < 35.0 -> R.string.imc_so_high_weight
            imc < 40.0 -> R.string.imc_severely_high_weight
            else -> R.string.imc_severely_low_weight
        }
    }

    private fun validateForm(): Boolean {
        //nao pode inserir valores nulos / vazio e nao pode começar com 0
        return (editHeigth.text.toString().isNotEmpty()
                && editWeigth.text.toString().isNotEmpty()
                && !editHeigth.text.toString().startsWith("0")
                && !editWeigth.text.toString().startsWith("0"))
    }

}