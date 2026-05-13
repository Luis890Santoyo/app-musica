package com.example.app_musica

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun DetailScreen(
    albumId: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBack: () -> Unit,
    onPlayClick: (Album) -> Unit
) {
    var album by remember { mutableStateOf<Album?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(albumId) {
        isLoading = true
        try {
            val mockAlbum = getMockAlbums().find { it.id == albumId }
            if (mockAlbum != null) {
                album = mockAlbum
            } else {
                album = RetrofitClient.instance.getAlbumById(albumId)
            }
        } catch (e: Exception) {
            album = getMockAlbums().find { it.id == albumId }
        } finally {
            isLoading = false
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFE6E1FF)) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF7C4DFF))
            }
        } else if (album != null) {
            val currentAlbum = album!!
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 100.dp)) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(420.dp).clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentAlbum.image).setHeader("User-Agent", "Mozilla/5.0").build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 600f)))
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            IconButton(onClick = onBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                            IconButton(onClick = onFavoriteClick) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFavorite) Color.Red else Color.White
                                )
                            }
                        }

                        Column(modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)) {
                            Text(currentAlbum.title, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                            Text(currentAlbum.artist, color = Color.LightGray, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(
                                    onClick = { onPlayClick(currentAlbum) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                                    shape = CircleShape,
                                    modifier = Modifier.size(56.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(32.dp))
                                }
                                Button(
                                    onClick = { },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    shape = CircleShape,
                                    modifier = Modifier.size(56.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(32.dp))
                                }
                            }
                        }
                    }
                }
                
                item {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("About this album", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1E0E3E))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(currentAlbum.description, color = Color.Gray)
                        }
                    }
                }

                item {
                    Surface(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White
                    ) {
                        Text(
                            text = "Artist: ${currentAlbum.artist}",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.Gray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                itemsIndexed(List(3) { it }) { index, _ ->
                    TrackItem(
                        album = currentAlbum,
                        trackNumber = index + 1,
                        onClick = { onPlayClick(currentAlbum) }
                    )
                }
            }
        }
    }
}
