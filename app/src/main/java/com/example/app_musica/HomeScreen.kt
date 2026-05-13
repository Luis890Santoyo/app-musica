package com.example.app_musica

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
fun HomeScreen(onAlbumClick: (String) -> Unit, onPlayClick: (Album) -> Unit) {
    var albums by remember { mutableStateOf<List<Album>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val bgColor = Color(0xFFE6E1FF) // Fondo lavanda claro

    LaunchedEffect(Unit) {
        try {
            albums = RetrofitClient.instance.getAlbums()
            if (albums.isEmpty()) {
                albums = getMockAlbums()
            }
            isLoading = false
        } catch (e: Exception) {
            albums = getMockAlbums()
            isLoading = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgColor
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7C4DFF))
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                // Cabecera
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "¡Buenos días!",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Luis Santoyo",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // Sección de Álbumes
                item {
                    SectionHeader(title = "Álbumes")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(albums) { album ->
                            AlbumCard(
                                album = album,
                                onClick = { onAlbumClick(album.id) },
                                onPlayClick = { onPlayClick(album) }
                            )
                        }
                    }
                }

                // Sección de Reproducciones Recientes
                item {
                    SectionHeader(title = "Escuchado recientemente")
                }

                items(albums) { album ->
                    RecentlyPlayedItem(
                        album = album,
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
        Album("1", "Midnights", "Taylor Swift", "Lavender Haze y más.", "https://upload.wikimedia.org/wikipedia/en/9/9f/Midnights_-_Taylor_Swift.png"),
        Album("2", "After Hours", "The Weeknd", "Blinding Lights.", "https://upload.wikimedia.org/wikipedia/en/c/c1/The_Weeknd_-_After_Hours.png"),
        Album("3", "Future Nostalgia", "Dua Lipa", "Levitating.", "https://upload.wikimedia.org/wikipedia/en/f/f5/Dua_Lipa_-_Future_Nostalgia.png")
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
