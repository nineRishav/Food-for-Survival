package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductViewHolder> {
    private final Context mCtx;
    private ProductDetail[] products;
    private String userType;
    private RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    RecyclerAdapter(Context mCtx, ProductDetail[] products) {
        this.mCtx = mCtx;
        this.products = products;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.food_item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final ProductDetail product = products[position];
        holder.jTitle.setText((product.getItemName()));
        Glide.with(holder.jImageUrl.getContext())
                .load(product.getItemUrl())
                .apply(options)
                .into(holder.jImageUrl);
        holder.jPrice.setText(String.valueOf(product.getPrice()));
        holder.jQuantity.setText(String.valueOf(product.getQuantity()));

        SharedPreferences shrd = mCtx.getSharedPreferences("MyPref", 0);
        userType = shrd.getString("type", null);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (userType.equalsIgnoreCase("BUYER")) {
                            final Intent intent1_seller_list;

                            intent1_seller_list = new Intent(view.getContext(), SellerList.class);
                            intent1_seller_list.putExtra("item_name", product.getItemName());
                            intent1_seller_list.putExtra("item_url", product.getItemUrl());

                            view.getContext().startActivity(intent1_seller_list);
                        }
                        if (userType.equalsIgnoreCase("SELLER")) {
                            final Intent modify_item_intent;

                            modify_item_intent = new Intent(view.getContext(), ModifyNewItem.class);
                            modify_item_intent.putExtra("item_name", product.getItemName());
                            modify_item_intent.putExtra("quantity", String.valueOf(product.getQuantity()));
                            modify_item_intent.putExtra("price", String.valueOf(product.getPrice()));
                            modify_item_intent.putExtra("seller_name", product.getSellerName());

                            view.getContext().startActivity(modify_item_intent);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView jTitle, jPrice, jQuantity;
        ImageView jImageUrl;
        View view;

        ProductViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            jImageUrl = itemView.findViewById(R.id.media_image);
            jTitle = itemView.findViewById(R.id.title_text);
            jPrice = itemView.findViewById(R.id.price);
            jQuantity = itemView.findViewById(R.id.quantity);
        }
    }

}

