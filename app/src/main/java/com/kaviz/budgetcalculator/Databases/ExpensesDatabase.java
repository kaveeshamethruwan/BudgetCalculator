package com.kaviz.budgetcalculator.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.kaviz.budgetcalculator.Models.BudgetProfile;
import com.kaviz.budgetcalculator.Models.Expenses;

import java.util.ArrayList;
import java.util.List;

public class ExpensesDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "ExpensesDatabase";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_NAME = "ExpensesTable";

    //columns
    private static final String ID = "id";
    private static final String PROFILE_ID = "profileID";
    private static final String DESCRIPTION = "description";
    private static final String AMOUNT = "amount";
    private static final String OTHER = "other";
    private static final String WITHDRAW = "withdraw";

    public ExpensesDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String TABLE_CREATE_QUERY = "CREATE TABLE "+DB_TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PROFILE_ID+" TEXT, "+DESCRIPTION+" TEXT, "+AMOUNT+" TEXT, "+OTHER+" TEXT, "+WITHDRAW+" TEXT);";
        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        final String TABLE_DROP_QUERY = "DROP TABLE IF EXISTS "+DB_TABLE_NAME;
        sqLiteDatabase.execSQL(TABLE_DROP_QUERY);
        onCreate(sqLiteDatabase);

    }

    public long addExpenses(Expenses expenses) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //for structure data
        contentValues.put(PROFILE_ID, expenses.getProfileID());
        contentValues.put(DESCRIPTION, expenses.getDescription());
        contentValues.put(AMOUNT, expenses.getAmount());
        contentValues.put(OTHER, expenses.getOther());
        contentValues.put(WITHDRAW, expenses.getWithdraw());

        long status = database.insert(DB_TABLE_NAME, null, contentValues);

        //closing database connection
        database.close();

        //returning current status
        return status;

    }

    public List<Expenses> getAllExpensesByProfileID(int profileID) {

        List<Expenses> expensesList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE_NAME, new String[] {ID, PROFILE_ID, DESCRIPTION, AMOUNT, OTHER, WITHDRAW},
                PROFILE_ID + "=?", new String[] {String.valueOf(profileID)}, null, null, null, null);

        if (cursor.moveToFirst()) {

            do {

                Expenses expenses = new Expenses();
                expenses.setId(Integer.parseInt(cursor.getString(0)));
                expenses.setProfileID(cursor.getString(1));
                expenses.setDescription(cursor.getString(2));
                expenses.setAmount(cursor.getString(3));
                expenses.setOther(cursor.getString(4));
                expenses.setWithdraw(cursor.getString(5));

                expensesList.add(expenses);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expensesList;
    }

    public int getLastRowId(int profileId) {
        int id = -1;
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = { ID };
        String selection = PROFILE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(profileId) };
        String orderBy = ID + " DESC";
        String limit = "1";

        Cursor cursor = db.query(DB_TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy, limit);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(ID);
            if (idIndex >= 0) {
                id = cursor.getInt(idIndex);
            }
            cursor.close();
        }

        return id;
    }

    public int updateExpenseToUser(int id, String otherValue) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(OTHER, otherValue);
        int numRowsUpdated = db.update(DB_TABLE_NAME, values, ID + "=?", new String[] {String.valueOf(id)});
        db.close();

        return numRowsUpdated;

    }

    public int updateExpenseToWithdraw(int id, String withdrawValue) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WITHDRAW, withdrawValue);
        int numRowsUpdated = db.update(DB_TABLE_NAME, values, ID + "=?", new String[] {String.valueOf(id)});
        db.close();

        return numRowsUpdated;

    }

    // Define a method to get all rows with "User" in "other" column & comparing profile id
    public List<Expenses> getExpensesByUser(String profileId) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ID, PROFILE_ID, DESCRIPTION, AMOUNT, OTHER, WITHDRAW};
        String selection = OTHER + "=? AND " + PROFILE_ID + "=?";
        String[] selectionArgs = {"User", profileId};

        Cursor cursor = db.query(DB_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        List<Expenses> expensesList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            do {

                Expenses expenses = new Expenses();
                int idIndex = cursor.getColumnIndex(ID);
                int profileIdIndex = cursor.getColumnIndex(PROFILE_ID);
                int descriptionIndex = cursor.getColumnIndex(DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(AMOUNT);
                int otherIndex = cursor.getColumnIndex(OTHER);
                int withdrawIndex = cursor.getColumnIndex(WITHDRAW);

                expenses.setId(cursor.getInt(idIndex));
                expenses.setProfileID(cursor.getString(profileIdIndex));
                expenses.setDescription(cursor.getString(descriptionIndex));
                expenses.setAmount(cursor.getString(amountIndex));
                expenses.setOther(cursor.getString(otherIndex));
                expenses.setWithdraw(cursor.getString(withdrawIndex));
                expensesList.add(expenses);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {

            cursor.close();

        }

        db.close();
        return expensesList;
    }

    // Define a method to get all rows with "Withdraw" in "other" column & comparing profile id
    public List<Expenses> getExpensesByWithdraw(String profileId) {

        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {ID, PROFILE_ID, DESCRIPTION, AMOUNT, OTHER, WITHDRAW};
        String selection = WITHDRAW + "=? AND " + PROFILE_ID + "=?";
        String[] selectionArgs = {"Withdraw", profileId};

        Cursor cursor = db.query(DB_TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        List<Expenses> expensesList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

            do {

                Expenses expenses = new Expenses();
                int idIndex = cursor.getColumnIndex(ID);
                int profileIdIndex = cursor.getColumnIndex(PROFILE_ID);
                int descriptionIndex = cursor.getColumnIndex(DESCRIPTION);
                int amountIndex = cursor.getColumnIndex(AMOUNT);
                int otherIndex = cursor.getColumnIndex(OTHER);
                int withdrawIndex = cursor.getColumnIndex(WITHDRAW);

                expenses.setId(cursor.getInt(idIndex));
                expenses.setProfileID(cursor.getString(profileIdIndex));
                expenses.setDescription(cursor.getString(descriptionIndex));
                expenses.setAmount(cursor.getString(amountIndex));
                expenses.setOther(cursor.getString(otherIndex));
                expenses.setWithdraw(cursor.getString(withdrawIndex));
                expensesList.add(expenses);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {

            cursor.close();

        }

        db.close();
        return expensesList;
    }

}
