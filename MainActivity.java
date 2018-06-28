package com.example.mark.journalapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;








import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "myEntryDatabase";
    EditText EditThoughts;
    DatePicker pickDate;
    Button Save;
    TextView ViewEntries;
    SQLiteDatabase mDatabase;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditThoughts=(EditText)findViewById(R.id.thoughts);
        pickDate=(DatePicker)findViewById(R.id.simpleDatePicker);
        Save=(Button)findViewById(R.id.buttonAddEntry);
        ViewEntries=(TextView) findViewById(R.id.textViewEntry);
        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createEntry();
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thoughtsStr = EditThoughts.getText().toString().trim();
                int dept = pickDate.getDayOfMonth();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String journalDate = sdf.format(cal.getTime());

                //validating the inptus
                if (inputsAreCorrect(thoughtsStr)) {

                    String insertSQL = "INSERT INTO journalEntry \n" +
                            "(thoughts, day)\n" +
                            "VALUES \n" +
                            "(?, ?);";

                    //using the same method execsql for inserting values
                    mDatabase.execSQL(insertSQL, new String[]{thoughtsStr, journalDate});

                    Toast.makeText(MainActivity.this, "journal Added Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, ListEntryActivity.class);
                startActivity(myIntent);
            }
        });
        }
    private boolean inputsAreCorrect(String thoughts) {
        if (thoughts.isEmpty()) {
            EditThoughts.setError("Please enter thoughts Value");
            EditThoughts.requestFocus();
            return false;
        }
        return true;
    }
     private void createEntry() {
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS journalEntry (\n" +
                        "    id int NOT NULL CONSTRAINT entry_pk PRIMARY KEY,\n" +
                        "    thoughts varchar(400) NOT NULL,\n" +
                        "    day datetime not NULL\n" +
                        ");"
        );
    }
}
