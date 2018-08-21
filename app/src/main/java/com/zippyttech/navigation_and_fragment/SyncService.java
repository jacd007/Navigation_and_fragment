package com.zippyttech.navigation_and_fragment;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.zippyttech.navigation_and_fragment.Models.Noticia;
import com.zippyttech.navigation_and_fragment.Models.NoticiasSQLiteHelper;
import com.zippyttech.navigation_and_fragment.common.ApiCall;
import com.zippyttech.navigation_and_fragment.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Thread.sleep;

public class SyncService extends Service {
    private final int TIME_SYNC = 5000;

    private String url_weather="http://api.openweathermap.org/data/2.5/forecast?id=524901&units=metric&APPID=44d8a60f7707ec918da8c1123c521ab1";
    private String url_news="https://lanacionweb.com/wp-json/wp/v2/posts";

    private ProgressDialog dialog;

    public static final String SHARED_KEY ="shared_key";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    NoticiasDB noticiasDB;

    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creando servicio...");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio iniciado...");
        Toast.makeText(SyncService.this, "Sync's Service start!", Toast.LENGTH_SHORT).show();

        try{

            hilo hil = new hilo(this);
            hil.execute();

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
   public int V;
        @Override
        protected String doInBackground(String... strings) {
                try {


                    for (int i = 0; i < 2; i++) {
                      //  publishProgress(strings);

                        V=i;
                        sleep(5000);
                    }
                }
                catch (Exception e){

                }
            return null;
        }

        private void sendBroadCast(Context context) {
            Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("es.androcode.android.mybroadcast");
                broadcastIntent.putExtra("parameter", "Nueva notificacion.");
                context.sendBroadcast(broadcastIntent);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

          //  new Utils.GetData(context,url_news);

          //  sendBroadCast(context);
           // throudNotificacion(V);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           consulta(context);
            sendBroadCast(context);
           // throudNotificacion(V);
        }
    }

    private void consulta(Context context) {

        NoticiasDB db = new NoticiasDB(this);
        String [] parametros = {"33250"};
        String [] campos = {"codigo","titulo"};
       Cursor c= db.selectRows("Noticia",campos,null,null,null);
      if( c.moveToFirst()) {
          Log.w("Sync", c.getString(0));
      }
           /* NoticiasSQLiteHelper  conn = new NoticiasSQLiteHelper(context, "DBNoticias", null, 1);
            String RES="";
            try{
                SQLiteDatabase db = conn.getReadableDatabase();
                String [] parametros = {"33250"};
                String [] campos = {"codigo","titulo"};
                Cursor c = db.query("Noticia",campos,"codigo"+"=? ",parametros,null,null,null);
                c.moveToFirst();
                String sp = c.getString(0);
                c.close();

            }catch (Exception e){
                Toast.makeText(context,"El documento no existe",Toast.LENGTH_LONG).show();

            }

            */
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void throudNotificacion(int cont){

        Intent intent = new Intent(this, SyncNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("NOTIFICACION")
                .setContentText("Notificacion del sistema").setSmallIcon(R.drawable.ic_stat_sync)
                .setContentIntent(pIntent)
                .setTicker("Iniciando servicio")
                .setContentInfo("0")
                .setSound(sonido)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

}
