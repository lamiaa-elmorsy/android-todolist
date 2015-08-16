package com.example.lamiaa.todolist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lamiaa.todolist.db.Task;

import java.util.ArrayList;

/**
 * Created by Lamiaa on 8/15/2015.
 */
public class Completed extends ListActivity {
    private ArrayList<String> completedTasks;
    private ArrayList<Task> tasksFromDB;
    private ArrayAdapter<String> adapter;
    private ListView completedTaskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_tasks);

        completedTasks = new ArrayList<String>();
        tasksFromDB = new ArrayList<Task>();
        completedTaskList = (ListView) findViewById(android.R.id.list);

        // view all tasks in the database
        for (Task t : Task.allCompleted()) {
            tasksFromDB.add(t);
            completedTasks.add(t.getTitle());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completedTasks);
        setListAdapter(adapter);

        //add onClick listener to list
        completedTaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String a = parent.getItemAtPosition(position).toString();

                AlertDialog alertDialog = new AlertDialog.Builder(Completed.this).create();
                alertDialog.setTitle("Delete this task ?");
                alertDialog.setMessage(a);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Task toBeDeleted = tasksFromDB.get(position);
                                toBeDeleted.delete();
                                //delete from the list
                                tasksFromDB.remove(position);
                                completedTasks.remove(position);
                                //notify the adapter
                                adapter.notifyDataSetChanged();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

    }
}
