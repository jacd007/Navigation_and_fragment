package com.zippyttech.navigation_and_fragment.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zippyttech on 14/05/18.
 */

public class BroadcastPersonal extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Broadcast personalizado", Toast.LENGTH_SHORT).show();
    }
}
