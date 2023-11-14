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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.jpm.fitnessmap.model.App
import br.com.jpm.fitnessmap.model.Calc
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalcActivity : AppCompatActivity(),OnListClickListener {
    private lateinit var adapter: ListCalcAdapter
    private lateinit var calcItem: MutableList<Calc>
    private lateinit var rvList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_calc)
        calcItem = mutableListOf<Calc>()
        adapter = ListCalcAdapter(calcItem,this)
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
        private val calcItem: List<Calc>,
        private val listener: OnListClickListener
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
                val type =
                    intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found")
                val tv : LinearLayout= itemView.findViewById(R.id.item_result_container)
                val dateValue: TextView = itemView.findViewById(R.id.txt_result_date)
                val name: TextView = itemView.findViewById(R.id.txt_result_value)
                val transformDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt","BR"))
                val data = transformDate.format(item.createDate)
                val btnDelete = itemView.findViewById<ImageView>(R.id.delete_item_img)
                btnDelete.setOnClickListener {
                    listener.onClickDelete(adapterPosition,item)
                }
                tv.setOnClickListener {
                    onClickUpdate(item.id,type)
                }
                dateValue.text = data
                name.text = "%.2f".format(item.res)

            }
        }
    }

    override fun onClickUpdate(id: Int, type: String) {
        when(type) {
            "imc" -> {
                val intent = Intent(this, ImcActivity::class.java)
                // FIXME: passando o ID do item que precisa ser atualizado, ou seja, na outra tela
                // FIXME: vamos buscar o item e suas propriedades com esse ID
                intent.putExtra("updateId", id)
                startActivity(intent)
            }
            "tmb" -> {
                val intent = Intent(this, TmbActivitty::class.java)
                intent.putExtra("updateId", id)
                startActivity(intent)
            }
        }
        finish()
    }

    override fun onClickDelete(position: Int, calc: Calc) {
        Thread {
            val app = application as App
            val dao = app.db.calcDao()
            dao.delete(calc)
            runOnUiThread {
                calcItem.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }.start()
    }

}