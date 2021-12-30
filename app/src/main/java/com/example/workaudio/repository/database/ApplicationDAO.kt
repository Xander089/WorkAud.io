package com.example.workaudio.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDAO {

    //POSITION
    @Query("UPDATE CurrentPosition SET position = position + :pos")
    suspend fun updateCurrentPosition(pos: Int)

    @Query("SELECT * FROM CurrentPosition LIMIT 1")
    fun getCurrentPosition(): Flow<CurrentPosition>

    @Query("DELETE FROM CurrentPosition")
    suspend fun clearCurrentPosition()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentPosition(position: CurrentPosition)

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
    fun getWorkoutTracks(id: Int): List<WorkoutTracksRoomEntity>

    @Query("SELECT * FROM WorkoutRoomEntity WHERE id = :id")
    suspend fun getWorkout(id: Int): WorkoutRoomEntity


    //UPDATE


    @Query("UPDATE WorkoutRoomEntity SET duration = :duration WHERE id = :id")
    suspend fun updateWorkoutDuration(duration: Int, id: Int)

    @Query("UPDATE WorkoutRoomEntity SET name = :name WHERE id = :id")
    suspend fun updateWorkoutName(name: String, id: Int)

    //INSERT


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