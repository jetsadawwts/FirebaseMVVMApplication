package com.jetsada.firebasemvvmapplication.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jetsada.firebasemvvmapplication.data.repository.auth.AuthImplRepository
import com.jetsada.firebasemvvmapplication.data.repository.auth.AuthRepository
import com.jetsada.firebasemvvmapplication.data.repository.note.NoteImplRepository
import com.jetsada.firebasemvvmapplication.data.repository.note.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(fireStore: FirebaseFirestore): NoteRepository {
        return NoteImplRepository(fireStore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(fireStore: FirebaseFirestore, fireAuth: FirebaseAuth): AuthRepository {
        return AuthImplRepository(fireStore, fireAuth)
    }
}