package com.example.workaudio.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDAO {

    //READ
    @Query("SELECT * FROM TokenRoomEntity")
    fun readToken(): Flow<TokenRoomEntity>

    @Query("SELECT * FROM TokenRoomEntity")
    suspend fun getToken(): TokenRoomEntity

    @Query("SELECT * FROM WorkoutRoomEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLatestWorkout(): WorkoutRoomEntity

    @Query("SELECT * FROM WorkoutRoomEntity")
    fun getAllWorkouts(): Flow<List<WorkoutRoomEntity>>

    @Query("SELECT * FROM WorkoutTracksRoomEntity WHERE playlistId = :id")
    fun getWorkoutTracks(id: Int): Flow<List<WorkoutTracksRoomEntity>>

    @Query("SELECT * FROM WorkoutRoomEntity WHERE id = :id")
    fun getWorkout(id: Int): Flow<WorkoutRoomEntity>

    @Query("SELECT * FROM SearchedTracksRoomEntity")
    fun getSearchedTracks(): Flow<List<SearchedTracksRoomEntity>>


    //UPDATE
    @Query("UPDATE WorkoutRoomEntity SET currentDuration = currentDuration + :currentDuration WHERE id = :id")
    suspend fun updateWorkoutCurrentDuration(currentDuration: Int, id: Int)

    @Query("UPDATE WorkoutRoomEntity SET duration = :duration WHERE id = :id")
    suspend fun updateWorkoutDuration(duration: Int, id: Int)

    @Query("UPDATE WorkoutRoomEntity SET name = :name WHERE id = :id")
    suspend fun updateWorkoutName(name: String, id: Int)

    //INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchedTracks(tracks: List<SearchedTracksRoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(tokenEntity: TokenRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workoutEntity: WorkoutRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutTrack(workoutTracksEntity: WorkoutTracksRoomEntity)

    //DELETE
    @Query("DELETE FROM WorkoutTracksRoomEntity WHERE playlistId = :id AND uri = :uri")
    suspend fun deleteWorkoutTrack(uri: String, id: Int)

    @Query("DELETE FROM TokenRoomEntity")
    suspend fun clearToken()

}