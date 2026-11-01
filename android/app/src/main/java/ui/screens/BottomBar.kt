package ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BottomBar(
    selected: Int,
    onItemSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF252525)
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = {
                onItemSelected(0)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Главная"
                )
            },
            label = {
                Text("Главная")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFBDBDBD),
                unselectedTextColor = Color(0xFFBDBDBD),
                indicatorColor = Color(0xFFEF3124)
            )
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = {
                onItemSelected(1)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Кейсы"
                )
            },
            label = {
                Text("Кейсы")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFBDBDBD),
                unselectedTextColor = Color(0xFFBDBDBD),
                indicatorColor = Color(0xFFEF3124)
            )
        )

        NavigationBarItem(
            selected = selected == 2,
            onClick = {
                onItemSelected(2)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Рейтинг"
                )
            },
            label = {
                Text("Рейтинг")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFBDBDBD),
                unselectedTextColor = Color(0xFFBDBDBD),
                indicatorColor = Color(0xFFEF3124)
            )
        )
        NavigationBarItem(
            selected = selected == 3,
            onClick = {
                onItemSelected(3)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Профиль"
                )
            },
            label = {
                Text("Профиль")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color(0xFFBDBDBD),
                unselectedTextColor = Color(0xFFBDBDBD),
                indicatorColor = Color(0xFFEF3124)
            )
        )
    }
}