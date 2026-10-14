package com.patcharawadee.main_project

/**
 * ActivityLog: ข้อมูลกิจกรรมล่าสุดในร้าน
 */
data class ActivityLog(
    val id: String,
    val title: String,
    val detail: String,
    val timestamp: String,
    val type: LogType,
)

enum class LogType {
    ADD,
    UPDATE,
    DELETE,
    ALERT
}
