package br.com.jpm.fitnessmap

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    //    private lateinit var  btnImc: LinearLayout
    private lateinit var rvMain: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = mutableListOf<MainItem>()
        mainItems.add(
            MainItem(
                id = 1,
                drawableId = R.drawable.ic_laucher_baseline_wb_sunny_24,
                textStrongId = R.string.label_imc,
                Color.GREEN
            )
        )
        mainItems.add(
            MainItem(
                id = 2,
                drawableId = R.drawable.baseline_wallet_24,
                textStrongId = R.string.tmb,
                Color.MAGENTA
            )
        )

//        usando com interface via activity
//        override fun onClick(id: Int) {
//            when (id) {
//                1 -> {
//                    val i = Intent(this, ImcActivity::class.java)
//                    startActivity(i)
//                }
//
//                2 -> {
//                    Log.i("teste", "$id")
//                }
//
//                3 -> {
//                    Log.i("teste", "$id")
//                }
//            }
//        }

//        val adapter = MainAdapter(mainItems, object : OnItemCLickListener {
//            override fun onClick(id: Int) {
//                when (id) {
//                    1 -> {
//                        val i = Intent(this@MainActivity, ImcActivity::class.java)
//                        startActivity(i)
//                    }
//
//                    2 -> {
//                        Log.i("teste", "$id")
//                    }
//
//                    3 -> {
//                        Log.i("teste", "$id")
//                    }
//                }
//            }
//
//        })
        val adapter = MainAdapter(mainItems) { id ->
            when (id) {
                1 -> {
                    val i = Intent(this@MainActivity, ImcActivity::class.java)
                    startActivity(i)
                }

                2 -> {
                    val i = Intent(this@MainActivity, TmbActivitty::class.java)
                    startActivity(i)
                }

                3 -> {
                    Log.i("teste", "$id")
                }
            }
        }
        rvMain = findViewById(R.id.rv_main)
        rvMain.adapter = adapter
        rvMain.layoutManager = GridLayoutManager(this, 2)

//        classe para administrar a recyclerview e suas celuas (os seus layouts de itens)

//        btnImc = findViewById(R.id.btn_imc)
//        btnImc.setOnClickListener {
//            // navegar para proxima tela
//            val i = Intent(this,ImcActivity::class.java)
//            startActivity(i)
//        }
    }


    //        Adapter-> informar como conectar o layout na recycleview
    private inner class MainAdapter(
        private val mainItem: List<MainItem>,
//        private val onItemCLickListener: OnItemCLickListener
        private val onItemCLickListener:(Int) -> Unit
    ) :
        RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
        // responsavel para informar qual é o xml da celula esecifica (item)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            val view = layoutInflater.inflate(R.layout.main_item, parent, false)
            return MainViewHolder(view)
        }

        // para informar quantas celulas interar
        override fun getItemCount(): Int {
            return mainItem.size
        }

        // disparado toda vez que houver uma rolagem na tela e for necessario trocar o
        // conteudo da celula
        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val itemCurrent = mainItem[position]
            holder.bind(itemCurrent)
        }

        private inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: MainItem) {
                val img: ImageView = itemView.findViewById(R.id.item_img)
                val name: TextView = itemView.findViewById(R.id.item_txt_name)
                val container: LinearLayout = itemView.findViewById(R.id.item_container_imc)
                img.setImageResource(item.drawableId)
                name.setText(item.textStrongId)
                container.setBackgroundColor(item.color)
                container.setOnClickListener {
//                    onItemCLickListener.onClick(item.id)
                    onItemCLickListener.invoke(item.id)
                }
//            val buttonTest: Button = itemView.findViewById(R.id.btn_item)
//            val buttonTest = itemView.findViewById<Button>(R.id.btn_item)
//            buttonTest.setText(item.textStrongId)
            }
        }
    }

    // classe da celula em si onde busca a refereça de cada botão


}