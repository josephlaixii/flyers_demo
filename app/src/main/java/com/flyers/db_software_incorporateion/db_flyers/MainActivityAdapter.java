package com.flyers.db_software_incorporateion.db_flyers;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by House on 11/26/2016.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ItemRowHolder> {

    private final Activity context;
    private final ArrayList<String> list_of_images;
    private final String url;

    public MainActivityAdapter(Activity context, ArrayList<String> list_of_images, String url) {
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
//        SingleItemModel singleItem = itemsList.get(i);
//
        System.out.println("list_of_images: " + list_of_images +"\n" + url);
        //holder.itemTitle.setText(url);
        System.out.println("This is the size" + list_of_images.size());

        if(!url.equals("") && !list_of_images.isEmpty()) {

            Glide.with(context).load(list_of_images.get(i)).priority(Priority.IMMEDIATE).diskCacheStrategy(DiskCacheStrategy.ALL).override(2000, 2000).fitCenter().into(holder.itemImage);

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
            this.itemTitle = (TextView) view.findViewById(R.id.text);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


        }
    }
}
