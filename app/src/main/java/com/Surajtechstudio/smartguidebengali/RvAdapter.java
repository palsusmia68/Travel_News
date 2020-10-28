package com.Surajtechstudio.smartguidebengali;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<DataModel> dataModelArrayList;
    Context context;

    public RvAdapter(Context ctx, ArrayList<DataModel> dataModelArrayList){
        this.context=ctx;
        inflater = LayoutInflater.from(ctx);
        this.dataModelArrayList = dataModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycler_view_items, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


       // Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
       /* Glide.with(context).load(dataModelArrayList.get(position).getImgURL())
                .apply(new RequestOptions().placeholder(R.drawable.logoorirginal)).error(R.drawable.logoorirginal)
                .override(400,400).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv);*/
      // Glide.with(context).load(dataModelArrayList.get(position).getImgURL()).into(holder.iv);
       RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(dataModelArrayList.get(position).getImgURL()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.iv);
       // holder.name.setText(dataModelArrayList.get(position).getName());
        holder.country.setText(dataModelArrayList.get(position).getCountry());
        //holder.city.setText(dataModelArrayList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView country, name, city;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);

            country = (TextView) itemView.findViewById(R.id.tv);
           // name = (TextView) itemView.findViewById(R.id.name);
            //city = (TextView) itemView.findViewById(R.id.city);
            iv = (ImageView) itemView.findViewById(R.id.textview1);
        }

    }
}
