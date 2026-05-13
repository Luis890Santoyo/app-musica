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
    var isLoading by remember { mutableStateOf(true) }

    val bgColor = Color(0xFFE6E1FF)


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
                                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Buenos dias!", color = Color.White.copy(alpha = 0.8f), fontSize = 18.sp)
                            Text("Luis Santoyo", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }

                item {
                    SectionHeader(title = "Albums")
                    LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                        items(albums) { album ->
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

                item {
                    SectionHeader(title = "Recently Played")
                }

                items(albums) { album ->
                    RecentlyPlayedItem(
                        album = album,
                        onClick = { onAlbumClick(album.id) }
                    )
                }
            }
        }
    }
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
            Text(text = "See more", color = Color(0xFF7C4DFF))
        }
    }
}
