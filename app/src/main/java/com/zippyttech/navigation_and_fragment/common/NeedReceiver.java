package com.zippyttech.navigation_and_fragment.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zippyttech.navigation_and_fragment.SyncService;

public class NeedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Vamos a iniciar servicios", Toast.LENGTH_SHORT).show();

        if(context!=null);
        {  Intent serv = new Intent(context, SyncService.class);
            context.startService(serv);
        }

    }
}
