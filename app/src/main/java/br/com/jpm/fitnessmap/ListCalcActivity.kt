package br.com.jpm.fitnessmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.fitnessmap.model.App
import br.com.jpm.fitnessmap.model.Calc
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalcActivity : AppCompatActivity() {

    private lateinit var rvList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)
        val calcItem = mutableListOf<Calc>()
        val adapter = ListCalcAdapter(calcItem)
        rvList = findViewById(R.id.rv_list_result)
        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this)
        val type =
            intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")
        val titleList = findViewById<TextView>(R.id.rv_title_list)
        titleList.text = type.uppercase()
        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            val response = dao.getRegisterByType(type)
            runOnUiThread {
                calcItem.addAll(response)
                adapter.notifyDataSetChanged()
            }
        }.start()


    }

    private inner class ListCalcAdapter(
        private val calcItem: List<Calc>
    ) :
        RecyclerView.Adapter<ListCalcActivity.ListCalcAdapter.ListCalcViewHolder>() {
        // responsavel para informar qual é o xml da celula esecifica (item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcViewHolder {
            val view = layoutInflater.inflate(R.layout.result_list, parent, false)
            return ListCalcViewHolder(view)
        }

        // para informar quantas celulas interar
        override fun getItemCount(): Int {
            return calcItem.size
        }

        // disparado toda vez que houver uma rolagem na tela e for necessario trocar o
        // conteudo da celula
        override fun onBindViewHolder(holder: ListCalcViewHolder, position: Int) {
            val itemCurrent = calcItem[position]
            holder.bind(itemCurrent)
        }

        private inner class ListCalcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: Calc) {
                val dateValue: TextView = itemView.findViewById(R.id.txt_result_date)
                val name: TextView = itemView.findViewById(R.id.txt_result_value)
                val transformDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt","BR"))
                val data = transformDate.format(item.createDate)
                dateValue.text = data
                name.text = "%.2f".format(item.res)

            }
        }
    }

}