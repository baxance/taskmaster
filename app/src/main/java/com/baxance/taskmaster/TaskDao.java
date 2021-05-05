//package com.baxance.taskmaster;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import com.amplifyframework.datastore.generated.model.TaskTwo;
//
//import java.util.List;
//
//@Dao
//public interface TaskDao {
//    @Insert
//    public void insert(TaskTwo task);
//
//    @Query("SELECT * FROM Task")
//    public List<Task> getAllTasks();
//
//    @Query("SELECT * FROM Task WHERE title = :taskTitle")
//    public List<Task> getTask(String taskTitle);
//}
