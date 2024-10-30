package br.com.cotemig.exercicio4

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IAnimalEndpoint {
    @GET("animal")
    fun get(): Call<ArrayList<AnimalModel>>

    @POST("animal")
    fun create(@Body animal: AnimalModel): Call<AnimalModel>

    @GET("animal/{id}")
    fun getAnimal(@Path("id") id: String): Call<AnimalModel>

    @PUT("animal/{id}")
    fun update(@Path("id") id: String, @Body animal: AnimalModel): Call<AnimalModel>

    @DELETE("animal/{id}")
    fun delete(@Path("id") id: String): Call<AnimalModel>

}