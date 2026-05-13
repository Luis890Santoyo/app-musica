package com.example.app_musica

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

// Modelo de datos para el Álbum
data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val description: String,
    val image: String
)

// Interfaz para la API de música
interface MusicApiService {
    @GET("getAlbums")
    suspend fun getAlbums(): List<Album>

    @GET("getAlbum/{id}")
    suspend fun getAlbumById(@Path("id") id: String): Album
}

// Cliente Retrofit para manejar las peticiones
object RetrofitClient {
    private const val BASE_URL = "https://pjasoft.com/musica/"

    val instance: MusicApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicApiService::class.java)
    }
}
