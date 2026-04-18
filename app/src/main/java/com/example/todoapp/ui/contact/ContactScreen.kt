package com.example.todoapp.ui.contact

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

/**
 * Ekran kontaktowy — pokazuje przyciski z implicit intents.
 * Każdy przycisk otwiera odpowiednią zewnętrzną aplikację.
 */
@Composable
fun ContactScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.contact_title),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        ContactButton(
            icon = Icons.Default.Email,
            label = stringResource(R.string.contact_email),
            onClick = { IntentHelper.sendEmail(context, "kontakt@example.com") }
        )

        ContactButton(
            icon = Icons.Default.Phone,
            label = stringResource(R.string.contact_phone),
            onClick = { IntentHelper.dialPhone(context, "+48123456789") }
        )

        ContactButton(
            icon = Icons.Default.Language,
            label = stringResource(R.string.contact_website),
            onClick = { IntentHelper.openWebsite(context, "https://www.youtube.com/watch?v=dQw4w9WgXcQ") }
        )

        ContactButton(
            icon = Icons.Default.Map,
            label = stringResource(R.string.contact_maps),
            onClick = { IntentHelper.openMaps(context, "Merito Wroclaw") }
        )
    }
}

@Composable
private fun ContactButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(label)
    }
}