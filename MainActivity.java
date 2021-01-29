package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

     ListView l1;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> mynotes=new ArrayList<String>();;
    static  TextView t1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_note,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.note){
            Intent i=new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(i);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l1=(ListView)findViewById(R.id.listview);
        t1=(TextView)findViewById(R.id.textView);


        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        HashSet<String>set= (HashSet<String>) sharedPreferences.getStringSet("note",null);
        if(set==null){
            mynotes.add("Example Note....");
        }
        else {
            mynotes=new ArrayList<>(set);
            text();
        }


        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.row,mynotes);
        l1.setAdapter(arrayAdapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                i.putExtra("noteid",position);
                startActivity(i);
            }
        });
        l1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete).setTitle("   Confirmation Alert").
                        setMessage("Do You really want to Delete !")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mynotes.remove( position);
                                arrayAdapter.notifyDataSetChanged();
                                HashSet<String> set= new HashSet<>(MainActivity.mynotes);
                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putStringSet("note",set).apply();
                                text();
                                Intent main_intent=new Intent(MainActivity.this,MainActivity.class);
                                PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this,0,main_intent,0);
                                Intent broad_int=new Intent(MainActivity.this,notificaton_Receiver.class);
                                PendingIntent pendingIntent1=PendingIntent.getBroadcast(MainActivity.this,0,broad_int,PendingIntent.FLAG_UPDATE_CURRENT);
                                if(Build.VERSION.SDK_INT>25) {
                                    int importance = NotificationManager.IMPORTANCE_DEFAULT;

                                    NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "MY_NAME", importance);
                                    NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                                    manager.createNotificationChannel(channel);
                                }

                                NotificationCompat.Builder nbuilder=new NotificationCompat.Builder(MainActivity.this,"CHANNEL_ID").
                                        setPriority(10).
                                setSmallIcon(android.R.drawable.sym_def_app_icon)
                                .setContentTitle("Notes")
                                        .setAutoCancel(true).
                                        setOnlyAlertOnce(true)
                                        .setContentIntent(pendingIntent)

                                .setContentText("You have Deleted a Note");
                                NotificationManager manager=(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(0,nbuilder.build());


                            }
                        }).setNegativeButton("Cancel",null).show();

                return true;
            }
        });

    }
    public static void text(){
        if(mynotes.size()<=0){
            t1.setText("You don't have any Notes..");
            t1.setTextColor(Color.RED);
        }
        else {
            t1.setText("Your Notes");
            t1.setTextColor(Color.WHITE);
        }
    }
}
