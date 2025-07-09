package com.example.authapp.repository

import android.content.Context
import com.example.authapp.model.LoginRequest
import com.example.authapp.model.UserCreate
import com.example.authapp.model.UserOut
import com.example.authapp.remote.AuthApi
import com.example.authapp.util.TokenManager
import java.lang.Exception

class AuthRepository(private val api: AuthApi, private val context: Context) {
    suspend fun register(email: String, password: String): Result<UserOut> {
        return try {
            val response = api.register(UserCreate(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Erro desconhecido ao registrar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val token = response.body()?.access_token
                if (token != null) {
                    TokenManager.saveToken(context, token)
                    Result.success(token)
                } else {
                    Result.failure(Exception("Token não recebido na resposta do login"))
                }
            } else {
                Result.failure(Exception("Usuário ou senha inválidos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun me(): Result<UserOut> {
        return try {
            val token = TokenManager.getToken(context)
            if (token == null) {
                return Result.failure(Exception("Sem token de autenticação"))
            }
            val response = api.me("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Token inválido ou sessão expirada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        TokenManager.clearToken(context)
    }
}