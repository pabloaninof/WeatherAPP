package com.example.myapplication.data.repository

import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, passwordHash: String): Result<UserEntity>
    suspend fun register(username: String, email: String, passwordHash: String): Result<UserEntity>
    fun getCurrentUser(userId: Int): Flow<UserEntity?>
    suspend fun updateUserPreferences(user: UserEntity)
}

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, passwordHash: String): Result<UserEntity> {
        val user = userDao.getUserByEmailAndPassword(email, passwordHash)
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Credenciales incorrectas o usuario no encontrado."))
        }
    }

    override suspend fun register(username: String, email: String, passwordHash: String): Result<UserEntity> {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(Exception("El correo electrónico ya está registrado."))
        }
        val newUser = UserEntity(
            username = username,
            email = email,
            passwordHash = passwordHash
        )
        val id = userDao.insertUser(newUser)
        return Result.success(newUser.copy(id = id.toInt()))
    }

    override fun getCurrentUser(userId: Int): Flow<UserEntity?> {
        return userDao.getUserById(userId)
    }

    override suspend fun updateUserPreferences(user: UserEntity) {
        userDao.updateUser(user)
    }
}
