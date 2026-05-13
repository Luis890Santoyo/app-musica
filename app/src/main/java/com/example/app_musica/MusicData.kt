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

fun getMockAlbums(): List<Album> {
    return listOf(
        Album("1", "Midnights", "Taylor Swift", "Lavender Haze y más éxitos.", "https://upload.wikimedia.org/wikipedia/en/9/9f/Midnights_-_Taylor_Swift.png"),
        Album("2", "After Hours", "The Weeknd", "Incluye Blinding Lights.", "https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png"),
        Album("3", "Future Nostalgia", "Dua Lipa", "Sonido disco moderno.", "https://upload.wikimedia.org/wikipedia/en/f/f5/Dua_Lipa_-_Future_Nostalgia.png"),
        Album("4", "Sour", "Olivia Rodrigo", "Pop rock emocional.", "https://upload.wikimedia.org/wikipedia/en/b/b2/Olivia_Rodrigo_-_Sour.png"),
        Album("5", "Harry's House", "Harry Styles", "As It Was y más.", "https://upload.wikimedia.org/wikipedia/en/d/d5/Harry_Styles_-_Harry%27s_House.png"),
        Album("6", "Starboy", "The Weeknd", "Estilo R&B y electrónico.", "https://upload.wikimedia.org/wikipedia/en/3/39/The_Weeknd_-_Starboy.png"),
        Album("7", "Tales of Ithiria", "Haggard", "Un álbum sinfónico que mezcla elementos de música clásica con death metal melódico.", "https://upload.wikimedia.org/wikipedia/en/0/0d/Haggard_Tales_of_Ithiria.jpg")
    )
}
