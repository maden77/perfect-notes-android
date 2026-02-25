package com.example.perfectnotes.domain.repository

import com.example.perfectnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNotesByCategory(category: String): Flow<List<Note>>
    fun getNotesByArchiveStatus(isArchived: Boolean): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    fun getTrashedNotes(): Flow<List<Note>>
    fun getAllCategories(): Flow<List<String>>
    
    suspend fun getNoteById(noteId: String): Note?
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun softDeleteNote(noteId: String)
    suspend fun restoreNote(noteId: String)
    suspend fun permanentlyDeleteNote(noteId: String)
    suspend fun permanentlyDeleteOldNotes(daysOld: Int)
}