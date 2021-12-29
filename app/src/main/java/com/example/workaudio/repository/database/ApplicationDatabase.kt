package com.example.workaudio.repository.database

import androidx.room.*

@Database(
    entities = [
        WorkoutRoomEntity::class,
        WorkoutTracksRoomEntity::class,
        TokenRoomEntity::class,
//        SearchedTracksRoomEntity::class
    ], version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun applicationDao(): ApplicationDAO
}


@Entity
data class WorkoutRoomEntity(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "duration") val duration: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Entity
data class WorkoutTracksRoomEntity(
    @ColumnInfo(name = "playlistId") var playlistId: Int? = -1,
    @ColumnInfo(name = "uri") val uri: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "duration") val duration: Int?,
    @ColumnInfo(name = "artist") val artist: String?,
    @ColumnInfo(name = "album") val album: String?,
    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}

//@Entity
//data class SearchedTracksRoomEntity(
//    @ColumnInfo(name = "playlistId") var playlistId: Int? = -1,
//    @ColumnInfo(name = "uri") val uri: String?,
//    @ColumnInfo(name = "name") val name: String?,
//    @ColumnInfo(name = "duration") val duration: Int?,
//    @ColumnInfo(name = "artist") val artist: String?,
//    @ColumnInfo(name = "album") val album: String?,
//    @ColumnInfo(name = "imageUrl") val imageUrl: String?,
//    ) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//
//}
@Entity
data class TokenRoomEntity(
    @ColumnInfo(name = "token") val token: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
