package com.example.mark.journalapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListEntryActivity extends AppCompatActivity {
    List<listAccessors> entryList;
    SQLiteDatabase mDatabase;
    ListView listViewEntry;
    entryListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_entry);
        listViewEntry = (ListView) findViewById(R.id.listViewEntry);
        entryList = new ArrayList<>();
        //opening the database
        mDatabase = openOrCreateDatabase(MainActivity.DATABASE_NAME, MODE_PRIVATE, null);
        //we used rawQuery(sql, selectionargs) for fetching all the entries
        Cursor cursorEntries = mDatabase.rawQuery("SELECT * FROM journalEntry", null);

        //if the cursor has some data
        if (cursorEntries.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the entry list
                entryList.add(new listAccessors(
                        cursorEntries.getInt(0),
                        cursorEntries.getString(1),
                        cursorEntries.getString(2)
                ));
            } while (cursorEntries.moveToNext());

            //closing the cursor
            cursorEntries.close();
        }

        //creating the adapter object
        adapter = new entryListAdapter(this, R.layout.layout_list, entryList,mDatabase);

        //adding the adapter to listview
        listViewEntry.setAdapter(adapter);
    }
}
