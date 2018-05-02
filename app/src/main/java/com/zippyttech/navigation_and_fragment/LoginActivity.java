package com.zippyttech.navigation_and_fragment;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.zippyttech.navigation_and_fragment.common.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;
    public static final String SHARED_KEY ="shared_key";
    private EditText user,pass;
    private Button ingresar;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ProfileTracker profileTracker;
    private AccessTokenManager accessTokenManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        //loginButton.setFragment(this);

        startService(new Intent(this, SyncService.class));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(LoginActivity.this, "onSuccess del boton registrer", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this, "onCancel del boton registrer", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this, "onError del boton registrer", Toast.LENGTH_SHORT).show();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Toast.makeText(LoginActivity.this, "onSuccess del login", Toast.LENGTH_SHORT).show();
                        String token = String.valueOf(loginResult.getAccessToken());
                        editor.putString("FacebookToken",token);
                        editor.putString("providerLogin","facebook");
                        editor.apply();
                        goMainScreenAll();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(LoginActivity.this, "onCancel del login", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, "onError del login", Toast.LENGTH_SHORT).show();
                    }
                });

        user = (EditText) findViewById(R.id.users);

        pass = (EditText) findViewById(R.id.pass);
        ingresar = (Button) findViewById(R.id.ingresar);
        ingresar.setOnClickListener(this);


        settings = getSharedPreferences(SHARED_KEY,0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);

        if(settings.getBoolean("logged",false)){

            Intent navigation = new Intent(this, NavigationActivity.class);
            startActivity(navigation);
            this.finish();
        }
        editor = settings.edit();
        signInButton.setOnClickListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==SIGN_IN_CODE){
          GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
          handleSignInResult(result);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void handleSignInResult(GoogleSignInResult result) {
        
        if (result.isSuccess()){
           saveData(result);
            ValidateUser validateUser = new ValidateUser(this,result);
            validateUser.execute();
            //goMainScreen(result);
        }else {
            Toast.makeText(this, R.string.not_log_in, Toast.LENGTH_LONG).show();
        }
    }

    public void saveData(GoogleSignInResult result){
        GoogleSignInAccount account = result.getSignInAccount();
        editor.putString("UserName", account.getDisplayName());
        editor.putString("EmailName", account.getEmail());
        editor.putString("ImageName",account.getPhotoUrl().toString());
        editor.putString("GoogleToken", account.getIdToken());
        editor.putString("providerLogin","google");
        editor.commit();
        editor.apply();
    }


    private void goMainScreen(GoogleSignInResult result) {

        Intent intent = new Intent(this,NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void goMainScreenAll() {
        Intent intent = new Intent(this,NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.ingresar){
            if(user.getText().toString().equals("admin") && pass.getText().toString().equals("123456")) {
             //   Toast.makeText(this, "ingresar", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged",true);
                //editor.putString("user",user.getText().toString());
                editor.putString("UserName",user.getText().toString());
                editor.putString("EmailName", ""+user.getText().toString()+"@example.com");
                editor.commit();
                this.finish();
                Intent navigation = new Intent(this, NavigationActivity.class);
                startActivity(navigation);
            }
            else {
                if(!pass.getText().toString().equals("")) user.setError("Error al ingresar el usuario");
                else if(!user.getText().toString().equals("")) pass.setError("Error al ingresar la contraseña");
                else Toast.makeText(this,"Ingrese usuario y/o contraseña",Toast.LENGTH_SHORT).show();
                //
            }
        }
        else if(view.getId()==R.id.signInButton) {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, SIGN_IN_CODE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("logged",true);
            editor.commit();


        }
    }


    public class ValidateUser extends AsyncTask<String,String,String>{
      private Context context;
      private ProgressDialog progress;
      private SharedPreferences settings;
      private SharedPreferences.Editor editor;
      private GoogleSignInResult result;
      public ValidateUser(Context  context, GoogleSignInResult result){
      this.context=context;
          progress= new ProgressDialog(context);
         settings = context.getSharedPreferences(SHARED_KEY,0);
         editor = settings.edit();
         this.result = result;
      }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setMessage("Cargando data...");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            Bitmap bitmap = Utils.getBitmapFromURL(settings.getString("ImageName","null"));
            if(bitmap!=null){
                String imageb64 = Utils.encodeImage(bitmap);
                editor.putString("ImageName",imageb64);
                editor.commit();

            }
/*
            InputStream is = null;
            try {
               // is = (InputStream) new URL(settings.getString("ImageName","null")).getContent();
             //   Drawable d = Drawable.createFromStream(is, "src name");

            } catch (IOException e) {
                e.printStackTrace();
            }
*/
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            goMainScreen(result);
        }
    }
}
