package br.com.cotemig.exercicio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnimalAdapter
    private var lista: ArrayList<AnimalModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCadastrar: Button = findViewById(R.id.btnCadastrar)
        btnCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        recyclerView = findViewById(R.id.rvAnimais)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()

        getAnimais()
    }

    override fun onResume() {
        super.onResume()

        getAnimais()
    }

    private fun getAnimais() {
        val instance = RetrofitUtil.getInstance(Vars.baseUrl)
        val endpoint = instance.create(IAnimalEndpoint::class.java)
        val contexto = this

        endpoint.get().enqueue(object : Callback<ArrayList<AnimalModel>> {
            override fun onResponse(call: Call<ArrayList<AnimalModel>>, response: Response<ArrayList<AnimalModel>>) {
                if (response.isSuccessful && response.body() != null) {
                    val retorno: ArrayList<AnimalModel> = response.body()!!

                    lista.clear()
                    lista = retorno

                    adapter = AnimalAdapter(contexto, lista, this@MainActivity)

                    recyclerView.adapter = adapter
                } else {
                    enviarMensagem("Erro ao carregar dados")
                }
            }

            override fun onFailure(call: Call<ArrayList<AnimalModel>>, t: Throwable) {
                enviarMensagem("Erro na chamada")
            }
        })
    }

    override fun onItemClick(position: Int) {
        if(lista.isNullOrEmpty()){
            enviarMensagem("Erro ao selecionar o animal da lista")

            return;
        }

        val animalEscolhido: AnimalModel = lista[position]

        if(animalEscolhido == null){
            enviarMensagem("Erro ao selecionar o animal da lista")

            return;
        }

        Vars.selectedAnimal = animalEscolhido

        enviarMensagem("Animal escolhido: ${animalEscolhido.nome}")

        startActivity(Intent(this, AnimalSelecionadoActivity::class.java))

    }

    private fun enviarMensagem(mensagem: String) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}
