package joakimiversen.notitz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

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

    private static final String DATABASE_NAME = "notitz.db";
    private static final int DATABASE_VERSION = 1;

    private static final String NOTITZ_CREATE = "CREATE TABLE "
            + TABLE_NOTITZ + "("
            + NOTITZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NOTITZ_ACTIVATED + " INTEGER NOT NULL DEFAULT 1, "
            + NOTITZ_TITLE + " TEXT NOT NULL, "
            + NOTITZ_TEXT + " TEXT, "
            + NOTITZ_HOUR + " INTEGER, "
            + NOTITZ_MINUTE + " INTEGER, "
            + NOTITZ_REPEAT + " INTEGER DEFAULT 0, "
            + NOTITZ_MONDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_TUESDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_WEDNESDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_THURSDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_FRIDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_SATURDAY + " INTEGER DEFAULT 0, "
            + NOTITZ_SUNDAY + " INTEGER DEFAULT 0);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NOTITZ_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
