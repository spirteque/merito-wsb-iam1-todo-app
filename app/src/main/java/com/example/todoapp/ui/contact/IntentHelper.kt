package com.example.todoapp.ui.contact

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.todoapp.R

/**
 * Funkcje pomocnicze do uruchamiania implicit intents.
 * Każda funkcja obsługuje przypadek gdy brak aplikacji do obsługi intentu.
 */
object IntentHelper {

    fun openWebsite(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        tryStart(context, intent)
    }

    fun dialPhone(context: Context, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        tryStart(context, intent)
    }

    fun sendEmail(context: Context, email: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
        tryStart(context, intent)
    }

    fun openMaps(context: Context, query: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$query"))
        tryStart(context, intent)
    }

    private fun tryStart(context: Context, intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.contact_no_app, Toast.LENGTH_SHORT).show()
        }
    }
}