package com.etuloser.padma.rohit.homework09a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Created by Rohit on 4/21/2017.
 */

public class FrdAdapter extends ArrayAdapter<User> {

    ArrayList<User> flist;
    Context mcontext;
    int mres;
    String cuid;
    int cflag;

    public FrdAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);
        this.flist=objects;
        this.mcontext=context;
        this.mres=resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(mres,parent,false);
        }
        final User g=flist.get(position);


        ImageView iv=(ImageView)convertView.findViewById(R.id.temp_frd_image);
        TextView txtroomname=(TextView)convertView.findViewById(R.id.tempfrdname);
        final Button btnjoin=(Button)convertView.findViewById(R.id.tempAddfrd);

        txtroomname.setText(g.getFirstname()+" "+g.getLastname());
        Picasso.with(mcontext).load(g.getImgurl()).placeholder(R.drawable.avatar_11_raster).into(iv);

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // if(cflag==1) {
                    ((FrdActivity) mcontext).sendfrdrequest(g);
                    notifyDataSetChanged();
                //}
                //else {

                   //}
            }
        });


        notifyDataSetChanged();

        return convertView;


    }


}
