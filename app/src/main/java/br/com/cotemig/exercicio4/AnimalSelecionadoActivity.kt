package br.com.cotemig.exercicio4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimalSelecionadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal_selecionado)

        val btnVoltar: Button = findViewById(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

        val btnAtualizar: Button = findViewById(R.id.btnAtualizar)
        btnAtualizar.setOnClickListener{
            atualizarAnimal()
        }

        val btnDeletar: Button = findViewById(R.id.btnDeletar)
        btnDeletar.setOnClickListener{
            deletarAnimalReq()
        }

        val etNome: EditText = findViewById(R.id.etNome)
        etNome.setText(Vars.selectedAnimal?.nome.toString())

        val etRaca: EditText = findViewById(R.id.etRaca)
        etRaca.setText(Vars.selectedAnimal?.raca.toString())

        val etIdade: EditText = findViewById(R.id.etIdade)
        etIdade.setText(Vars.selectedAnimal?.idade.toString())
    }

    fun atualizarAnimal () {
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

        var animalAtualizado: AnimalModel = Vars.selectedAnimal!!

        animalAtualizado.nome = etNome.text.toString()
        animalAtualizado.raca = etRaca.text.toString()
        animalAtualizado.idade = etIdade.text.toString()

        atualizaAnimalReq(animalAtualizado)

    }

    fun atualizaAnimalReq (animalAtualizado: AnimalModel) {
        val instance = RetrofitUtil.getInstance(Vars.baseUrl)
        val endpoint = instance.create(IAnimalEndpoint::class.java)
        val contexto = this

        endpoint.update(animalAtualizado.id.toString(), animalAtualizado).enqueue(object : Callback<AnimalModel> {
            override fun onResponse(call: Call<AnimalModel>, response: Response<AnimalModel>) {
                if(response.isSuccessful && response.body() != null){
                    enviarMensagem("Animal atualizado!")

                    // atualizaDadosAtuais(response.body()!!.id.toString())
                }
            }

            override fun onFailure(call: Call<AnimalModel>, t: Throwable) {
                enviarMensagem("Erro na chamada")
            }
        })
    }

    fun deletarAnimalReq () {
        val instance = RetrofitUtil.getInstance(Vars.baseUrl)
        val endpoint = instance.create(IAnimalEndpoint::class.java)
        val contexto = this
        endpoint.delete(Vars.selectedAnimal!!.id.toString()).enqueue(object : Callback<AnimalModel> {
            override fun onResponse(call: Call<AnimalModel>, response: Response<AnimalModel>) {
                if(response.isSuccessful && response.body() != null){
                    enviarMensagem("${response.body()!!.nome} foi deletado! :(")

                    startActivity(Intent(contexto, MainActivity::class.java))
                }
            }

            override fun onFailure(call: Call<AnimalModel>, t: Throwable) {
                enviarMensagem("Erro na chamada")
            }
        })
    }

    // desnecessário já que os dados atualizados já foram digitados
    fun atualizaDadosAtuais (id: String) {
        val instance = RetrofitUtil.getInstance(Vars.baseUrl)
        val endpoint = instance.create(IAnimalEndpoint::class.java)
        val contexto = this

        endpoint.getAnimal(id).enqueue(object : Callback<AnimalModel> {
            override fun onResponse(call: Call<AnimalModel>, response: Response<AnimalModel>) {
                if(response.isSuccessful && response.body() != null){
                    val novoAnimal: AnimalModel = response.body()!!
                    Vars.selectedAnimal = novoAnimal

                    val etNome: EditText = findViewById(R.id.etNome)
                    etNome.setText(Vars.selectedAnimal?.nome.toString())

                    val etRaca: EditText = findViewById(R.id.etRaca)
                    etRaca.setText(Vars.selectedAnimal?.raca.toString())

                    val etIdade: EditText = findViewById(R.id.etIdade)
                    etIdade.setText(Vars.selectedAnimal?.idade.toString())
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