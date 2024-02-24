package com.example.tipapp.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val IconSizeButtonModifier = Modifier.size(40.dp)
@Composable
fun RoundedIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick.invoke() }
            .then(IconSizeButtonModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        )
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "Plus Minus Icon",
            tint = tint
        )
    }
}