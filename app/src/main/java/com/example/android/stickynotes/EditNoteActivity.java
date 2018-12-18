package com.example.android.stickynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText mUserText;
    private TextView mTextView;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        getSupportActionBar().setTitle(R.string.edit_note);
        note = (Note) getIntent().getSerializableExtra("NOTE");
        mTextView = findViewById(R.id.above_edit_note_text);
        mTextView.setText(note.getDateTime());
        mUserText = findViewById(R.id.edit_note);
        mUserText.setText(note.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        menu.findItem(R.id.actionbar_settings).setVisible(false);
        menu.findItem(R.id.actionbar_confirm).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_confirm: {
                onBackPressed();
            }
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setText(mTextView);
        note.setAbsoluteTime(System.currentTimeMillis());
        note.setNote(mUserText.getText().toString());
        note.setDateTime(mTextView.getText().toString());
        intent.putExtra("NOTE", note);
        setResult(1, intent);
        finish();
    }

    private void setText(TextView view) {
        Format dateFormat = android.text.format.DateFormat.getLongDateFormat(getApplicationContext());
        Format timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        String patternDate = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        String patternDateWithoutYear = patternDate.replaceAll("\\W?[Yy,]+\\W?", "");
        Date dateTime = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyLocalizedPattern(patternDateWithoutYear);
        String output = simpleDateFormat.format(dateTime) + " " + (((DateFormat) timeFormat).format(dateTime));
        view.setText(output);
    }
}
