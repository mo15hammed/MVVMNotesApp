package com.example.mo15hammed.mvvmnotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddEditNoteActivity";

    public static final String EXTRA_ID = "com.example.mo15hammed.mvvmnotesapp.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.mo15hammed.mvvmnotesapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.mo15hammed.mvvmnotesapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.mo15hammed.mvvmnotesapp.EXTRA_PRIORITY";

    private EditText mTitle, mDescription;
    private NumberPicker mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mTitle = findViewById(R.id.edt_title);
        mDescription = findViewById(R.id.edt_desc);
        mPriority = findViewById(R.id.number_picker);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            getSupportActionBar().setTitle("Edit Note");
            mTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            mDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            mPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            getSupportActionBar().setTitle("Add Note");
        }

    }


    private void saveNote() {

        String title = mTitle.getText().toString();
        String desc = mDescription.getText().toString();
        int priority = mPriority.getValue();

        if (title.trim().isEmpty() || desc.trim().isEmpty()) {
            Toast.makeText(this, "Please Insert Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, desc);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
