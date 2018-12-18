package com.example.android.stickynotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        String title = getIntent().getStringExtra("TITLE");
        getSupportActionBar().setTitle(title);
        mTextView = findViewById(R.id.above_add_note_text);
        setText(mTextView);
        mUserText = findViewById(R.id.add_note);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        MenuItem item = menu.findItem(R.id.actionbar_add_note);
        item.setTitle(R.string.confirm);
        item.setIcon(R.drawable.ic_confirm);
        return true;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_add_note: {
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
        long absoluteTime = System.currentTimeMillis();
        intent.putExtra("NOTE",mUserText.getText().toString());
        intent.putExtra("DATE_TIME",mTextView.getText().toString());
        intent.putExtra("ABS_TIME", absoluteTime);
        setResult(RESULT_OK, intent);
        finish();
    }
}
