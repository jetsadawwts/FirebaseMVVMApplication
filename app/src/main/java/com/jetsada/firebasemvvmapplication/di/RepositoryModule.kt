package com.jetsada.firebasemvvmapplication.di

import com.google.firebase.firestore.FirebaseFirestore
import com.jetsada.firebasemvvmapplication.data.repository.NoteImplRepository
import com.jetsada.firebasemvvmapplication.data.repository.NoteRepository
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

}