package com.kaviz.budgetcalculator.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.kaviz.budgetcalculator.Models.BudgetProfile;

import java.util.ArrayList;
import java.util.List;

public class BudgetProfileDatabase extends SQLiteOpenHelper {

    //db info
    private static final String DB_NAME = "BudgetProfileDatabase";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE_NAME = "BudgetProfileTable";

    //columns
    private static final String ID = "id";
    private static final String PROFILE_TITLE = "profileTitle";
    private static final String BANK_SOURCE = "bankSource";
    private static final String DATE = "date";
    private static final String BUDGET = "budget";
    public BudgetProfileDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String TABLE_CREATE_QUERY = "CREATE TABLE "+DB_TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PROFILE_TITLE+" TEXT, "+BANK_SOURCE+" TEXT, "+DATE+" TEXT, "+BUDGET+" TEXT);";
        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        final String TABLE_DROP_QUERY = "DROP TABLE IF EXISTS "+DB_TABLE_NAME;
        sqLiteDatabase.execSQL(TABLE_DROP_QUERY);
        onCreate(sqLiteDatabase);

    }

    public long addBudgetProfile(BudgetProfile budgetProfile) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //for structure data
        contentValues.put(PROFILE_TITLE, budgetProfile.getProfileTitle());
        contentValues.put(BANK_SOURCE, budgetProfile.getBankSource());
        contentValues.put(DATE, budgetProfile.getDate());
        contentValues.put(BUDGET, budgetProfile.getBudget());

        long status = database.insert(DB_TABLE_NAME, null, contentValues);

        //closing database connection
        database.close();

        //returning current status
        return status;

    }

    public List<BudgetProfile>getAllBudgetProfiles(Context context) {

        List<BudgetProfile>budgetProfileList = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + DB_TABLE_NAME + "." + ID + " ASC";

        Cursor cursor = database.rawQuery(query, null);

        try {

            if (cursor.moveToFirst()) {

                do {

                    BudgetProfile budgetProfile = new BudgetProfile(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    );

                    budgetProfileList.add(budgetProfile);

                } while (cursor.moveToNext());

            }

        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        cursor.close();
        database.close();

        return budgetProfileList;

    }

    public int getLastID() {

        // Define the SELECT query
        String query = "SELECT " + ID + " FROM " + DB_TABLE_NAME + " ORDER BY " + ID + " ASC";

        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // Execute the SELECT query
        Cursor cursor = db.rawQuery(query, null);

        int lastId = -1;
        // Move the cursor to the last row
        int idColumnIndex = cursor.getColumnIndex(ID);
        if (cursor.moveToLast() && idColumnIndex >= 0) {
            // Get the ID column value of the last row
            lastId = cursor.getInt(idColumnIndex);
        }

        // Close the cursor and the database
        cursor.close();
        db.close();

        // The lastId variable now contains the ID value of the last row in ascending order
        return lastId;

    }

    public BudgetProfile budgetProfileByID(int id) {

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DB_TABLE_NAME, new String[]{ID, PROFILE_TITLE, BANK_SOURCE, DATE, BUDGET},
                ID+" = ?", new String[]{String.valueOf(id)}, null, null, null);

        BudgetProfile budgetProfile = null;

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            budgetProfile = new BudgetProfile(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            );

        }

        //closing database connection
        database.close();

        return budgetProfile;

    }

}
