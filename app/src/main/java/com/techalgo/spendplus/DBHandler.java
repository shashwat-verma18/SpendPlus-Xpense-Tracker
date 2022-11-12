package com.techalgo.spendplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "spendplus";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_TRXN = "trxn";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String AMT_COL = "amt";

    // below variable id for our course duration column.
    private static final String DATE_COL = "date";

    // below variable for our course description column.
    private static final String TYPE_COL = "type";

    // below variable is for our course tracks column.
    private static final String NOTE_COL = "note";

    // below variable is for our course tracks column.
    private static final String TTYPE_COL = "ttype";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String create_trxn = "CREATE TABLE " + TABLE_TRXN + " ("
                + ID_COL + " INTEGER PRIMARY KEY, "
                + AMT_COL + " DOUBLE,"
                + TYPE_COL + " TEXT,"
                + DATE_COL + " DATE,"
                + NOTE_COL + " TEXT,"
                + TTYPE_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(create_trxn);
    }

    // this method is use to add new course to our sqlite database.
    public void addTrxn(double amt, String type, String date, String note, String ttype) {

        int id = getID();
        id++;
        Log.d("id",String.valueOf(id));

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL,id);
        values.put(AMT_COL, amt);
        values.put(TYPE_COL, type);
        values.put(DATE_COL, date);
        values.put(NOTE_COL, note);
        values.put(TTYPE_COL, ttype);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_TRXN, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public int getID(){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_TRXN+" ORDER BY "+ID_COL, null);
        int res=-1;
        ArrayList<TransactionModal> trxnModalArrayList = new ArrayList<>();
        int i=0;
        if (cursorTransaction.moveToFirst()) {
            do {
                trxnModalArrayList.add(new TransactionModal(cursorTransaction.getInt(0),
                        cursorTransaction.getDouble(1),
                        cursorTransaction.getString(2),
                        cursorTransaction.getString(3),
                        cursorTransaction.getString(4),
                        cursorTransaction.getString(5)));
                res = trxnModalArrayList.get(i).getId();
                i++;
            } while (cursorTransaction.moveToNext());
        }
        cursorTransaction.close();
        return res;
    }



    public ArrayList<TransactionModal> readTransactions() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_TRXN+" ORDER BY "+DATE_COL+" DESC", null);

        // on below line we are creating a new array list.
        ArrayList<TransactionModal> trxnModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransaction.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                trxnModalArrayList.add(new TransactionModal(cursorTransaction.getInt(0),
                        cursorTransaction.getDouble(1),
                        cursorTransaction.getString(2),
                        cursorTransaction.getString(3),
                        cursorTransaction.getString(4),
                        cursorTransaction.getString(5)));
            } while (cursorTransaction.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransaction.close();
        return trxnModalArrayList;
    }

    public ArrayList<TransactionModal> readRecentTransactions() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_TRXN+" ORDER BY "+DATE_COL+" DESC LIMIT 10", null);

        // on below line we are creating a new array list.
        ArrayList<TransactionModal> trxnModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransaction.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                trxnModalArrayList.add(new TransactionModal(cursorTransaction.getInt(0),
                        cursorTransaction.getDouble(1),
                        cursorTransaction.getString(2),
                        cursorTransaction.getString(3),
                        cursorTransaction.getString(4),
                        cursorTransaction.getString(5)));
            } while (cursorTransaction.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransaction.close();
        return trxnModalArrayList;
    }

    public ArrayList<TransactionModal> readIncomeTransactions(String start, String end) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_TRXN+" WHERE "+TTYPE_COL+" = "+"\'INCOME\' AND "+DATE_COL+" BETWEEN \'"+start+"\' AND \'"+end+"\'"+" ORDER BY "+DATE_COL, null);

        // on below line we are creating a new array list.
        ArrayList<TransactionModal> trxnModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransaction.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                trxnModalArrayList.add(new TransactionModal(cursorTransaction.getInt(0),
                        cursorTransaction.getDouble(1),
                        cursorTransaction.getString(2),
                        cursorTransaction.getString(3),
                        cursorTransaction.getString(4),
                        cursorTransaction.getString(5)));
            } while (cursorTransaction.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransaction.close();
        return trxnModalArrayList;
    }

    public ArrayList<TransactionModal> readExpenseTransactions(String start, String end) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorTransaction = db.rawQuery("SELECT * FROM " + TABLE_TRXN+" WHERE "+TTYPE_COL+" = "+"\'EXPENSE\' AND "+DATE_COL+" BETWEEN \'"+start+"\' AND \'"+end+"\'"+" ORDER BY "+DATE_COL, null);

        // on below line we are creating a new array list.
        ArrayList<TransactionModal> trxnModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransaction.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                trxnModalArrayList.add(new TransactionModal(cursorTransaction.getInt(0),
                        cursorTransaction.getDouble(1),
                        cursorTransaction.getString(2),
                        cursorTransaction.getString(3),
                        cursorTransaction.getString(4),
                        cursorTransaction.getString(5)));
            } while (cursorTransaction.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransaction.close();
        return trxnModalArrayList;
    }

    public void deleteRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_TRXN, "id=?", new String[]{String.valueOf(id)});
        db.close();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRXN);
        onCreate(db);
    }


}
