package com.techalgo.spendplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class TrxnRVAdapter extends RecyclerView.Adapter<TrxnRVAdapter.ViewHolder> {
        private ArrayList<TransactionModal> trxnModalArrayList;
        private Context context;
        private DBHandler db;

        // constructor
        public TrxnRVAdapter(ArrayList<TransactionModal> trxnModalArrayList, Context context) {
            this.trxnModalArrayList = trxnModalArrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // on below line we are inflating our layout
            // file for our recycler view items.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trxn_rv_item, parent, false);
            db = new DBHandler(parent.getContext());
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // on below line we are setting data
            // to our views of recycler view item.

            TransactionModal modal = trxnModalArrayList.get(position);
            String check = modal.getTtype();
            if(check.equals("INCOME")){
                holder.amount.setTextColor(Color.parseColor("#009F42"));
                holder.type.setTextColor(Color.parseColor("#009F42"));

            }
            else if(check.equals("EXPENSE")){
                holder.amount.setTextColor(Color.parseColor("#B80000"));
                holder.type.setTextColor(Color.parseColor("#B80000"));
            }
            String amt_Val = Double.toString(modal.getAmt());
            holder.amount.setText("₹ "+amt_Val);
            String date_format = modal.getDate();
            String date = getDateText(date_format);
            holder.date.setText(date);
            String type = modal.getType();
            holder.type.setText(type);
            //setting type icon
            switch (type){
                case "Salary":
                    holder.icon.setBackgroundResource(R.drawable.salary);
                    break;
                case "Business":
                    holder.icon.setBackgroundResource(R.drawable.business);
                    break;
                case "Stocks":
                    holder.icon.setBackgroundResource(R.drawable.stocks);
                    break;
                case "Friends":
                    holder.icon.setBackgroundResource(R.drawable.friends);
                    break;
                case "Others":
                    holder.icon.setBackgroundResource(R.drawable.others);
                    break;
                case "Food":
                    holder.icon.setBackgroundResource(R.drawable.food);
                    break;
                case "Travel":
                    holder.icon.setBackgroundResource(R.drawable.travel);
                    break;
                case "Entertainment":
                    holder.icon.setBackgroundResource(R.drawable.entertainment);
                    break;
                case "Bills":
                    holder.icon.setBackgroundResource(R.drawable.bill);
                    break;
                case "Shopping":
                    holder.icon.setBackgroundResource(R.drawable.shopping);
                    break;

            }

            holder.message.setText(modal.getNote());


        }

    private String getDateText(String date_format) {
            String res="";
            String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
            String[] splt = date_format.split("-");
            int ind = Integer.parseInt(splt[1]);
            ind--;
            res=res+mon[ind]+" ";
            res=res+splt[2]+", ";
            res+=splt[0];
            return res;

    }

    @Override
        public int getItemCount() {
            // returning the size of our array list
            return trxnModalArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            // creating variables for our text views.
            private TextView amount,date,type,message;
            private ImageView icon;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // initializing our text views
                amount = itemView.findViewById(R.id.tvAmount);
                date = itemView.findViewById(R.id.tvDate);
                type = itemView.findViewById(R.id.tvType);
                message = itemView.findViewById(R.id.tvMessage);
                icon = itemView.findViewById(R.id.tvIcon);
            }
        }
}
