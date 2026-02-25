package com.example.perfectnotes.domain.usecase

import com.example.perfectnotes.domain.model.Note
import com.example.perfectnotes.domain.repository.INoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    operator fun invoke(): Flow<List<Note>> = repository.getAllNotes()
}

class SearchNotesUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    operator fun invoke(query: String): Flow<List<Note>> = repository.searchNotes(query)
}

class AddNoteUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.insertNote(note)
}

class UpdateNoteUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.updateNote(note)
}

class DeleteNoteUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}

class SoftDeleteNoteUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    suspend operator fun invoke(noteId: String) = repository.softDeleteNote(noteId)
}

class RestoreNoteUseCase @Inject constructor(
    private val repository: INoteRepository
) {
    suspend operator fun invoke(noteId: String) = repository.restoreNote(noteId)
}