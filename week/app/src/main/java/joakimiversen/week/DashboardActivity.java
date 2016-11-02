package joakimiversen.week;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Console;
import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddNewTask.AddNewListener, EditTask.EditListener, StateSelector.StateListener, DeleteTask.DeleteTaskListener {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;

    private DatabaseClass databaseClass;

    private ArrayList<Task> tasks = new ArrayList<>();

    public String currentState;
    // Possible states
    private static final String IN_PROGRESS = "In progress";
    private static final String DONE = "Done";
    private static final String PENDING = "Pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseClass = new DatabaseClass(this);
        try {
            databaseClass.open();
        } catch (SQLException e) {
            System.out.println("SQLException!");
        }

        currentState = IN_PROGRESS;
        getSupportActionBar().setTitle("Tasks - " + currentState);
        tasks = databaseClass.getAllTasksForState(currentState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask newTask = new AddNewTask();
                newTask.show(getFragmentManager(), "activity");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.activityView);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TaskAdapter(this, tasks);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new MoveTouchHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    public void setTitleAndFetchTasks() {
        getSupportActionBar().setTitle("Tasks - " + currentState);
        tasks.clear();
        tasks.addAll(databaseClass.getAllTasksForState(currentState));
        //tasks = databaseClass.getAllTasksForState(currentState);
        mAdapter.notifyDataSetChanged();
        //mAdapter = new TaskAdapter(this, tasks);
        //mRecyclerView.setAdapter(mAdapter);
    }

    // After accepting new task, do the following:
    @Override
    public void onDialogPositiveClick(String _task, String _note) {
        Task task = new Task();
        task.task = _task;
        task.note = _note;
        task.state = currentState;
        databaseClass.createTask(task);

        if (databaseClass.sameExists) {
            Toast.makeText(DashboardActivity.this, "Equal task already exist", Toast.LENGTH_SHORT).show();
        }
        setTitleAndFetchTasks();
    }

    @Override
    public void onDialogStateClick(int _id, String _task, String _state) {
        databaseClass.changeTaskState(_id, _state);
        Toast.makeText(DashboardActivity.this, "\"" + _task + "\" changed state to " + "\"" + _state + "\"", Toast.LENGTH_SHORT).show();
        setTitleAndFetchTasks();
    }

    //@Override
    public void onDialogDeleteClick(int _id, String _task) {
        databaseClass.deleteTaskById(_id);
        Toast.makeText(DashboardActivity.this, "\"" + _task + "\" deleted", Toast.LENGTH_SHORT).show();
        setTitleAndFetchTasks();
    }

    @Override
    public void onDialogAcceptClick(int _id, String _task, String _note) {
        databaseClass.updateTask(_id, _task, _note);
        Toast.makeText(DashboardActivity.this, "\"" + _task + "\" updated", Toast.LENGTH_SHORT).show();
        setTitleAndFetchTasks();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void openHelpBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Keep record of current-, pending-, and accomplished tasks. Swiping left/right on either moves tasks to other states. " +
                "\n\n * Pending \t\t\t\t->\t\t In progress" +
                "\n * In progress \t->\t\t Done" +
                "\n * Done \t\t\t\t\t\t->\t\t delete the task" +
                "\n \n/ Joakim Iversen")
                .setCancelable(false)
                .setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Does nothing but removes the dialog
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void openAboutBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This is a simple application made for keeping an overveiw of the tasks i am doing. These consist of different subproject i like to do, such as creating this " +
                "application for remembering tasks and overview. \n \n/ Joakim Iversen")
                .setCancelable(false)
                .setPositiveButton("Cool!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Does nothing but removes the dialog
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        //Log.e("TIS", "id = " + id);

        if (id == R.id.nav_in_progress) {
            currentState = IN_PROGRESS;
        } else if (id == R.id.nav_pending) {
            currentState = PENDING;
        } else if (id == R.id.nav_done) {
            currentState = DONE;
        } else if (id == R.id.nav_help) {
            openHelpBox();
        } else if (id == R.id.nav_about) {
            openAboutBox();
        }

        setTitleAndFetchTasks();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
