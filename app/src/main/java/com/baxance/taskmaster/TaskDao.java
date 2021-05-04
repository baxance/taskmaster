package com.baxance.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insert(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> getAllTasks();

    @Query("SELECT * FROM Task WHERE title = :taskTitle")
    public List<Task> getTask(String taskTitle);
}
