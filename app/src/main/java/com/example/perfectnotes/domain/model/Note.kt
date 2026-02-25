package com.example.perfectnotes.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val category: String = "Umum",
    val color: Int = 0xFFF5F5F5.toInt(),
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val isLocked: Boolean = false,
    val hasReminder: Boolean = false,
    val reminderTime: Long? = null,
    val imagePaths: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) : Parcelable {
    
    val formattedDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
            return dateFormat.format(Date(updatedAt))
        }
    
    val formattedReminderTime: String?
        get() {
            return reminderTime?.let {
                val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id"))
                dateFormat.format(Date(it))
            }
        }
    
    val previewContent: String
        get() {
            return if (content.length > 100) {
                content.substring(0, 97) + "..."
            } else {
                content
            }
        }
    
    val wordCount: Int
        get() {
            return content.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size
        }
    
    val characterCount: Int
        get() = content.length
}