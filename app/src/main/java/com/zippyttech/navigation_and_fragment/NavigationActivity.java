package com.zippyttech.navigation_and_fragment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.zippyttech.navigation_and_fragment.Models.Noticia;
import com.zippyttech.navigation_and_fragment.common.PerfilFragment;
import com.zippyttech.navigation_and_fragment.common.Utils;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  DrawerLocker,ListFragment.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String SHARED_KEY = "shared_key";
    private  DrawerLayout drawer;
    private GoogleApiClient googleApiClient;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private TextView navUserName,navUserEmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private IntentFilter myFilter;
    private String usu;
    private int x=0;
    private String mail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
       // TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);


        navUserName = (TextView) headerView.findViewById(R.id.nav_user_name);
        navUserEmail = (TextView) headerView.findViewById(R.id.nav_email_user);
       settings = getSharedPreferences(SHARED_KEY,0);
        editor = settings.edit();



        setFragment(0);


        this.googleApiClient = new GoogleApiClient
                .Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API).build();



        usu=settings.getString("UserName","");
        mail=settings.getString("EmailName","");
    if(usu!=null) {
        navUserName.setText(usu);
    }
    if (mail!=null){
        navUserEmail.setText(mail);
    }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_button) {
            Toast.makeText(this, "accion", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
          String msg="este es la prueba";

        int id = item.getItemId();
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("weather",false);
        editor.commit();

        if (id == R.id.nav_o1) {
           item.setChecked(true);
           drawer.closeDrawer(GravityCompat.START);

            getSupportActionBar().setTitle("Mi Perfil");

            setFragment(4);
          //  drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        } else if (id == R.id.nav_o2) {
           item.setChecked(true);
            getSupportActionBar().setTitle("Listado de Noticias");
            drawer.closeDrawer(GravityCompat.START);

            setFragment(1);
           // drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_o3) {
            item.setChecked(true);
            getSupportActionBar().setTitle("DolarToday");
            drawer.closeDrawer(GravityCompat.START);
            setFragment(2);
        } else if (id == R.id.nav_o5) {
            item.setChecked(true);
            getSupportActionBar().setTitle("Agenda");
            drawer.closeDrawer(GravityCompat.START);
            setFragment(3);
        } else if (id == R.id.nav_o4) {
            item.setChecked(true);
            getSupportActionBar().setTitle("El Clima");
            drawer.closeDrawer(GravityCompat.START);
            setFragment(5);
        } else if (id == R.id.nav_o6) {/** Boton actualizar BD con notificacion */
           /* Intent i = new Intent();
            i.setAction("com.proyectosimio.BUTTON_RECEIVER");
            sendBroadcast(i);*/
           // ListFragment.GetData getData = new ListFragment.GetData(ListFragment.GetData.class,);
            x++;
           //  ViewToastInflate(msg);
             NotificacionButton(x);

        }else if (id == R.id.nav_logout) { /** CLEAR SHARED_PREFERENTS */
          //  logOut();
           // revoke();
           Logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void ViewToastInflate(String msg) {
        //sirve para un toast personalizado
        Toast toast3 = new Toast(getApplicationContext());

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) findViewById(R.id.lytLayout));

        TextView txtMsg = (TextView)layout.findViewById(R.id.txtMensaje);
        txtMsg.setText(""+msg);

        toast3.setDuration(Toast.LENGTH_SHORT);
        toast3.setView(layout);
        toast3.show();

    }

    private void Logout() {

String provider = settings.getString("providerLogin","");
        if(provider.equals("google")){
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    //todo:actions
                    Log.i("NavigationActivity","Logout From Google");
                    Toast.makeText(NavigationActivity.this, "Logout From Google", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if(provider.equals("facebook")){
           if( AccessToken.getCurrentAccessToken() !=null){
               new GraphRequest(AccessToken.getCurrentAccessToken(),
                       "email, public_profile",
                       null,
                       HttpMethod.DELETE,
                       new GraphRequest.Callback() {
                           @Override
                           public void onCompleted(GraphResponse response) {
                               LoginManager.getInstance().logOut();
                               Log.i("NavigationActivity","Logout From Facebook");
                               Toast.makeText(NavigationActivity.this, "Logout From Facebook", Toast.LENGTH_SHORT).show();
                           }
                       }).executeAsync();
               FacebookSdk.sdkInitialize(this);
           }
        }
        editor.clear();
        FirebaseAuth.getInstance().signOut();
        editor.commit();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private BroadcastReceiver myReceiver;
    @Override
    protected void onResume() {
        super.onResume();
        myFilter = new IntentFilter();
        myFilter.addAction("es.androcode.android.mybroadcast");

        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int valor= intent.getExtras().getInt("iniciar");
                String myParam = intent.getExtras().getString("parameter");
                if (myParam != null) {
                 //   Toast.makeText(context, myParam, Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "se ha realizado el broadcast", Toast.LENGTH_LONG).show();
                }
            }
        };
        registerReceiver(myReceiver, myFilter);

    }



    @Override
    protected void onPause() {
        unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void goLogInScreen() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public  void logOut(View view){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                     goLogInScreen();
                }else{
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setFragment(int position) {
        android.support.v4.app.FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                MainFragment inboxFragment = new MainFragment();
                fragmentTransaction.replace(R.id.content_frame, inboxFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
               ListFragment starredFragment = new ListFragment();
                fragmentTransaction.replace(R.id.content_frame, starredFragment);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                DolarFragment dolarFragment = new DolarFragment();
                fragmentTransaction.replace(R.id.content_frame, dolarFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                   fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                CalendarFragment calendarFragment = new CalendarFragment();
                fragmentTransaction.replace(R.id.content_frame, calendarFragment);
                fragmentTransaction.commit();
                break;
            case 4:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                PerfilFragment perfilFragment = new PerfilFragment();
                fragmentTransaction.replace(R.id.content_frame, perfilFragment);
                fragmentTransaction.commit();
                break;

            case 5:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                WeatherFragment weatherFragment = new WeatherFragment();
                fragmentTransaction.replace(R.id.content_frame, weatherFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void NotificacionButton(int x){
        // notificacion para avisar de la sincronizacion con la bd de la nacion
        Intent intent = new Intent(this, NewMessageNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
        Uri sonid = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notif = new Notification.Builder(this)
                    .setContentTitle("Informacion")
                    .setContentText(""+getString(R.string.info_content_syncBD))
                    .setSmallIcon(R.drawable.ic_go)
                    .setColor(64)
                    .setTicker("test...")
                    .setContentInfo("#"+x)
                    .setContentIntent(pIntent)
                    .setContentIntent(pIntent)
                    .setSound(sonid)
                    .build();
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(2, notif);
    }

}
