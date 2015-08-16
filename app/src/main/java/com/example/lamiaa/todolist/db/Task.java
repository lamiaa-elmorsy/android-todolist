package com.example.lamiaa.todolist.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Lamiaa on 8/14/2015.
 */
@Table(name = "tasks")
public class Task extends Model {
    @Column(name = "title")
    private String title;
    @Column(name = "isCompleted")
    private int isCompleted; //0 = false, 1= true

    public Task() {

    }

    public Task(String title) {
        this.title = title;
        this.isCompleted = 0;
    }

    // select * from task
    // static, so that I can use it to get all tasks
    public static List<Task> all() {
        return new Select().from(Task.class).execute();
    }

    public static List<Task> allIncomplete() {
        return new Select().from(Task.class).where("isCompleted = ?", 0).execute();
    }

    public static List<Task> allCompleted() {
        return new Select().from(Task.class).where("isCompleted = ?", 1).execute();
    }

    public static void delete(String title) {
        new Delete().from(Task.class).where("title = ?", title).execute();
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    // Setters
    public void setCompleted() {
        this.isCompleted = 1;
        this.save();
    }

    public void setTitle(String title) {
        this.title = title;
        this.save();
    }
}
