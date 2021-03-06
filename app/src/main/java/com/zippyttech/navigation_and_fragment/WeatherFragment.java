package com.zippyttech.navigation_and_fragment;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.state.Weather;
import com.zippyttech.navigation_and_fragment.common.ApiCall;
import com.zippyttech.navigation_and_fragment.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView weather,tiempo,descripcion;
    private ImageView image;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //TODO: URL Variable declaration
    String url_weather="http://api.openweathermap.org/data/2.5/forecast?id=524901&units=metric&APPID=44d8a60f7707ec918da8c1123c521ab1";

    private OnFragmentInteractionListener mListener;
    private IntentFilter Filter;
    private int imagen;


    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetData getData = new GetData(getContext(),0);
        getData.execute();

        setThisFragment();
    }

    public void setThisFragment(){
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weather, container, false);
        // Inflate the layout for this fragment

        weather = (TextView) view.findViewById(R.id.weather);
        tiempo = (TextView) view.findViewById(R.id.time);
        descripcion = (TextView) view.findViewById(R.id.description);
        image = (ImageView) view.findViewById(R.id.image_flat_weather);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GetData extends AsyncTask<String,String,String> {
        private ApiCall call;
        private ProgressDialog dialog;
        private int UPDATE;
        public GetData(Context context, int upd){
            this.call = new ApiCall(context);
            this.UPDATE=upd;
            dialog = new ProgressDialog(context);
            dialog.setMessage("Cargando data del clima");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String  resp = call.callGet(url_weather);
          // String resp = call.callGet("http://api.openweathermap.org/data/2.5/weather?lat=7.7560392&lon=-72.2310149&APPID=44d8a60f7707ec918da8c1123c521ab1");
            return resp;
        }



        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            dialog.dismiss();
            try {
                String main="",tiemp="",desc="";

                    JSONObject item = new JSONObject(resp);
                    JSONArray list = item.getJSONArray("list");

                     main =  String.valueOf( list.getJSONObject(0).getJSONObject("main").getDouble("temp") );
               JSONObject z =  list.getJSONObject(0) ;
               list = z.getJSONArray("weather");
               tiemp =  list.getJSONObject(0).getString("main") ;
                desc = list.getJSONObject(0).getString("description");

                // JSONArray l = i.getJSONArray(0);
                double v= Double.parseDouble(main);

                    weather.setText("" + main+"°C");
                    tiempo.setText("" + tiemp );
                    descripcion.setText("" + desc);
                if (tiemp.compareTo("Clear")==0){
                    image.setImageResource(R.drawable.sunny);
                    imagen = R.drawable.sunny;
                }
                if (tiemp.compareTo("Clouds")==0){
                    image.setImageResource(R.drawable.cloud);
                    imagen = R.drawable.cloud;
                }
                if (tiemp.compareTo("Rain")==0){
                    image.setImageResource(R.drawable.rain);
                    imagen = R.drawable.rain;
                }
                NotificacionWeather(v);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private BroadcastReceiver Receiver;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void NotificacionWeather(double value ){
        // notificacion para avisar de la sincronizacion con la bd de la nacion
        Intent intent = new Intent(getContext(), NewMessageNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity(this.getContext(), (int) System.currentTimeMillis(), intent, 0);
        Uri sonid = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notif = new Notification.Builder(getContext())
                    .setContentTitle("El Clima Actual...")
                    //.setContentText(""+subcontent.substring(0,20))
                    .setContentText(""+value+"°C")
                    .setColor(1200)
                    .setTicker("El Clima es: "+value+"°c")
                    .setContentInfo("Weather")
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.drawable.ic_sunny)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSound(sonid)
                    .build();
        }
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notif);
    }

}
