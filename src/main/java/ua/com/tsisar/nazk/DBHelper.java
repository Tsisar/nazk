package ua.com.tsisar.nazk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ua.com.tsisar.nazk.dto.Item;
import ua.com.tsisar.nazk.dto.ItemData;
import ua.com.tsisar.nazk.dto.Step1;
import ua.com.tsisar.nazk.dto.Step1Data;

public class DBHelper extends SQLiteOpenHelper {
    private final static String TAG = "MyLog";

    private static final String NAME = "favorites_db";
    private static final String TABLE = "favorites";
    private static final String LIMIT = "100";

    private static final String DOCUMENT_ID = "document_id";
    private static final String FIRST_NAME = "first_name";
    private static final String MIDDLE_NAME = "middle_name";
    private static final String LAST_NAME = "last_name";
    private static final String DOCUMENT_TYPE = "document_type";
    private static final String DECLARATION_TYPE = "declaration_type";
    private static final String DECLARATION_YEAR = "declaration_year";
    private static final String WORK_PLACE = "Work_place";
    private static final String WORK_POST = "work_post";
    private static final String USER_DECLARANT_ID = "user_declarant_id";
    private static final String DATE = "date";

    public DBHelper(Context context) {
        super(context, NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " ("
                + "id integer primary key autoincrement,"
                + DOCUMENT_ID + " text,"
                + FIRST_NAME + " text,"
                + MIDDLE_NAME + " text,"
                + LAST_NAME + " text,"
                + DOCUMENT_TYPE + " Integer,"
                + DECLARATION_TYPE + " Integer,"
                + DECLARATION_YEAR + " Integer,"
                + WORK_PLACE + " text,"
                + WORK_POST + " text,"
                + USER_DECLARANT_ID + " Integer,"
                + DATE + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<Item> getFavoritesList(){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<Item> list = new ArrayList<>();

        // делаем запрос всех данных из таблицы TABLE, получаем Cursor
        Cursor cursor = database.query(TABLE, null, null, null,
                null, null, LAST_NAME + ", " + FIRST_NAME + ", " + MIDDLE_NAME, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (cursor.moveToFirst()) {
            do {
                list.add(new Item(
                        new ItemData(new Step1(
                                new Step1Data(
                                        cursor.getString(cursor.getColumnIndex(LAST_NAME)),
                                        cursor.getString(cursor.getColumnIndex(FIRST_NAME)),
                                        cursor.getString(cursor.getColumnIndex(MIDDLE_NAME)),
                                        cursor.getString(cursor.getColumnIndex(WORK_POST)),
                                        cursor.getString(cursor.getColumnIndex(WORK_PLACE))
                                )
                        )),
                        cursor.getInt(cursor.getColumnIndex(DOCUMENT_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(DECLARATION_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(DECLARATION_YEAR)),
                        cursor.getString(cursor.getColumnIndex(DOCUMENT_ID)),
                        cursor.getInt(cursor.getColumnIndex(USER_DECLARANT_ID)),
                        cursor.getString(cursor.getColumnIndex(DATE))));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "0 rows");
        }
        cursor.close();
        Log.d(TAG, "list: " + list);
        return  list;
    }

    public boolean isSaved(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<String> list = new ArrayList<>();

        Cursor cursor = database.query(TABLE, null, DOCUMENT_ID + " = \'" + id + "\'",
                null, null, null, null, LIMIT);

        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(DOCUMENT_ID)));
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "0 rows");
        }
        cursor.close();

        return !list.isEmpty();
    }

    public void delete(String id){
        this.getWritableDatabase().execSQL("delete from " + TABLE + " where " + DOCUMENT_ID + " = \'" + id + "\';");
    }

    public void save(Item item){
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = this.getWritableDatabase();

        cv.put(DOCUMENT_ID, item.getId());
        cv.put(FIRST_NAME, item.getFirstName());
        cv.put(MIDDLE_NAME, item.getMiddleName());
        cv.put(LAST_NAME, item.getLastName());
        cv.put(DOCUMENT_TYPE, item.getDocumentType());
        cv.put(DECLARATION_TYPE, item.getDeclarationType());
        cv.put(DECLARATION_YEAR, item.getDeclarationYear());
        cv.put(WORK_PLACE, item.getWorkPlace());
        cv.put(WORK_POST, item.getWorkPost());
        cv.put(USER_DECLARANT_ID, item.getUserDeclarantId());
        cv.put(DATE, item.getDate());

        long rowID = db.insert(TABLE, null, cv);

        Log.d(TAG, "row inserted, ID = " + rowID);
    }

}
