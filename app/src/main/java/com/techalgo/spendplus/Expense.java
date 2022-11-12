package com.techalgo.spendplus;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Expense extends Fragment {

    Button date;
    TextView selected, trxn_text;
    FloatingActionButton search;
    Boolean check;
    String start_date,end_date;
    View view_final;

    MaterialDatePicker.Builder builder;

    DBHandler db;
    private ArrayList<TransactionModal> expenseTransactionList;

    //Recycler View
    private TrxnRVAdapter trxnRVAdapter;
    private RecyclerView trxnRV;

    public Expense() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view_final = inflater.inflate(R.layout.fragment_expense, container, false);

        initializeViews(view_final);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        builder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build());
        builder.setTitleText("SELECT A START DATE");
        builder.setPositiveButtonText("Next");
        final MaterialDatePicker materialDatePicker = builder.build();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });


        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                start_date=materialDatePicker.getHeaderText();
                date.setVisibility(View.INVISIBLE);
                selected.setVisibility(View.VISIBLE);
                selected.setText(start_date+" - ");
                MaterialDatePicker.Builder builder2 = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build());
                builder2.setTitleText("SELECT AN END DATE");
                final MaterialDatePicker materialDatePicker2 = builder2.build();

                materialDatePicker2.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");

                materialDatePicker2.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        end_date=materialDatePicker2.getHeaderText();
                        if(validateDate(start_date,end_date)) {
                            showTransactions(start_date,end_date);
                            search.show();
                            selected.setText(start_date + " - " + end_date);
                        }
                        else{
                            Toast.makeText(getActivity(), "Select Appropriate Range", Toast.LENGTH_SHORT).show();
                            date.setVisibility(View.VISIBLE);
                            selected.setVisibility(View.INVISIBLE);
                            search.hide();
                        }
                    }
                });

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });


        return view_final;
    }

    private void initializeViews(View view) {
        date = view.findViewById(R.id.select_date2);
        selected = view.findViewById(R.id.dispaly_Date2);
        search = view.findViewById(R.id.seacrh2);
        trxn_text=view.findViewById(R.id.no_trxn_exp);

        check = false;
        selected.setVisibility(View.GONE);
        search.hide();

        db = new DBHandler(getActivity());
    }

    private void showTransactions(String start_date, String end_date) {
        String start = getDateFormat(start_date);
        String end = getDateFormat(end_date);
        expenseTransactionList = db.readExpenseTransactions(start,end);
        int size = expenseTransactionList.size();

        if(size>0){
            trxn_text.setVisibility(View.GONE);
        }
        else{
            trxn_text.setVisibility(View.VISIBLE);
        }


        trxnRVAdapter = new TrxnRVAdapter(expenseTransactionList, getActivity());
        trxnRV = view_final.findViewById(R.id.recyclerView_exp);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        trxnRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        trxnRV.setAdapter(trxnRVAdapter);
    }

    private String getDateFormat(String date_temp) {
        Log.d("date_temp",date_temp);
        date_temp = date_temp.replaceAll(",","");
        String[] splt = date_temp.split(" ");
        String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String mon_t="0";
        for(int i=0;i<mon.length;i++){
            if(splt[0].equals(mon[i])){
                int x = i+1;
                mon_t=String.valueOf(x);
                if(mon_t.length()==1)
                    mon_t="0"+mon_t;
            }
        }
        String year = splt[2];
        String date = splt[1];
        if(date.length()==1)
            date="0"+date;
        String res=year+"-"+mon_t+"-"+date;
        Log.d("Expense",res);
        return res;
    }
    private boolean validateDate(String start_date, String end_date){
        start_date = start_date.replaceAll(",","");
        end_date = end_date.replaceAll(",","");
        String[] start = start_date.split(" ");
        String[] end = end_date.split(" ");
        String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        boolean res=false;
        int ind1 = 0;
        int ind2=0;
        for(int i=0;i<mon.length;i++){
            if(start[0].equals(mon[i])){
                ind1=i;
            }
            if(end[0].equals(mon[i])){
                ind2=i;
            }
        }
        int y1 = Integer.parseInt(start[2]);
        int y2 = Integer.parseInt(end[2]);
        if(y1<=y2){
            if(y1==y2) {
                if (ind1 <= ind2) {
                    if(ind1==ind2) {
                        int d1 = Integer.parseInt(start[1]);
                        int d2 = Integer.parseInt(end[1]);
                        if (d1 < d2)
                            res = true;
                    }
                    else
                        res=true;
                }
            }
            else
                res=true;
        }
        return res;
    }
}