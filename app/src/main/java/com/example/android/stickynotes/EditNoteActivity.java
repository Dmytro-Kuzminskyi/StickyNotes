package com.example.android.stickynotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditTextWithListener mUserText;
    private TextView mTextView;
    private TextView mCharactersCounter;
    private Note initialNote;
    private Note editedNote;
    private Menu menu;

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
                switchesMenuItems();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserText.requestFocus();
            }
        });
        mUserText.setBackPressedListener(new EditTextWithListener.BackPressedListener() {
            @Override
            public void onImeBack(EditTextWithListener editText) {
                mUserText.clearFocus();
            }
        });
        mUserText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mUserText.setCursorVisible(false);
                } else {
                    mUserText.setCursorVisible(true);
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_edit_note_activity: {
                mUserText.clearFocus();
                setText(mTextView);
                initialNote.setAbsoluteTime(System.currentTimeMillis());
                initialNote.setNote(mUserText.getText().toString());
                initialNote.setDateTime(mTextView.getText().toString());
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
            case R.id.revert_edit_note_activity: {
                mUserText.setText(initialNote.getNote());
                mUserText.setSelection(initialNote.getNote().length());
                return true;
            }
            case android.R.id.home: {
                backToMainActivity();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_activity_default_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (mUserText.hasFocus()) {
            getMenuInflater().inflate(R.menu.edit_note_activity_edit_mode_menu, menu);
            this.menu = menu;
            return true;
        }
        else {
            getMenuInflater().inflate(R.menu.edit_note_activity_default_menu, menu);
            return true;
        }
    }

    private void switchesMenuItems() {
        if (mUserText.getText().toString().equals(initialNote.getNote())) {
            menu.findItem(R.id.confirm_edit_note_activity).setEnabled(false);
            menu.findItem(R.id.confirm_edit_note_activity).setIcon(R.drawable.ic_confirm_disabled);
            menu.findItem(R.id.revert_edit_note_activity).setEnabled(false);
            menu.findItem(R.id.revert_edit_note_activity).setIcon(R.drawable.ic_revert_disabled);
        } else {
            menu.findItem(R.id.confirm_edit_note_activity).setEnabled(true);
            menu.findItem(R.id.confirm_edit_note_activity).setIcon(R.drawable.ic_confirm);
            menu.findItem(R.id.revert_edit_note_activity).setEnabled(true);
            menu.findItem(R.id.revert_edit_note_activity).setIcon(R.drawable.ic_revert);
        }
    }

    public void backToMainActivity() {
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

    @Override
    public void onBackPressed() {
        backToMainActivity();
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
