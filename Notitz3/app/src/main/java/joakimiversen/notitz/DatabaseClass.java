package joakimiversen.notitz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseClass {

    public static final String TABLE_NOTITZ = "Notitz";
    public static final String NOTITZ_ID = "_id";
    public static final String NOTITZ_ACTIVATED = "_activated";
    public static final String NOTITZ_TITLE = "_title";
    public static final String NOTITZ_TEXT = "_text";
    public static final String NOTITZ_HOUR = "_hour";
    public static final String NOTITZ_MINUTE = "_minute";
    public static final String NOTITZ_REPEAT = "_repeat";
    public static final String NOTITZ_MONDAY = "_monday";
    public static final String NOTITZ_TUESDAY = "_tuesday";
    public static final String NOTITZ_WEDNESDAY = "_wednesday";
    public static final String NOTITZ_THURSDAY = "_thursday";
    public static final String NOTITZ_FRIDAY = "_friday";
    public static final String NOTITZ_SATURDAY = "_saturday";
    public static final String NOTITZ_SUNDAY = "_sunday";

    public SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allNotitzColumns = {
            NOTITZ_ID,
            NOTITZ_ACTIVATED,
            NOTITZ_TITLE,
            NOTITZ_TEXT,
            NOTITZ_HOUR,
            NOTITZ_MINUTE,
            NOTITZ_REPEAT,
            NOTITZ_MONDAY,
            NOTITZ_TUESDAY,
            NOTITZ_WEDNESDAY,
            NOTITZ_THURSDAY,
            NOTITZ_FRIDAY,
            NOTITZ_SATURDAY,
            NOTITZ_SUNDAY
    };
    public boolean sameExists;

    public DatabaseClass(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ArrayList<Notitz> getAllNotitz() {
        ArrayList<Notitz> notitzs = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NOTITZ, allNotitzColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Notitz notitz = cursorToNotitz(cursor);
            notitzs.add(notitz);
            cursor.moveToNext();
        }

        cursor.close();
        return notitzs;
    }

    public Notitz createNotitz(Notitz notitz) {
        sameExists = false;
        ContentValues values = new ContentValues();
        values.put(NOTITZ_TITLE, notitz.title);
        values.put(NOTITZ_TEXT, notitz.text);
        values.put(NOTITZ_HOUR, notitz.hour);
        values.put(NOTITZ_MINUTE, notitz.minute);

        Notitz newNotitz = new Notitz();

        Cursor cursor = database.query(TABLE_NOTITZ, allNotitzColumns, NOTITZ_TITLE + " = " + "\"" + notitz.title + "\"", null, null, null, null);
        if (!cursor.moveToFirst()) {
            long insertId = database.insert(TABLE_NOTITZ, NOTITZ_TITLE, values);
            cursor = database.query(TABLE_NOTITZ, allNotitzColumns, NOTITZ_ID + " = " + insertId, null, null, null, null);
            cursor.moveToFirst();
            newNotitz = cursorToNotitz(cursor);
        } else {
            sameExists = true;
        }

        cursor.close();
        return newNotitz;
    }

    public void deleteNotitz(int id) {
        database.delete(TABLE_NOTITZ, NOTITZ_ID + " = " + id, null);
    }

    public void updateNotitzValue(String value, int id, int i) {
        ContentValues cv = new ContentValues();
        cv.put(value, i);
        database.update(TABLE_NOTITZ, cv, NOTITZ_ID + " = " + id, null);
    }

    public void updateNotitzValue(String value, int id, boolean b) {
        ContentValues cv = new ContentValues();
        cv.put(value, booleanToInt(b));
        database.update(TABLE_NOTITZ, cv, NOTITZ_ID + " = " + id, null);
    }

    public void updateNotitzValue(String value, int id, String s) {
        ContentValues cv = new ContentValues();
        cv.put(value, s);
        database.update(TABLE_NOTITZ, cv, NOTITZ_ID + " = " + id, null);
    }

    public void updateTime(int id, int hour, int minute) {
        ContentValues cv = new ContentValues();
        cv.put(NOTITZ_HOUR, hour);
        cv.put(NOTITZ_MINUTE, minute);
        database.update(TABLE_NOTITZ, cv, NOTITZ_ID + " = " + id, null);
    }

    public void updateTitleAndText(int id, String title, String text) {
        ContentValues cv = new ContentValues();
        cv.put(NOTITZ_TITLE, title);
        cv.put(NOTITZ_TEXT, text);
        database.update(TABLE_NOTITZ, cv, NOTITZ_ID + " = " + id, null);
    }


    private boolean intToBoolean(int i) {
        return (i == 1)? true : false;
    }

    private int booleanToInt(boolean b) {
        return (b == true)? 1 : 0;
    }

    private Notitz cursorToNotitz(Cursor cursor) {
        Notitz notitz = new Notitz();
        notitz.id = cursor.getInt(0);
        notitz.activated = intToBoolean(cursor.getInt(1));
        notitz.title = cursor.getString(2);
        notitz.text = cursor.getString(3);
        notitz.hour = cursor.getInt(4);
        notitz.minute = cursor.getInt(5);
        notitz.repeat = intToBoolean(cursor.getInt(6));
        notitz.monday = intToBoolean(cursor.getInt(7));
        notitz.tuesday = intToBoolean(cursor.getInt(8));
        notitz.wednesday = intToBoolean(cursor.getInt(9));
        notitz.thursday = intToBoolean(cursor.getInt(10));
        notitz.friday = intToBoolean(cursor.getInt(11));
        notitz.saturday = intToBoolean(cursor.getInt(12));
        notitz.sunday = intToBoolean(cursor.getInt(13));
        return notitz;
    }
}
