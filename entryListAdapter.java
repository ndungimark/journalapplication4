package com.example.mark.journalapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.mark.journalapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class entryListAdapter extends ArrayAdapter<listAccessors> {

    Context mCtx;
    int listLayoutRes;
    public List<listAccessors> entryList;
    SQLiteDatabase mDatabase;

    public entryListAdapter(Context mCtx, int listLayoutRes, List<listAccessors> entryList, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, entryList);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.entryList = entryList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listLayoutRes, null);

        //getting employee of the specified position
        final listAccessors entry = entryList.get(position);


        //getting views
        TextView textViewEntry = view.findViewById(R.id.textViewEntry);
        TextView textViewDate = view.findViewById(R.id.textViewDate);

        //adding data to views
        textViewEntry.setText(entry.getThought());
        textViewDate.setText(entry.getDay());

        //we will use these buttons later for update and delete operation
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        Button buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEntry(entry);
            }
        });

        //the delete operation
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM journalEntry WHERE id = ?";
                        mDatabase.execSQL(sql, new Integer[]{entry.getId()});
                        reloadEntryFromDatabase();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    //update entry button
    private void updateEntry(final listAccessors entry) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_update, null);
        builder.setView(view);


        final EditText editTexThought = view.findViewById(R.id.updateThought);
        final DatePicker editTextDate = view.findViewById(R.id.DatePicker);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String journalDate = sdf.format(cal.getTime());
        editTexThought.setText(entry.getThought());
        //editTextDate.setMaxDate(cal.geTime());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.buttonUpdateEntry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String thoughts = editTexThought.getText().toString().trim();
                int day = editTextDate.getDayOfMonth();

                if (thoughts.isEmpty()) {
                    editTexThought.setError("Name can't be blank");
                    editTexThought.requestFocus();
                    return;
                }

                String sql = "UPDATE journalEntry \n" +
                        "SET thoughts = ?, \n" +
                        "day = ?\n" +
                        "WHERE id = ?;\n";

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String journalDate = sdf.format(cal.getTime());


                mDatabase.execSQL(sql, new String[]{thoughts, journalDate, String.valueOf(entry.getId())});
                Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show();
                reloadEntryFromDatabase();

                dialog.dismiss();
            }
        });
    }
    //reload entry on update
    private void reloadEntryFromDatabase() {
        Cursor cursorEntry = mDatabase.rawQuery("SELECT * FROM journalEntry", null);
        if (cursorEntry.moveToFirst()) {
            entryList.clear();
            do {
                entryList.add(new listAccessors(
                        cursorEntry.getInt(0),
                        cursorEntry.getString(1),
                        cursorEntry.getString(2)
                ));
            } while (cursorEntry.moveToNext());
        }
        cursorEntry.close();
        notifyDataSetChanged();
    }
}
