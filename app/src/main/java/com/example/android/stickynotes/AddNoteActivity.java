package com.example.android.stickynotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.Format;
import android.content.Intent;

public class AddNoteActivity extends AppCompatActivity {
    private EditText mUserText;
    private TextView mTextView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        getSupportActionBar().setTitle(R.string.add_note);
        mTextView = findViewById(R.id.above_add_note_text);
        setText(mTextView);
        mUserText = findViewById(R.id.add_note);
        mUserText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                menuItemsChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserText.setCursorVisible(true);
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note_activity_menu, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_add_note_activity: {
                backToMainActivity();
                return true;
            }
            case R.id.revert_add_note_activity: {
                mUserText.setText("");
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

    private void menuItemsChange() {
        if (mUserText.getText().toString().isEmpty()) {
            menu.findItem(R.id.confirm_add_note_activity).setEnabled(false);
            menu.findItem(R.id.confirm_add_note_activity).setIcon(R.drawable.ic_confirm_disabled);
            menu.findItem(R.id.revert_add_note_activity).setEnabled(false);
            menu.findItem(R.id.revert_add_note_activity).setIcon(R.drawable.ic_revert_disabled);
        } else {
            menu.findItem(R.id.confirm_add_note_activity).setEnabled(true);
            menu.findItem(R.id.confirm_add_note_activity).setIcon(R.drawable.ic_confirm);
            menu.findItem(R.id.revert_add_note_activity).setEnabled(true);
            menu.findItem(R.id.revert_add_note_activity).setIcon(R.drawable.ic_revert);
        }
    }

    @Override
    public void onBackPressed() {
        backToMainActivity();
    }

    private void backToMainActivity() {
        Intent intent = new Intent();
        setText(mTextView);
        long absoluteTime = System.currentTimeMillis();
        intent.putExtra("NOTE", new Note(mUserText.getText().toString(), mTextView.getText().toString(), absoluteTime));
        setResult(RESULT_OK, intent);
        finish();
    }
}
