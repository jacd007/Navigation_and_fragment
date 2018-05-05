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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
 * {@link DolarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DolarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DolarFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView resultado,cambio,dolar;
    private Button aceptar;
    private RadioButton check_bs_ps,check_ps_bs;
    private EditText ref;
    private RadioGroup radio;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public DolarFragment() {
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
    public static DolarFragment newInstance(String param1, String param2) {
        DolarFragment fragment = new DolarFragment();
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
        View v=inflater.inflate(R.layout.fragment_dolar, container, false);
        // Inflate the layout for this fragment
        //TODO: instances
        resultado = (TextView) v.findViewById(R.id.view_text_final);
        dolar = (TextView) v.findViewById(R.id.statusDolar);
        cambio = (TextView) v.findViewById(R.id.cambio_ref);
        aceptar = (Button) v.findViewById(R.id.aceptar);
        check_bs_ps = (RadioButton) v.findViewById(R.id.check_bs_ps);
        check_ps_bs = (RadioButton) v.findViewById(R.id.check_ps_bs);
        ref = (EditText) v.findViewById(R.id.editText_ref);
        radio = (RadioGroup) v.findViewById(R.id.radio);
        aceptar.setOnClickListener(this);
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
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        float refer = Float.parseFloat(String.valueOf(cambio.getText())),
                values = Float.parseFloat(String.valueOf(ref.getText())),
                rep=0;

                    if (check_bs_ps.isChecked())
                        rep = values * refer;

                    if (check_ps_bs.isChecked()){
                        if (refer!=0)  rep = values / refer;
                        else rep=0;
                    }

                   String c= Utils.cientificMethod(rep);
                resultado.setText(""+rep);
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
            dialog.setMessage("Cargando data de DolarToday");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resp = call.callGet("https://s3.amazonaws.com/dolartoday/data.json");
            return resp;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            dialog.dismiss();
            try {
                  JSONArray array = new JSONArray(resp);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
