package com.example.app_musica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.app_musica.ui.theme.AppmusicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppmusicaTheme {
                MusicAppRootContainer()
            }
        }
    }
}

@Composable
fun MusicAppRootContainer() {
    val navController = rememberNavController()
    var currentPlayingAlbum by remember { mutableStateOf<Album?>(null) }
    var favoriteAlbums by remember { mutableStateOf(setOf<String>()) }

    val toggleFavorite: (String) -> Unit = { id ->
        favoriteAlbums = if (favoriteAlbums.contains(id)) {
            favoriteAlbums - id
        } else {
            favoriteAlbums + id
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MiniPlayer(
                currentAlbum = currentPlayingAlbum,
                onClick = {
                    currentPlayingAlbum?.let { album ->
                        navController.navigate(UniqueDetailRoute(albumId = album.id))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = UniqueHomeRoute,
                enterTransition = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(400)) },
                exitTransition = { androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(400)) }
            ) {
                composable<UniqueHomeRoute> {
                    HomeScreen(
                        favoriteAlbums = favoriteAlbums,
                        onFavoriteClick = toggleFavorite,
                        onAlbumClick = { id ->
                            navController.navigate(UniqueDetailRoute(albumId = id))
                        },
                        onPlayClick = { album ->
                            currentPlayingAlbum = album
                        }
                    )
                }
                composable<UniqueDetailRoute> { backStackEntry ->
                    val detail: UniqueDetailRoute = backStackEntry.toRoute()
                    DetailScreen(
                        albumId = detail.albumId,
                        isFavorite = favoriteAlbums.contains(detail.albumId),
                        onFavoriteClick = { toggleFavorite(detail.albumId) },
                        onBack = { navController.popBackStack() },
                        onPlayClick = { album ->
                            currentPlayingAlbum = album
                        }
                    )
                }
            }
        }
    }
}
