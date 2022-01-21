package com.example.workaudio.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDAO {


    //READ
    @Query("SELECT * FROM WorkoutTracksRoomEntity WHERE playlistId = :workoutId")
    fun getWorkoutTracksFlow(workoutId: Int): Flow<List<WorkoutTracksRoomEntity>>

    @Query("SELECT * FROM WorkoutRoomEntity WHERE id = :workoutId")
    fun getWorkoutById(workoutId: Int): Flow<WorkoutRoomEntity>

    @Query("SELECT * FROM TokenRoomEntity")
    fun readToken(): Flow<TokenRoomEntity>

    @Query("SELECT * FROM TokenRoomEntity")
    suspend fun getToken(): TokenRoomEntity

    @Query("SELECT * FROM WorkoutRoomEntity ORDER BY id DESC LIMIT 1")
    suspend fun getLatestWorkout(): WorkoutRoomEntity

    @Query("SELECT * FROM WorkoutRoomEntity ORDER BY id DESC LIMIT 1")
    fun getLatestWorkoutAsFlow(): Flow<WorkoutRoomEntity>

    @Query("SELECT * FROM WorkoutRoomEntity")
    fun getAllWorkouts(): Flow<List<WorkoutRoomEntity>>

    @Query("SELECT * FROM WorkoutTracksRoomEntity WHERE playlistId = :id")
    fun getWorkoutTracks(id: Int): List<WorkoutTracksRoomEntity>

    @Query("SELECT * FROM WorkoutTracksRoomEntity WHERE playlistId = :id")
    fun getFlowWorkoutTracks(id: Int): Flow<List<WorkoutTracksRoomEntity>>

    @Query("SELECT * FROM WorkoutTracksRoomEntity WHERE playlistId = :id LIMIT 1")
    suspend fun getWorkoutTrack(id: Int): WorkoutTracksRoomEntity


    @Query("SELECT * FROM WorkoutRoomEntity WHERE id = :id")
    suspend fun getWorkout(id: Int): WorkoutRoomEntity


    //UPDATE


    @Query("UPDATE WorkoutRoomEntity SET duration = :duration WHERE id = :id")
    suspend fun updateWorkoutDuration(id: Int, duration: Int)

    @Query("UPDATE WorkoutRoomEntity SET name = :name WHERE id = :id")
    suspend fun updateWorkoutName(name: String, id: Int)

    @Query("UPDATE WorkoutRoomEntity SET imageUrl = :imageUrl WHERE id = :id")
    suspend fun updateWorkoutImageUrl(imageUrl: String, id: Int)

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

    @Query("DELETE FROM WorkoutRoomEntity WHERE id = :id")
    suspend fun deleteWorkout(id: Int)

    @Query("DELETE FROM TokenRoomEntity")
    suspend fun clearToken()

    @Query("DELETE FROM WorkoutRoomEntity")
    suspend fun clearWorkouts()

    @Query("DELETE FROM WorkoutTracksRoomEntity")
    suspend fun clearWorkoutsTracks()

}