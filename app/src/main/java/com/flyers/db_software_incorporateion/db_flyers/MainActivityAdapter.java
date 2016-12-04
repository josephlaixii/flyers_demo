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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flyers.db_software_incorporateion.db_flyers.eventbus.FlyerArrayBus;

import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by House on 11/26/2016.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ItemRowHolder> {

    private final Activity context;
    private final ArrayList<String> list_of_images;
    private final ArrayList<String> title;
    private final ArrayList<String> gurl;
    int i = 0;

    public MainActivityAdapter(Activity context, ArrayList<String> list_of_images, ArrayList<String> title,ArrayList<String> gurl) {
        this.context = context;
        this.list_of_images = list_of_images;
        this.title = title;
        this.gurl = gurl;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {
//        SingleItemModel singleItem = itemsList.get(i);
//
        if(!title.get(i).isEmpty() && !list_of_images.equals("")) {
            holder.itemTitle.setText(title.get(i));
            Glide.with(context).load(list_of_images.get(i)).priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 200).fitCenter().into(holder.itemImage);

        }

    }

    @Override
    public int getItemCount(){
        return (null != list_of_images ? list_of_images.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;
        protected ImageView itemImage;



        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i = getAdapterPosition();



                    Bundle low = new Bundle();
                    low.putSerializable("gurl", gurl);

                    Bundle bpos = new Bundle();
                    bpos.putInt("bpos",i);

                    Bundle btitle = new Bundle();
                    btitle.putString("btitle",title.get(i));

                    Intent intent = new Intent(v.getContext(),FlyerActivity.class);
                    intent.putExtra("low",low);
                    intent.putExtra("bpos",bpos);
                    intent.putExtra("btitle",btitle);

                    v.getContext().startActivity(intent);


                }
            });

        }
    }
}
