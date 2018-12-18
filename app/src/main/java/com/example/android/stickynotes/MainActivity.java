package com.example.android.stickynotes;

import java.util.ArrayList;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private NoteAdapter noteAdapter;
    private ActionBarDrawerToggle mToggle;
    private ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        DrawerLayout mDrawerLayout = findViewById(R.id.main_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(R.string.notes);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddNoteActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onResume() {
        getNotesFromDatabase();
        showNotes();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Note note = new Note(data.getIntExtra("ID", -1), data.getStringExtra("NOTE"), data.getStringExtra("DATE_TIME"), data.getLongExtra("ABS_TIME", 0));
        if (resultCode == 0) {
            if (!note.getNote().isEmpty())
                addNoteToDatabase(note.getNote(), note.getDateTime(), note.getAbsoluteTime());
        } else if (resultCode == 1) {
            if (!note.getNote().isEmpty())
                updateNoteInDatabase(note.getId(), note.getNote(), note.getDateTime(), note.getAbsoluteTime());
        }
        invalidateOptionsMenu();
    }

    private void addNoteToDatabase(String note, String dateTime, long absoluteTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note", note);
        cv.put("date_time", dateTime);
        cv.put("abs_time", absoluteTime);
        db.insert("Notes", null, cv);
        dbHelper.close();
    }

    private void updateNoteInDatabase(int id, String note, String dateTime, long absoluteTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note", note);
        cv.put("date_time", dateTime);
        cv.put("abs_time", absoluteTime);
        db.update("Notes", cv, "id="+ id, null);
        dbHelper.close();
    }

    private void getNotesFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Notes", null);
        notes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                String dateTime = cursor.getString(cursor.getColumnIndex("date_time"));
                long absoluteTime = cursor.getLong(cursor.getColumnIndex("abs_time"));
                notes.add(new Note(id, note, dateTime, absoluteTime));
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
    }

    private void showNotes() {
        noteAdapter = new NoteAdapter(this, notes);
        ListView listView = findViewById(R.id.main_list);
        listView.setAdapter(noteAdapter);
    }

    public void editNote(View v) {
        Note note = null;
        TextView mUserText = v.findViewById(R.id.note_text);
        String userText = mUserText.getText().toString();
        for (Note object: notes) {
            if (object.getNote() == userText) note = object;
        }
        Intent intent = new Intent(v.getContext(), EditNoteActivity.class);
        intent.putExtra("USER_TEXT", note.getNote());
        intent.putExtra("USER_TIME", note.getDateTime());
        intent.putExtra("ID", note.getId());
        startActivityForResult(intent, 1);
    }
}
