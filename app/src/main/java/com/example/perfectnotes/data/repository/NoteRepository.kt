package com.example.perfectnotes.data.repository

import com.example.perfectnotes.data.local.dao.NoteDao
import com.example.perfectnotes.data.local.entity.NoteEntity
import com.example.perfectnotes.domain.model.Note
import com.example.perfectnotes.domain.repository.INoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDao: NoteDao
) : INoteRepository {
    
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getNotesByCategory(category: String): Flow<List<Note>> {
        return noteDao.getNotesByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getNotesByArchiveStatus(isArchived: Boolean): Flow<List<Note>> {
        return noteDao.getNotesByArchiveStatus(isArchived).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getTrashedNotes(): Flow<List<Note>> {
        return noteDao.getTrashedNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getAllCategories(): Flow<List<String>> {
        return noteDao.getAllCategories()
    }
    
    override suspend fun getNoteById(noteId: String): Note? {
        return noteDao.getNoteById(noteId)?.toDomain()
    }
    
    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note.toEntity())
    }
    
    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note.toEntity().copy(updatedAt = System.currentTimeMillis()))
    }
    
    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note.toEntity())
    }
    
    override suspend fun softDeleteNote(noteId: String) {
        noteDao.softDeleteNote(noteId, System.currentTimeMillis())
    }
    
    override suspend fun restoreNote(noteId: String) {
        val note = noteDao.getNoteById(noteId)
        note?.let {
            noteDao.updateNote(it.copy(deletedAt = null))
        }
    }
    
    override suspend fun permanentlyDeleteNote(noteId: String) {
        val note = noteDao.getNoteById(noteId)
        note?.let {
            noteDao.deleteNote(it)
        }
    }
    
    override suspend fun permanentlyDeleteOldNotes(daysOld: Int) {
        val threshold = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L)
        noteDao.permanentlyDeleteOldNotes(threshold)
    }
    
    private fun NoteEntity.toDomain(): Note {
        return Note(
            id = this.id,
            title = this.title,
            content = this.content,
            category = this.category,
            color = this.color,
            isPinned = this.isPinned,
            isArchived = this.isArchived,
            isLocked = this.isLocked,
            hasReminder = this.hasReminder,
            reminderTime = this.reminderTime,
            imagePaths = this.imagePaths,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
    
    private fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = this.id,
            title = this.title,
            content = this.content,
            category = this.category,
            color = this.color,
            isPinned = this.isPinned,
            isArchived = this.isArchived,
            isLocked = this.isLocked,
            hasReminder = this.hasReminder,
            reminderTime = this.reminderTime,
            imagePaths = this.imagePaths,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            deletedAt = this.deletedAt
        )
    }
}