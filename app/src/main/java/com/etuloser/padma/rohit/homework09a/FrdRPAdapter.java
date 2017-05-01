package com.etuloser.padma.rohit.homework09a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rohit on 4/22/2017.
 */

public class FrdRPAdapter extends ArrayAdapter<friend> {


    ArrayList<friend> flist;
    Context mcontext;
    int mres;
    String cuid;
   // int cflag;

    public FrdRPAdapter(Context context, int resource, ArrayList<friend> objects) {
        super(context, resource, objects);
        this.flist=objects;
        this.mcontext=context;
        this.mres=resource;
    //    this.cflag=flag;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(mres,parent,false);
        }
        final friend g=flist.get(position);
        Log.d("In RP Adapter",g.getFrdname());
//            if (g.getStatus().equals("1")) {
                ImageView iv = (ImageView) convertView.findViewById(R.id.temp_frd_image);
                TextView txtroomname = (TextView) convertView.findViewById(R.id.tempfrdname);
                final Button btnjoin = (Button) convertView.findViewById(R.id.tempAddfrd);

                txtroomname.setText(g.getFrdname());
                Picasso.with(mcontext).load(g.getPimagurl()).placeholder(R.drawable.avatar_11_raster).into(iv);

                 if(g.getStatus().equals("1")) {

                        btnjoin.setText("Accept");
                 }else
                 {
                     btnjoin.setVisibility(View.INVISIBLE);
                 }

                 btnjoin.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         ((FrdActivity) mcontext).Acceptfrdrequest(g);
                         notifyDataSetChanged();

                     }
                 });


        if(g.getStatus().equals("0")) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FrdActivity)mcontext).showfrdprofile(g);
                }
            });
        }

              //  }

  //              }



        return convertView;


    }



}
