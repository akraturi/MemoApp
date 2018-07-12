package com.example.amit.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {

    public static final String EXTRA="intent extra";
    private EditText mEditText;
    private String results;
    public static final int REQ_CODE=0;
    public static final String RESULT="result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        mEditText=findViewById(R.id.editText);
        mEditText.setText(getIntent().getStringExtra(EXTRA));
        mEditText.setSelection(mEditText.getText().length());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("Text after changing:",mEditText.getText().toString());
                setText(mEditText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static Intent createIntent(Context context, String note)
    {
        Intent intent = new Intent(context,NoteEditorActivity.class);
        intent.putExtra(EXTRA,note);
        return intent;
    }
    public void setText(String result)
    {
        Intent intent = new Intent();
        intent.putExtra(RESULT,result);
        Log.i("Result set:",result);
        setResult(RESULT_OK,intent);
    }

}
