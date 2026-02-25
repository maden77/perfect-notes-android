package com.example.perfectnotes.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.perfectnotes.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL AND category = :category ORDER BY updatedAt DESC")
    fun getNotesByCategory(category: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL AND isArchived = :isArchived ORDER BY updatedAt DESC")
    fun getNotesByArchiveStatus(isArchived: Boolean): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%')")
    fun searchNotes(query: String): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC")
    fun getTrashedNotes(): Flow<List<NoteEntity>>
    
    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): NoteEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)
    
    @Update
    suspend fun updateNote(note: NoteEntity)
    
    @Delete
    suspend fun deleteNote(note: NoteEntity)
    
    @Query("UPDATE notes SET deletedAt = :deletedAt WHERE id = :noteId")
    suspend fun softDeleteNote(noteId: String, deletedAt: Long)
    
    @Query("DELETE FROM notes WHERE deletedAt IS NOT NULL AND deletedAt < :threshold")
    suspend fun permanentlyDeleteOldNotes(threshold: Long)
    
    @Query("SELECT DISTINCT category FROM notes WHERE deletedAt IS NULL")
    fun getAllCategories(): Flow<List<String>>
}