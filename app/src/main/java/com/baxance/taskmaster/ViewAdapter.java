package com.baxance.taskmaster;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.TaskViewHolder> {
    public ArrayList<Task> taskList;
    public TaskListener taskListener;

    public ViewAdapter(ArrayList<Task> taskList, TaskListener taskListener) {
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

        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.TaskViewHolder holder, int position) {
        holder.task = taskList.get(position);

        TextView titleText = holder.itemView.findViewById(R.id.taskTitle);
        titleText.setText(holder.task.getTitle());
    }

    @Override
    public int getItemCount() {
        if (taskList == null){
            return 0;
        }
        return taskList.size();
    }

    public interface TaskListener {
        void tListener(Task task);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public Task task;
        public View itemView;

        public TaskViewHolder(@NonNull View taskView){
            super(taskView);
            this.itemView = taskView;
        }
    }
}