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
        try {
            album = RetrofitClient.instance.getAlbumById(albumId)
            isLoading = false
        } catch (e: Exception) {
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
                    Box(modifier = Modifier.fillMaxWidth().height(400.dp).clip(RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp))) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentAlbum.image).setHeader("User-Agent", "Mozilla/5.0").build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)), startY = 500f)))
                        
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            IconButton(onClick = onBack, modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), CircleShape)) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                            }
                            IconButton(onClick = onFavoriteClick, modifier = Modifier.background(Color.Black.copy(alpha = 0.3f), CircleShape)) {
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
                            Row {
                                Button(
                                    onClick = { onPlayClick(currentAlbum) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                                    shape = CircleShape,
                                    modifier = Modifier.size(56.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                }
                            }
                        }
                    }
                }
                
                item {
                    Card(modifier = Modifier.padding(16.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("Sobre este álbum", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(currentAlbum.description, color = Color.Gray)
                        }
                    }
                }

                itemsIndexed(List(5) { it }) { index, _ ->
                    RecentlyPlayedItem(album = currentAlbum, onClick = {}, onPlayClick = { onPlayClick(currentAlbum) })
                }
            }
        }
    }
}
