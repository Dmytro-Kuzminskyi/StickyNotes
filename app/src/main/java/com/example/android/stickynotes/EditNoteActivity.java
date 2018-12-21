package com.example.android.stickynotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
    private TextView mCharactersCounter;
    private Note initialNote;
    private Note editedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);
        getSupportActionBar().setTitle(R.string.edit_note);
        initialNote = editedNote = (Note) getIntent().getSerializableExtra("NOTE");
        mCharactersCounter = findViewById(R.id.characters_counter);
        mTextView = findViewById(R.id.above_edit_note_text);
        mTextView.setText(initialNote.getDateTime());
        mUserText = findViewById(R.id.edit_note);
        mUserText.setText(initialNote.getNote());
        mCharactersCounter.setText(String.valueOf(countOfCharacters(mUserText.getText())));
        mUserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCharactersCounter.setText(String.valueOf(countOfCharacters(s)));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (!mUserText.getText().toString().equals(initialNote.getNote())) {
            setText(mTextView);
            editedNote.setAbsoluteTime(System.currentTimeMillis());
            editedNote.setNote(mUserText.getText().toString());
            editedNote.setDateTime(mTextView.getText().toString());
        }
        intent.putExtra("NOTE", editedNote);
        setResult(RESULT_OK, intent);
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

    private int countOfCharacters(Editable object) {
        String text = object.toString();
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != 0x0A) counter++;
        }
        return counter;
    }

    private int countOfCharacters(CharSequence seq) {
        String text = seq.toString();
        int counter = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != 0x0A) counter++;
        }
        return counter;
    }
}
