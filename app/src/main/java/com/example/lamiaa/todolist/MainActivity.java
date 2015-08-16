package com.example.lamiaa.todolist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lamiaa.todolist.db.Task;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

    private ArrayList<String> tasks;
    private ArrayList<Task> tasksFromDB;
    private ArrayAdapter<String> adapter;
    private EditText newTask;
    private Button addButton;
    private ListView taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization
        tasks = new ArrayList<String>();
        tasksFromDB = new ArrayList<Task>();

        addButton = (Button) findViewById(R.id.addButton);
        newTask = (EditText) findViewById(R.id.newTask);
        taskList = (ListView) findViewById(android.R.id.list);

        // view all tasks in the database
        for (Task t : Task.allIncomplete()) {
            tasksFromDB.add(t);
            tasks.add(t.getTitle());
            // Log.v("Main Activity",t.getTitle());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasks);
        setListAdapter(adapter);

        //add onClick listener to list
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String a = parent.getItemAtPosition(position).toString();

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Mark this task as completed ?");
                alertDialog.setMessage(a);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Mark as completed
                                Task toBeDeleted = tasksFromDB.get(position);
                                toBeDeleted.setCompleted();
                                //delete from the list
                                tasksFromDB.remove(position);
                                tasks.remove(position);
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

        // add onClick Listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTaskString = newTask.getText().toString();
                if (newTaskString.length() > 0) {
                    // add task to database
                    Task t = new Task(newTaskString);
                    t.save();
                    // add task to array list
                    tasks.add(newTaskString);
                    // add to the db list
                    tasksFromDB.add(t);
                    //notify the adapter
                    adapter.notifyDataSetChanged();
                    // clear the edit text
                    newTask.setText("");
                    hideKeyboard();
                } else {
                    // TODO: add dialog for empty task
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Please Enter your task");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_show_completed) {
            // start new activity
            Intent intent = new Intent(this, Completed.class);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement
        /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /* Hide Keyboard function
    reference: http://stackoverflow.com/a/7696791/3332953
     */
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
