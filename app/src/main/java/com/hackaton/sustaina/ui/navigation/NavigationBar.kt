package com.hackaton.sustaina.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hackaton.sustaina.R

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Routes.Landing to R.drawable.icon_landing,
        Routes.Camera to R.drawable.icon_camera,
        Routes.Map to R.drawable.icon_map
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(horizontal = 25.dp, vertical = 15.dp),
        shape = RoundedCornerShape(35.dp),
        tonalElevation = 3.dp,
        color = MaterialTheme.colorScheme.primary
    ) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                val (destination, drawableRes) = item
                val targetIconSize = if (currentRoute == destination.route) 34.dp else 30.dp
                val animatedIconSize by animateDpAsState(targetValue = targetIconSize)

                NavigationBarItem(
                    selected = currentRoute == destination.route,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .height(45.dp)
                                .width(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = drawableRes),
                                contentDescription = destination.route,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(animatedIconSize)
                            )
                        }
                    },
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
