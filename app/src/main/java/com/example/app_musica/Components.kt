package com.example.app_musica

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun MiniPlayer(currentAlbum: Album? = null) {
    var isPlaying by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF1E0E3E),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (currentAlbum != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(currentAlbum.image)
                            .setHeader("User-Agent", "Mozilla/5.0")
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color.Gray))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(currentAlbum?.title ?: "Selecciona una canción", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(currentAlbum?.artist ?: "Artista", color = Color.LightGray, fontSize = 12.sp)
                }
            }
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.background(Color.White, CircleShape).size(40.dp)
            ) {
                Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit, onPlayClick: () -> Unit) {
    Surface(
        modifier = Modifier.width(180.dp).padding(8.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.image)
                    .setHeader("User-Agent", "Mozilla/5.0")
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(album.title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(album.artist, color = Color.LightGray, fontSize = 10.sp, maxLines = 1)
                    }
                    IconButton(onClick = onPlayClick, modifier = Modifier.size(24.dp).background(Color.White, CircleShape)) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit, onPlayClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(album.image).setHeader("User-Agent", "Mozilla/5.0").build(),
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(album.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(album.artist, color = Color.Gray, fontSize = 13.sp)
            }
            IconButton(onClick = onPlayClick) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color(0xFF8B5CF6))
            }
        }
    }
}
