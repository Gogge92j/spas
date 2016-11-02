package joakimiversen.notitz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

// todo on activated, do snack bar
//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        .setAction("Action", null).show();

public class MainActivity extends AppCompatActivity implements TimePickerDialogBox.TimeChangedListener,
        AddNewNotitz.AddNewNotitzListener, DeleteNotitzDialog.DeleteNotitzListener,
        NotitzAdapter.OnToggleStateListener, EditNotitz.EditListener {

    RecyclerView mRecycleView;
    NotitzAdapter notitzAdapter;
    ArrayList<Notitz> notitzCollection = new ArrayList<>();

    public static final String INTENT_EXTRA_ID = "INTENT_EXTRA_ID";
    public static final String INTENT_EXTRA_TITLE = "INTENT_EXTRA_TITLE";
    public static final String INTENT_EXTRA_TEXT = "INTENT_EXTRA_TEXT";
    public static final String NOTITZ = "NOTITZ";

    private DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseClass = new DatabaseClass(this);
        try {
            databaseClass.open();
        } catch (SQLException e) {
            System.out.println("SQLException");
        }

        notitzCollection = databaseClass.getAllNotitz();
        // Sorts in ascending order
        Collections.sort(notitzCollection, new Comparator<Notitz>() {
            @Override
            public int compare(Notitz n1, Notitz n2) {
                if (n1.hour == n2.hour) {
                    if (n1.minute < n2.minute) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (n1.hour < n2.hour) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        //mScheduleNotitzReciever = new ScheduleNotitzReciever();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewNotitz addNewNotitz = new AddNewNotitz();
                addNewNotitz.show(getSupportFragmentManager(), "AddNewNotitz");
            }
        });

        mRecycleView = (RecyclerView) findViewById(R.id.notitz_view);
        mRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);

        notitzAdapter = new NotitzAdapter(this, notitzCollection);
        mRecycleView.setAdapter(notitzAdapter);
    }

    public int compareTo(Notitz n1, Notitz n2) {
        if (n1.hour == n2.hour) {
            if (n1.minute < n2.minute) {
                return 1;
            } else {
                return 0;
            }
        } else if (n1.hour < n2.hour) {
            return 1;
        } else {
            return 0;
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*public class ScheduleNotitzReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nId = intent.getIntExtra(MainActivity.INTENT_EXTRA_ID, -1);
            String nTitle = intent.getStringExtra(MainActivity.INTENT_EXTRA_TITLE);
            String nText = intent.getStringExtra(MainActivity.INTENT_EXTRA_TEXT);
            int nHour = intent.getIntExtra(MainActivity.INTENT_EXTRA_HOUR, 0);
            int nMinute = intent.getIntExtra(MainActivity.INTENT_EXTRA_MINUTE, 0);

            scheduleAlarm(findViewById(R.id.layout_main), nId, nTitle, nText, nHour, nMinute);
        }
    }

    public void scheduleAlarm(View v, int id, String title, String text, int hour, int minute) {
        // 20 sec after initiation
        // todo calculate correct time to the alarm
        // todo setup for consecutive days
        Long time = new GregorianCalendar().getTimeInMillis()+10*1000;

        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        intentAlarm.putExtra(INTENT_EXTRA_ID, id);
        intentAlarm.putExtra(INTENT_EXTRA_TITLE, title);
        intentAlarm.putExtra(INTENT_EXTRA_TEXT, text);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        Snackbar.make(v, "Notitz scheduled for " + hour + ":" + minute, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }*/



    @Override
    public void onTimeChanged(int _id, int hour, int minute) {
        databaseClass.updateTime(_id, hour, minute);
        Toast.makeText(MainActivity.this, "Notitz time updated", Toast.LENGTH_SHORT).show();

        int position = notitzAdapter.getNotitzPositionById(_id);
        if (position != 999) {
            notitzAdapter.mNotitzCollection.get(position).hour = hour;
            notitzAdapter.mNotitzCollection.get(position).minute = minute;
        } else {
            Toast.makeText(MainActivity.this, "Cannot find the Notitz", Toast.LENGTH_SHORT).show();
        }
        notitzAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAcceptNewNotitz(String _title, String _text) {
        Notitz notitz = new Notitz();
        notitz.title = _title;
        notitz.text = _text;
        final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
        notitz.hour = c.get(Calendar.HOUR_OF_DAY);
        notitz.minute = c.get(Calendar.MINUTE);

        Notitz newNotitz = databaseClass.createNotitz(notitz);

        if (databaseClass.sameExists) {
            Toast.makeText(MainActivity.this, "Equal Notitz already exists", Toast.LENGTH_SHORT).show();
        } else {
            notitzAdapter.mNotitzCollection.add(newNotitz);
            notitzAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDialogDeleteClick(int id, String title) {
        databaseClass.deleteNotitz(id);
        Toast.makeText(MainActivity.this, "\"" + title + "\" deleted", Toast.LENGTH_SHORT).show();

        int position = notitzAdapter.getNotitzPositionById(id);
        notitzAdapter.mNotitzCollection.remove(position);
        notitzAdapter.notifyDataSetChanged();
    }

    @Override
    public void onToggleStateChanged(int _id, String property, boolean newState) {
        databaseClass.updateNotitzValue(property, _id, newState);
        notitzAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogEditClick(int _id, String _title, String _text) {
        databaseClass.updateTitleAndText(_id, _title, _text);
        int position = notitzAdapter.getNotitzPositionById(_id);
        notitzAdapter.mNotitzCollection.get(position).title = _title;
        notitzAdapter.mNotitzCollection.get(position).text = _text;
        notitzAdapter.notifyDataSetChanged();
    }
}
