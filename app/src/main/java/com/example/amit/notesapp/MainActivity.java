package com.example.amit.notesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List mNotesList;
    private static final int EDITOR_REQ=1;
    private  int mCureentNote=0;
    private ArrayAdapter mArrayAdapter;
    private static final int EDITOR_ADD_REQ=2;
    protected SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView=findViewById(R.id.listview);
        mSharedPreferences=getApplicationContext().getSharedPreferences("package com.example.amit.notesapp", Activity.MODE_PRIVATE);



        HashSet<String> hashSet=(HashSet<String>)mSharedPreferences.getStringSet("results",null);
        if(hashSet!=null)
        {
            mNotesList=new ArrayList(hashSet);
        }
        else
            { mNotesList = new ArrayList<String>();
            mNotesList.add("This is a sample note");
        }



        mArrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,mNotesList);
        mListView.setAdapter(mArrayAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCureentNote=i;
                Intent intent = NoteEditorActivity.createIntent(MainActivity.this,mNotesList.get(i).toString());
                startActivityForResult(intent,EDITOR_REQ);

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int temp=i;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Do You really want to delete this note?")
                        .setMessage("This note will be deleted")
                        .setIcon(R.drawable.ic_launcher_background)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mNotesList.remove(temp);
                                mArrayAdapter.notifyDataSetChanged();
                                updateSharedPreferences();
                            }
                        })
                        .setNegativeButton("No",null)
                         .show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode!= Activity.RESULT_OK)
        {
            return;
        }
        if(requestCode==EDITOR_REQ)
        {
            {
                if(data==null)
                {
                    return;
                }
                mNotesList.set(mCureentNote,data.getStringExtra(NoteEditorActivity.RESULT));
               // Log.i("Result:",data.getStringExtra(NoteEditorActivity.EXTRA));
                mArrayAdapter.notifyDataSetChanged();
                updateSharedPreferences();
            }
        }
        if(requestCode==EDITOR_ADD_REQ)
        {
            if(data==null)
            {
                return;
            }
            mNotesList.add(data.getStringExtra(NoteEditorActivity.RESULT));
            mArrayAdapter.notifyDataSetChanged();
            updateSharedPreferences();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_note)
        {
            Intent intent = NoteEditorActivity.createIntent(MainActivity.this,"");
            startActivityForResult(intent,EDITOR_ADD_REQ);
            return true;
        }
        return false;
    }
    public void updateSharedPreferences()
    {
        HashSet<String> hashSet =new HashSet<>();
        hashSet.addAll(mNotesList);
        mSharedPreferences.edit().putStringSet("results",hashSet).apply();
        Log.i("saved:",mSharedPreferences.getStringSet("results",hashSet).toString());
    }
}
