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

public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.ViewHolder> {

    private ArrayList<TransactionModal> trxnModalArrayList;
    private Context context;
    private DBHandler db;

    public HistoryRVAdapter(ArrayList<TransactionModal> trxnModalArrayList, Context context) {
        this.trxnModalArrayList = trxnModalArrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // on below line we are inflating our layout
        // file for our recycler view items.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_rv_item, parent, false);
        db = new DBHandler(parent.getContext());
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransactionModal modal = trxnModalArrayList.get(position);
        String check = modal.getTtype();
        if(check.equals("INCOME")){
            holder.amount.setTextColor(Color.parseColor("#009F42"));

        }
        else if(check.equals("EXPENSE")){
            holder.amount.setTextColor(Color.parseColor("#B80000"));
        }
        String amt_Val = Double.toString(modal.getAmt());
        holder.amount.setText("₹ "+amt_Val);
        String date_format = modal.getDate();
        String date = getDateText(date_format);
        holder.date.setText(date);
        String type = modal.getType();
        String message = modal.getNote();
        if(type.equals(""))
            holder.type.setVisibility(View.GONE);
        else
            holder.type.setText(type);
        if(message.equals(""))
            holder.message.setVisibility(View.GONE);
        else
            holder.message.setText(message);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirmation Alert to delete a Transaction
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setCancelable(false)
                        .setTitle("Are you sure? The transaction will be deleted.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int index = holder.getAdapterPosition();
                                TransactionModal modal = trxnModalArrayList.get(index);
                                int x = modal.getId();
                                db.deleteRow(x);
                                trxnModalArrayList.remove(holder.getAdapterPosition());
                                dialogInterface.dismiss();
                                notifyDataSetChanged();
                            }
                        })
                        .create();
                dialog.show();
            }
        });



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
        private ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views
            amount = itemView.findViewById(R.id.tvAmount);
            date = itemView.findViewById(R.id.tvDate);
            type = itemView.findViewById(R.id.tvType);
            message = itemView.findViewById(R.id.tvMessage);
            delete = itemView.findViewById(R.id.tvIcon);

        }
    }
}