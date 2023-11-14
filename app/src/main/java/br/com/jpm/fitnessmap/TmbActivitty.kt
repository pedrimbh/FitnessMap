package br.com.jpm.fitnessmap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.jpm.fitnessmap.model.App
import br.com.jpm.fitnessmap.model.Calc

class TmbActivitty : AppCompatActivity() {

    private lateinit var editWeigth: EditText
    private lateinit var editHeigth: EditText
    private lateinit var editAge: EditText
    private lateinit var lifestyle: AutoCompleteTextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmb)
        lifestyle = findViewById(R.id.auto_lifestyle)
        val items = resources.getStringArray(R.array.tbm_lifestyle)
        lifestyle.setText(items.first())
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        lifestyle.setAdapter(adapter)

        editWeigth = findViewById(R.id.edit_tmb_weight)
        editHeigth = findViewById(R.id.edit_tmb_height)
        editAge = findViewById(R.id.edit_tmb_age)
        val btnSend: Button = findViewById(R.id.btn_tmb_send)
        btnSend.setOnClickListener {
            if (!validateForm()) {
                Toast.makeText(this, R.string.fields_message, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val weight = editWeigth.text.toString().toInt()
            val height = editHeigth.text.toString().toInt()
            val age = editAge.text.toString().toInt()

            val tmbResponse = calculateTmb(weight, height, age)
//            Log.d("teste", "$result")
            val result = tmbRequest(tmbResponse)
//            Toast.makeText(this,imcResponseId,Toast.LENGTH_LONG).show()
            val title = getString(R.string.tmb_response, result)
            AlertDialog.Builder(this)
                .setTitle(title)
//                .setMessage(tmbResponseId.toString())
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, which ->
                    //                   criar logia para salvar na base do celular
                }
                .setNegativeButton(R.string.save) { _, _ ->
                    Thread {
                        val app = application as App
                        val dao = app.db.calcDao()
                        val updateId = intent.extras?.getInt("updateId")
                        if (updateId != null) {
                            dao.update(Calc(id = updateId, type = "tmb", res = result))
                        } else {
                            dao.insert(Calc(type = "tmb", res = result))
                        }

                        runOnUiThread {
                            openListActivity()
                        }
                    }.start()

                }
                .create()
                .show()
            val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun calculateTmb(weigth: Int, height: Int, age: Int): Double {
        // peso / (altura * altura)
        return 66 + (13.8 * weigth) + (5 * height) - (6.8 * age)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            finish()
            openListActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openListActivity() {
        val intent = Intent(this@TmbActivitty, ListCalcActivity::class.java)
        intent.putExtra("type", "tmb")
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        //nao pode inserir valores nulos / vazio e nao pode começar com 0
        return (editHeigth.text.toString().isNotEmpty()
                && editWeigth.text.toString().isNotEmpty()
                && editAge.text.toString().isNotEmpty()
                && !editHeigth.text.toString().startsWith("0")
                && !editWeigth.text.toString().startsWith("0")
                && !editAge.text.toString().startsWith("0"))
    }

    private fun tmbRequest(tmb: Double): Double {
        val item = resources.getStringArray(R.array.tbm_lifestyle)
        when {
            lifestyle.text.toString() == item[0] -> return tmb * 1.2

            lifestyle.text.toString() == item[1] -> return tmb * 1.375

            lifestyle.text.toString() == item[2] -> return tmb * 1.55

            lifestyle.text.toString() == item[3] -> return tmb * 1.725

            lifestyle.text.toString() == item[4] -> return tmb * 1.9

            else -> return 0.0
        }
    }




}