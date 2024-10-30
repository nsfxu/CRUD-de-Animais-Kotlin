package br.com.cotemig.exercicio4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        val btnVoltar: Button = findViewById(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnCadastrar: Button = findViewById(R.id.btnCadastrar)
        btnCadastrar.setOnClickListener{
            cadastrarAnimal()
        }
    }

    fun cadastrarAnimal () {
        val etNome: EditText = findViewById(R.id.etNome)
        if(etNome.text.toString().isEmpty()){
            enviarMensagem("Insira um nome!")

            return;
        }


        val etRaca: EditText = findViewById(R.id.etRaca)
        if(etRaca.text.toString().isEmpty()){
            enviarMensagem("Insira uma raça!")

            return;
        }


        val etIdade: EditText = findViewById(R.id.etIdade)
        if(etIdade.text.toString().isEmpty()){
            enviarMensagem("Insira uma idade!")

            return;
        }

        var novoAnimal: AnimalModel = AnimalModel()

        novoAnimal.nome = etNome.text.toString()
        novoAnimal.raca = etRaca.text.toString()
        novoAnimal.idade = etIdade.text.toString()

        criarAnimalReq(novoAnimal)

    }

    fun criarAnimalReq (novoAnimal: AnimalModel) {
        val instance = RetrofitUtil.getInstance(Vars.baseUrl)
        val endpoint = instance.create(IAnimalEndpoint::class.java)
        val contexto = this

        endpoint.create(novoAnimal).enqueue(object : Callback<AnimalModel> {
            override fun onResponse(call: Call<AnimalModel>, response: Response<AnimalModel>) {
                if(response.isSuccessful && response.body() != null){
                    enviarMensagem("Animal criado com o ID: ${response.body()!!.id.toString()}")
                }
            }

            override fun onFailure(call: Call<AnimalModel>, t: Throwable) {
                enviarMensagem("Erro na chamada")
            }
        })
    }

    fun enviarMensagem (mensagem: String){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
    }
}