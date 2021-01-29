package com.example.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class notificaton_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg=intent.getStringExtra("Rahul");
        Intent  intent1=new Intent(context,MainActivity.class);
        context.startActivity(intent1);
    }
}
