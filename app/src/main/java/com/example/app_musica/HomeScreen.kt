package com.example.app_musica

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    favoriteAlbums: Set<String>,
    onFavoriteClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit,
    onPlayClick: (Album) -> Unit
) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var showOnlyFavorites by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    val bgColor = Color(0xFFE6E1FF)

    // Filtrar álbumes basándose en búsqueda y favoritos
    val filteredAlbums = albums.filter {
        val matchesSearch = it.title.contains(searchQuery, ignoreCase = true) ||
                           it.artist.contains(searchQuery, ignoreCase = true)
        val matchesFavorite = if (showOnlyFavorites) favoriteAlbums.contains(it.id) else true
        matchesSearch && matchesFavorite
    }

    LaunchedEffect(Unit) {
        try {
            val fetchedAlbums = RetrofitClient.instance.getAlbums()
            albums = if (fetchedAlbums.isEmpty()) getMockAlbums() else fetchedAlbums
        } catch (e: Exception) {
            albums = getMockAlbums()
        } finally {
            isLoading = false
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = bgColor) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7C4DFF))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(32.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF8B5CF6))
                    ) {
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                                
                                // Campo de búsqueda dinámico
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Buscar álbum o artista...", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp) },
                                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.White,
                                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                                        cursorColor = Color.White,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                                    singleLine = true
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Botón de filtro para favoritos
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showOnlyFavorites = !showOnlyFavorites },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (showOnlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (showOnlyFavorites) Color.Red else Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Solo mostrar favoritos",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 14.sp,
                                    fontWeight = if (showOnlyFavorites) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            Text("¡Buenos días!", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
                            Text("Luis Santoyo", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                // Mostrar mensaje si no hay resultados
                if (filteredAlbums.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No se encontraron coincidencias", color = Color.Gray)
                        }
                    }
                }

                item {
                    SectionHeader(title = if (searchQuery.isEmpty()) "Álbumes" else "Resultados de búsqueda")
                    LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                        items(filteredAlbums) { album ->
                            AlbumCard(
                                album = album,
                                isFavorite = favoriteAlbums.contains(album.id),
                                onFavoriteClick = { onFavoriteClick(album.id) },
                                onClick = { onAlbumClick(album.id) },
                                onPlayClick = { onPlayClick(album) }
                            )
                        }
                    }
                }

                if (searchQuery.isEmpty()) {
                    item {
                        SectionHeader(title = "Escuchado recientemente")
                    }
                }

                items(filteredAlbums) { album ->
                    RecentlyPlayedItem(
                        album = album,
                        isFavorite = favoriteAlbums.contains(album.id),
                        onFavoriteClick = { onFavoriteClick(album.id) },
                        onClick = { onAlbumClick(album.id) },
                        onPlayClick = { onPlayClick(album) }
                    )
                }
            }
        }
    }
}

fun getMockAlbums(): List<Album> {
    return listOf(
        Album("1", "Midnights", "Taylor Swift", "Lavender Haze y más éxitos.", "https://upload.wikimedia.org/wikipedia/en/9/9f/Midnights_-_Taylor_Swift.png"),
        Album("2", "After Hours", "The Weeknd", "Incluye Blinding Lights.", "https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png"),
        Album("3", "Future Nostalgia", "Dua Lipa", "Sonido disco moderno.", "https://upload.wikimedia.org/wikipedia/en/f/f5/Dua_Lipa_-_Future_Nostalgia.png"),
        Album("4", "Sour", "Olivia Rodrigo", "Pop rock emocional.", "https://upload.wikimedia.org/wikipedia/en/b/b2/Olivia_Rodrigo_-_Sour.png"),
        Album("5", "Harry's House", "Harry Styles", "As It Was y más.", "https://upload.wikimedia.org/wikipedia/en/d/d5/Harry_Styles_-_Harry%27s_House.png"),
        Album("6", "Starboy", "The Weeknd", "Estilo R&B y electrónico.", "https://upload.wikimedia.org/wikipedia/en/3/39/The_Weeknd_-_Starboy.png")
    )
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        TextButton(onClick = { }) {
            Text(text = "Ver más", color = Color(0xFF7C4DFF))
        }
    }
}
