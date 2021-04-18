package ua.tsisar.pavel.nazk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ua.tsisar.pavel.nazk.dto.Item;
import ua.tsisar.pavel.nazk.dto.ItemData;
import ua.tsisar.pavel.nazk.dto.Step1;
import ua.tsisar.pavel.nazk.dto.Step1Data;

public class DBHelper extends SQLiteOpenHelper {
    private static final String NAME = "nazk_client_db";

    private static final String FAVORITES = "favorites";
    private static final String FAVORITES_LIMIT = "100";

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

    private static final String HISTORY = "history";
    private static final String HISTORY_LIMIT = "10";

    private static final String QUERY = "history_query";

    public DBHelper(Context context) {
        super(context, NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FAVORITES + " ("
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

        db.execSQL("create table " + HISTORY + " ("
                + "_id integer primary key autoincrement,"
                + QUERY + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public ArrayList<Item> getFavoritesList(){
        ArrayList<Item> list = new ArrayList<>();

        // делаем запрос всех данных из таблицы TABLE, получаем Cursor
        Cursor cursor = this.getWritableDatabase().query(FAVORITES, null, null, null,
                null, null, LAST_NAME + ", " + FIRST_NAME + ", " + MIDDLE_NAME, FAVORITES_LIMIT);

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
        }
//        } else {
//            Log.d(TAG, "0 rows");
//        }
        cursor.close();
        return  list;
    }

    public boolean isSavedFavorites(String id){
        Cursor cursor = this.getReadableDatabase().query(
                FAVORITES, null, DOCUMENT_ID + " = \'" + id + "\'",
                null, null, null, null, null);
        boolean res = cursor.moveToFirst();
        cursor.close();
        return res;
    }

    public void deleteFavorites(String id){
        this.getWritableDatabase().execSQL("delete from " + FAVORITES + " where " + DOCUMENT_ID + " = \'" + id + "\';");
    }

    public void saveFavorites(Item item){
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

        db.insert(FAVORITES, null, cv);
//        long rowID = db.insert(FAVORITES, null, cv);
//        Log.d(TAG, "row inserted, ID = " + rowID);
    }

    public Cursor loadHistory(String query){
        return this.getReadableDatabase().query(
                HISTORY, null, QUERY + " like \'" + query + "%\'",
                null, null, null, "_id DESC", HISTORY_LIMIT);
    }

    public void saveHistory(String query){
       if(!isSavedHistory(query)) {
           ContentValues cv = new ContentValues();
           SQLiteDatabase db = this.getWritableDatabase();

           cv.put(QUERY, query);

           db.insert(HISTORY, null, cv);
       }
    }

    public void deleteHistory(String query){
        this.getWritableDatabase().execSQL("delete from " + HISTORY + " where " + QUERY + " = \'" + query + "\';");
    }

    public boolean isSavedHistory(String query){
        Cursor cursor = this.getReadableDatabase().query(
                HISTORY, null, QUERY + " = \'" + query + "\'",
                null, null, null, null, null);
        boolean res = cursor.moveToFirst();
        cursor.close();
        return res;
    }
}
