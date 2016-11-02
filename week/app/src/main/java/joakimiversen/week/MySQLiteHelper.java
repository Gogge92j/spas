package joakimiversen.week;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASK = "task";
    public static final String TASK_ID = "_id";
    public static final String TASK_TASK = "_task";
    public static final String TASK_STATE = "_state";
    public static final String TASK_PROGRESS = "_progress";
    public static final String TASK_PRIORITY = "_priority";
    public static final String TASK_NOTE = "_note";

    private static final String DATABASE_NAME = "activities.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TASK_CREATE = "CREATE TABLE "
            + TABLE_TASK + "("
            + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK_TASK + " TEXT NOT NULL, "
            + TASK_STATE + " TEXT NOT NULL, "
            + TASK_PROGRESS + " TEXT, "
            + TASK_PRIORITY + " INTEGER DEFAULT 100, "
            + TASK_NOTE + " TEXT);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TASK_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
