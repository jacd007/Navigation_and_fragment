package com.zippyttech.navigation_and_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 28/03/18.
 */

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {



   private ItemClickListener itemClickListener;
    private AppCompatActivity ap;
    private AppCompatActivity ap2;

    private TextView tv_content;
    private TextView tv_title;
    private TextView fecha;

    /**
     *
     * @param itemView Item on List
     * @param act   Activity
     *
     */

    public RecyclerViewHolder(View itemView, AppCompatActivity act) {
        super(itemView);


        ap2=act;
        ap=act;
        tv_title = itemView.findViewById(R.id.titulo);
        tv_content = itemView.findViewById(R.id.contenido);
        fecha = itemView.findViewById(R.id.fecha);
        //Typeface face = Typeface.createFromAsset(ap2.getAssets(), "fonts/century_gothic.ttf");
     //   tv_title.setTypeface(face);

        itemView.setOnClickListener(this);


    }

    public void setData(int id, String title, String content, String date){

        tv_title.setText(title);
        tv_content.setText(content);
        fecha.setText(date);

    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false,ap);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true,ap);
        return false;
    }
}

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {


    private List<Noticia> listData = new ArrayList<>(); // array tipo noticia
    private AppCompatActivity acti;
    private Context context;
    private AppCompatActivity Actividad;

    private RecyclerView list;


    /**
     *
     * @param listado
     * @param c
     * @param ap
     */

    public RecyclerAdapter(List<Noticia> listado, Context c, AppCompatActivity ap) {
        context = c;
        listData = listado;
        this.Actividad=ap;

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.setData(listData.get(position).getId(),listData.get(position).getTitulo(),listData.get(position).getContenido(),listData.get(position).getFecha());
        holder.setItemClickListener(new ItemClickListener() {

            @Override
            public void onClick(View view, int position, boolean isLongClick, AppCompatActivity ap) {
                if(!isLongClick){
                //    Toast.makeText(ap, "Noticia: "+listData.get(position).getTitulo(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Actividad,ScrollingActivity.class);
                intent.putExtra("title",listData.get(position).getTitulo());
                    intent.putExtra("content",listData.get(position).getContenido());
                Actividad.startActivity(intent);
                }
            }

        });
    }



    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.items, parent, false);
        return new RecyclerViewHolder(itemView,Actividad);
    }

    @Override
    public int getItemCount() {
        if(listData!=null)
        return listData.size();
        else return 0;
    }

public void changeDataItem(List<Noticia> list){
        listData = list;
        notifyDataSetChanged();
}


}
