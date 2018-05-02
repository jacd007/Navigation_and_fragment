package com.zippyttech.navigation_and_fragment;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class SyncService extends Service {
    private final int TIME_SYNC = 5000;
    private ProgressDialog dialog;
    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Servicio creado...");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio iniciado...");
        Toast.makeText(SyncService.this, "Sync's Service start!", Toast.LENGTH_SHORT).show();

        try{

            hilo hil = new hilo(this);
            hil.execute();
           /* thread.start();

            new Handler().postDelayed(new Runnable(){
                public void run(){
                    Toast.makeText(SyncService.this, "wait "+TIME_SYNC/1000 + "seg...", Toast.LENGTH_SHORT).show();
                }
            }, TIME_SYNC);
**/
        }catch (Exception e){
            e.printStackTrace();
        }


        return START_STICKY;
      //  return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Sync's Service done!", Toast.LENGTH_SHORT).show();
    }
    public class hilo extends AsyncTask<String,String,String>{
        Context context ;
   public hilo(Context context){
       this.context = context;

   }
        @Override
        protected String doInBackground(String... strings) {
    try {
        for (int i = 0; i < 10; i++) {
            publishProgress(strings);
            sleep(3000);
        }
    }
    catch (Exception e){

    }
            return null;

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            throudNotificacion();

         //   Toast.makeText(context, "servicio", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void throudNotificacion(){
        Intent intent = new Intent(this, SyncNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("NOTIFICACION")
                .setContentText("Esta es una notificacion").setSmallIcon(R.drawable.ic_stat_sync)
                .setContentIntent(pIntent)
                .setSound(sonido)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

}
