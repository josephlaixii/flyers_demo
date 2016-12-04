package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Activity;
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
    private final ArrayList<String> list_of_images;
    private final String url;

    public FlyerAdapter(Activity context, ArrayList<String> list_of_images, String url) {
        this.context = context;
        this.list_of_images = list_of_images;
        this.url = url;


    }


    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }


    @Override
    public void onBindViewHolder(ItemRowHolder holder, int i) {


        if(!list_of_images.isEmpty()) {

            Glide.with(context).load(list_of_images.get(i)).priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).override(1500, 2000).fitCenter().into(holder.itemImage);

        }
    }

    @Override
    public int getItemCount(){
        return (null != list_of_images ? list_of_images.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {


        protected ImageView itemImage;


        public ItemRowHolder(View view) {
            super(view);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


        }
    }
}
