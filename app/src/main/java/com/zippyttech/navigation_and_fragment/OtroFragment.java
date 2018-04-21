package com.zippyttech.navigation_and_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zippyttech.navigation_and_fragment.common.ApiCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtroFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OtroFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtroFragment newInstance(String param1, String param2) {
        OtroFragment fragment = new OtroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);


        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dolar, container, false);
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
            dialog.setMessage("Cargando data");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resp = call.callGet("https://lanacionweb.com/wp-json/wp/v2/posts");
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
                    noticia.setTitulo(item.getJSONObject("title").getString("rendered"));
                    noticiaList.add(noticia);

                }

                refreshCustomerList(noticiaList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshCustomerList(List<Noticia> noticiaList) {
      /*  if (noticiaList != null) {
            layoutManager = new LinearLayoutManager(getContext());
            adapter = new RecyclerAdapter(noticiaList,getContext(), (AppCompatActivity) getActivity());
        }*/
    }

}
