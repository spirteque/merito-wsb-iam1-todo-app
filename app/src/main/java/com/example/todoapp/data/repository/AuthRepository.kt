package com.example.todoapp.data.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

/**
 * Repozytorium odpowiedzialne za logowanie i rejestrację przez Firebase Auth.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    /** Czy użytkownik jest aktualnie zalogowany. */
    val isLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    /** Email zalogowanego użytkownika lub null jeśli nikt nie jest zalogowany. */
    val userEmail: String?
        get() = firebaseAuth.currentUser?.email

    /** Rejestracja nowego użytkownika. Zwraca Result z sukcesem lub błędem. */
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Logowanie istniejącego użytkownika. */
    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Wylogowanie. */
    fun signOut() = firebaseAuth.signOut()
}