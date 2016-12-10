package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyers.db_software_incorporateion.db_flyers.eventbus.FlyerArrayBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by House on 11/26/2016.
 */

public class FlyerAdapter extends RecyclerView.Adapter<FlyerAdapter.ItemRowHolder> {

    private final Activity context;

    private final ArrayList<String> expiredate;
    private final ArrayList<String> h3tag ;

    private final ArrayList<String> flyerimagetag ;
    private final ArrayList<String> gurlUpdate2;
    private final String title;

    public FlyerAdapter(Activity context, ArrayList<String> expiredate, ArrayList<String> h3tag, ArrayList<String> flyerimagetag, ArrayList<String> gurlUpdate2, String title) {
        this.context = context;
        this.expiredate = expiredate;
        this.h3tag = h3tag;
        this.flyerimagetag = flyerimagetag;
        this.gurlUpdate2 = gurlUpdate2;
        this.title = title;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_flyer_adapter, null);;
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }


    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {

//        Glide.with(context).load(R.drawable.ic_menu_camera).priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200).fitCenter().into(holder.itemImage);
        System.out.println("what up" + flyerimagetag.get(i));

        if(!flyerimagetag.isEmpty()) {
            holder.tvTitle.setText(h3tag.get(i));
            Glide.with(context).load(flyerimagetag.get(i)).priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).override(2000, 2000).fitCenter().into(holder.itemImage);

        }
    }

    @Override
    public int getItemCount(){
        return (null != flyerimagetag ? flyerimagetag.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {


        protected ImageView itemImage;
        protected TextView tvTitle;


        public ItemRowHolder(View view) {
            super(view);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImageflyer);
            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int i = getAdapterPosition();



                    Bundle low = new Bundle();
                    low.putSerializable("gurl", gurlUpdate2);

                    Bundle bpos = new Bundle();
                    bpos.putInt("bpos",i);

                    Bundle btitle = new Bundle();
                    btitle.putString("btitle",title);

                    Bundle bexpire = new Bundle();
                    bexpire.putSerializable("expire",expiredate);

                    Intent intent = new Intent(v.getContext(),ViewPagerActivity.class);
                    intent.putExtra("low",low);
                    intent.putExtra("bpos",bpos);
                    intent.putExtra("btitle",btitle);
                    intent.putExtra("bexpire",bexpire);


                    v.getContext().startActivity(intent);

                }
            });
        }
    }
}
