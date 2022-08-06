package com.jetsada.firebasemvvmapplication.data.repository.auth

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.gson.Gson
import com.jetsada.firebasemvvmapplication.data.model.User
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.USER
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.USER_SESSION
import com.jetsada.firebasemvvmapplication.util.UiState
import javax.inject.Inject

class AuthImplRepository @Inject constructor(val fireStorage: FirebaseFirestore, val fireAuth: FirebaseAuth, val sharedPreferences: SharedPreferences, val gson: Gson): AuthRepository {

    override fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit) {
        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    user.id = it.result.user?.uid ?: ""
                    updateUserInfo(user) { state ->
                        when(state) {
                            is UiState.Success -> {
                                storeSession(id = it.result.user?.uid ?: "") {
                                    if(it == null) {
                                        result.invoke(UiState.Failure("User register successfully but session failed to store"))
                                    } else {
                                        result.invoke(
                                            UiState.Success("User register successfully")
                                        )
                                    }

                                }
                            }
                            is UiState.Failure -> {
                                result.invoke(
                                    UiState.Failure(state.error)
                                )
                            }
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val document = fireStorage.collection(USER).document(user.id)
        document
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("User has been update successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        fireAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if(it == null) {
                            result.invoke(UiState.Failure("Failed to store local session"))
                        } else {
                            result.invoke(UiState.Success("Login successfully!"))
                        }
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email and password"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        fireAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Email has been sent"))

                } else {
                    result.invoke(UiState.Failure(task.exception?.message))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email"))
            }
    }

    override fun logout(result: () -> Unit) {
        fireAuth.signOut()
        sharedPreferences.edit().putString(USER_SESSION, null).apply()
        result.invoke()
    }

    override fun storeSession(id: String, result: (User?) -> Unit) {
        fireStorage.collection(USER).document(id)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val user = it.result.toObject(User::class.java)
                    sharedPreferences.edit().putString(USER_SESSION, gson.toJson(user)).apply()
                    result.invoke(user)
                } else {
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }

    override fun getSession(result: (User?) -> Unit) {
        val user_str = sharedPreferences.getString(USER_SESSION, null)
        if(user_str == null) {
            result.invoke(null)
        } else {
            val user = gson.fromJson(user_str, User::class.java)
            result.invoke(user)
        }
    }



}