package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;


public class RecyclerAdapterForOrderDetail extends RecyclerView.Adapter<RecyclerAdapterForOrderDetail.OrderViewHolder> {
    private Context mCtx1;
    private OrderDetail[] orders;

    public RecyclerAdapterForOrderDetail(Context mCtx1, OrderDetail[] orders) {
        this.mCtx1 = mCtx1;
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view1 = inflater.inflate(R.layout.order_history_layout, parent, false);
        return new OrderViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        OrderDetail order = orders[position];
        holder.SSItem_name.setText(order.getSitemName());
        holder.SSQuantity.setText(String.format("%s pc", String.valueOf(order.getSquantity())));
        holder.SSBuyer_name.setText(order.getSbuyerName());
        holder.SSOrder_no.setText(String.format("#%s", String.valueOf(order.getSOrder_no())));
        holder.SSPrice.setText(String.format("₹ %s", String.valueOf(order.getSprice())));
    }

    @Override
    public int getItemCount() {
        return orders.length;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView SSItem_name, SSQuantity, SSBuyer_name, SSOrder_no, SSPrice;

        OrderViewHolder(View itemView) {
            super(itemView);
            SSItem_name = itemView.findViewById(R.id.SItem_name);
            SSQuantity = itemView.findViewById(R.id.SQuantity);
            SSBuyer_name = itemView.findViewById(R.id.SBuyer_name);
            SSOrder_no = itemView.findViewById(R.id.SOrderId);
            SSPrice = itemView.findViewById(R.id.SPrice);
        }
    }
}
