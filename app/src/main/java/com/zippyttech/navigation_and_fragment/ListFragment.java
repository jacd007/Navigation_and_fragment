package com.zippyttech.navigation_and_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zippyttech.navigation_and_fragment.common.ApiCall;
import com.zippyttech.navigation_and_fragment.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerAdapter adapter;



    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_list, container, false);
     recyclerView = v.findViewById(R.id.lista);

        return v;


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
            dialog.setMessage("Cargando data de LA NACIÓN");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String  resp = call.callGet("https://lanacionweb.com/wp-json/wp/v2/posts");
            return resp;
        }



        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            dialog.dismiss();
            try {
                JSONArray array = new JSONArray(resp);
                List<Noticia> noticiaList = new ArrayList<>();



                for(int i=0; i<array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Noticia noticia = new Noticia();
                   // item.getJSONObject("content").getString("rendered")/*.replace("<p>","").replace("<strong>","").replace("&#8220","\"").replace("&#8221","\"")*/;
                    String c = Utils.RegexReplaceSimbol(item.getJSONObject("content").getString("rendered"));
                    noticia.setCodigo(String.valueOf(item.getInt("id")));
                    noticia.setTitulo(item.getJSONObject("title").getString("rendered"));
                    noticia.setContenido(c);
                    String date = Utils.dateFormater(item.getString("date"),"MMMM d, yyyy HH:mm:ss");
                    noticia.setFecha(date);
                  //  noticia.setFecha("Fecha: "+item.getString("date").substring(0,16).replace("-","/").replace("T"," Hora: "));
                    noticia.setImagen("");
                    //    noticia.setFecha(item.getJSONObject("_links").getJSONArray("self").getJSONObject(0).getString("href"));
                    noticiaList.add(noticia);

                }
            /*  noticiasDB.insertarNoticias(noticiaList);
                editor.putBoolean("banderadb",true);
                editor.commit();
                if(UPDATE==0)*/
                    refreshCustomerList(noticiaList);
              /*  else
                    adapter.changeDataItem(noticiasDB.getList());
 */
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshCustomerList(List<Noticia> noticiaList) {
            if (noticiaList != null) {
                recyclerView.setHasFixedSize(true);
                // layoutManager = new LinearLayoutManager(this);
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                //adapter = new RecyclerAdapter(listado, this, this);
                adapter = new RecyclerAdapter(noticiaList,getContext(), (AppCompatActivity) getActivity());
                recyclerView.setAdapter(adapter);

            }
            // else Toast.makeText(this,"hay algo",Toast.LENGTH_SHORT).show();


    }


}
