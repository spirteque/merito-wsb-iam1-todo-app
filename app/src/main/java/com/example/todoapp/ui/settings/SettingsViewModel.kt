package com.example.todoapp.ui.settings

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel dla SettingsScreen.
 * Udostępnia dane konta i akcję wylogowania.
 * Stan dark mode jest przechowywany w AppRoot (nie tutaj),
 * bo musi wpływać na Theme który jest wyżej w hierarchii.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userEmail: String?
        get() = authRepository.userEmail

    fun signOut() = authRepository.signOut()
}