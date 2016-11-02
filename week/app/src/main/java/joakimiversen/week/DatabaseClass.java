package joakimiversen.week;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseClass {

    public static final String TABLE_TASK = "task";
    public static final String TASK_ID = "_id";
    public static final String TASK_TASK = "_task";
    public static final String TASK_STATE = "_state";
    public static final String TASK_PROGRESS = "_progress";
    public static final String TASK_PRIORITY = "_priority";
    public static final String TASK_NOTE = "_note";

    public SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allTaskColumns = {
            TASK_ID,
            TASK_TASK,
            TASK_STATE,
            TASK_PROGRESS,
            TASK_PRIORITY,
            TASK_NOTE
    };

    public boolean sameExists = false;

    public DatabaseClass(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Task createTask(Task task) {
        sameExists = false;
        ContentValues values = new ContentValues();
        values.put(TASK_TASK, task.task);
        values.put(TASK_NOTE, task.note);
        values.put(TASK_STATE, task.state);

        Task newlyInsertedTask = new Task();

        Cursor cursor = database.query(TABLE_TASK, allTaskColumns, TASK_TASK + " = " + "\"" + task.task + "\"", null, null, null, null);
        if (!cursor.moveToFirst()) {
            long insertId = database.insert(TABLE_TASK, TASK_NOTE, values);
            cursor = database.query(TABLE_TASK, allTaskColumns, TASK_ID + " = " + insertId, null, null, null, null);
            cursor.moveToFirst();
            newlyInsertedTask = cursorToTask(cursor);
        } else {
            sameExists = true;
        }

        cursor.close();
        return newlyInsertedTask;
    }

    public void updatePriority(int id, int priority) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseClass.TASK_PRIORITY, priority);
        database.update(DatabaseClass.TABLE_TASK, cv, DatabaseClass.TASK_ID + " = " + id, null);
    }

    public void deleteTask(Task task) {
        long id = task.id;
        database.delete(TABLE_TASK, TASK_ID + " = " + id, null);
    }

    public void deleteTaskById(int _id) {
        //long id = _id;
        database.delete(TABLE_TASK, TASK_ID + " = " + _id, null);
    }

    public void updateTask(int id, String task, String note) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseClass.TASK_TASK, task);
        cv.put(DatabaseClass.TASK_NOTE, note);
        database.update(DatabaseClass.TABLE_TASK, cv, DatabaseClass.TASK_ID + " = " + id, null);
    }

    public void updateTask(int id, String task, String note, String progress) {
        ContentValues cv = new ContentValues();
        cv.put(TASK_TASK, task);
        cv.put(DatabaseClass.TASK_NOTE, note);
        cv.put(DatabaseClass.TASK_PROGRESS, progress);
        database.update(DatabaseClass.TABLE_TASK, cv, DatabaseClass.TASK_ID + " = " + id, null);
    }

    public void changeTaskState(Task task, String newState) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_STATE, newState);

        database.update(TABLE_TASK, contentValues, TASK_ID + " = " + task.id, null);
    }

    public void changeTaskState(int id, String newState) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_STATE, newState);

        database.update(TABLE_TASK, contentValues, TASK_ID + " = " + id, null);
    }

    public void getAllTasks() {
        Cursor cursor = database.query(TABLE_TASK, allTaskColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            System.out.println("TASK: " + task.id + " " + task.task + " " + task.state);
            cursor.moveToNext();
        }
    }

    public ArrayList<Task> getAllTasksForState(String state) {
        ArrayList<Task> tasks = new ArrayList<>();

        Cursor cursor = database.query(TABLE_TASK, allTaskColumns, TASK_STATE + " = " + "\"" + state + "\"", null, null, null, TASK_PRIORITY + " ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = cursorToTask(cursor);
            tasks.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return tasks;
    }

    private Task cursorToTask(Cursor cursor) {
        Task task = new Task();
        task.id = cursor.getInt(0);
        task.task = cursor.getString(1);
        task.state = cursor.getString(2);
        task.progress = cursor.getString(3);
        task.priority = cursor.getInt(4);
        task.note = cursor.getString(5);
        return task;
    }
}
