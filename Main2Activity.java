package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class Main2Activity extends AppCompatActivity {
    int noteId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        EditText e=(EditText)findViewById(R.id.editText);

        Intent intent=getIntent();
        noteId= intent.getIntExtra("noteid",-1);
        if(noteId!=-1){

            e.setText(MainActivity.mynotes.get(noteId));
        }
        else {

            MainActivity.mynotes.add("");
            noteId=MainActivity.mynotes.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
            MainActivity.text();

        }
        e.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                MainActivity.mynotes.set(noteId,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                HashSet <String>set= new HashSet<>(MainActivity.mynotes);
                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                sharedPreferences.edit().putStringSet("note",set).apply();
                MainActivity.text();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


    }
}
