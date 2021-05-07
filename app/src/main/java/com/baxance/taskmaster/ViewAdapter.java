package com.baxance.taskmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskTwo;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.TaskViewHolder> {
    public ArrayList<TaskTwo> taskList;
    public TaskListener taskListener;

    public ViewAdapter(ArrayList<TaskTwo> taskList, TaskListener taskListener) {
        this.taskList = taskList;
        this.taskListener = taskListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        ViewAdapter.TaskViewHolder taskViewHolder = new ViewAdapter.TaskViewHolder(view);

        view.setOnClickListener(newView -> {
            taskListener.tListener(taskViewHolder.task);
        });

//        TaskViewHolder taskViewHolder = new TaskViewHolder(view);

        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = taskList.get(position);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences();
        String username = preferences.getString("username", "Guest");
        String userTeam = preferences.getString("userTeam", "");

        TextView titleText = holder.itemView.findViewById(R.id.taskTitle);
        titleText.setText(holder.task.getTitle());
        TextView teamText = holder.itemView.findViewById(R.id.taskTeam);
        teamText.setText(holder.task.getTeam().getName());
    }

    @Override
    public int getItemCount() {
        if (taskList == null){
            return 0;
        }
        return taskList.size();
    }

    public interface TaskListener {
        void tListener(TaskTwo task);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TaskTwo task;
        public View itemView;

        public TaskViewHolder(@NonNull View taskView){
            super(taskView);
            this.itemView = taskView;
        }
    }
}