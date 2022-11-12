package com.techalgo.spendplus;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Dash extends Fragment implements View.OnClickListener {
    View view_final;

    ExtendedFloatingActionButton efab;
    FloatingActionButton inc, exp;
    TextView inc_text, exp_text, total_text, inc_text_val, exp_text_val,trxn_text;
    Boolean isAllFabsVisibile;
    String date_selected;
//    String trxn="";

    public AlertDialog.Builder dialogBuilder, dialogBuilder2;
    public AlertDialog dialog;
    EditText amount, date, note, amount2, date2, note2;
    Button save1, cancel1, save2, cancel2;

    String[] inc_types = {"Salary", "Business", "Stocks", "Friends", "Others"};
    String[] exp_types = {"Food", "Travel", "Entertainment", "Bills", "Shopping"};
    String type="";

    //for Database
    DBHandler db;
    private ArrayList<TransactionModal> trxnModalArrayList, recentTransactionsList;

    //storing values
    private double income_val = 0;
    private double expense_val = 0;

    //Recycler View
    private TrxnRVAdapter trxnRVAdapter;
    private RecyclerView trxnRV;

    public Dash() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view_final = inflater.inflate(R.layout.fragment_dash, container, false);

        String trans = getArguments().getString("trxn");
        Log.d("Dash.java",trans);

        db = new DBHandler(getActivity());



        initializeViews(view_final);

        calculateTotal();

        showRecentTransactions();



        efab.setOnClickListener(this);
        inc.setOnClickListener(this);
        exp.setOnClickListener(this);
        inc_text.setOnClickListener(this);
        exp_text.setOnClickListener(this);

        return view_final;
    }

    private void initializeViews(View view) {
        efab = view.findViewById(R.id.extended_fab);
        inc = view.findViewById(R.id.add_income_fab);
        exp = view.findViewById(R.id.add_expense_fab);
        inc_text = view.findViewById(R.id.add_income);
        exp_text = view.findViewById(R.id.add_expense);
        inc_text_val = view.findViewById(R.id.income_text_val);
        exp_text_val = view.findViewById(R.id.expense_text_val);
        total_text = view.findViewById(R.id.total_balance);
        trxn_text = view.findViewById(R.id.trxn_found);

        inc.setVisibility(View.GONE);
        exp.setVisibility(View.GONE);
        inc_text.setVisibility(View.GONE);
        exp_text.setVisibility(View.GONE);

        isAllFabsVisibile = false;

        efab.shrink();

    }

    private void showRecentTransactions() {
        //recent transactions;

        recentTransactionsList = db.readRecentTransactions();
        int size = recentTransactionsList.size();

        if(size>0){
            trxn_text.setVisibility(View.GONE);
        }
        else{
            trxn_text.setVisibility(View.VISIBLE);
        }


        trxnRVAdapter = new TrxnRVAdapter(recentTransactionsList, getActivity());
        trxnRV = view_final.findViewById(R.id.recyclerView);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        trxnRV.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        trxnRV.setAdapter(trxnRVAdapter);

    }

    private void calculateTotal() {

        trxnModalArrayList = db.readTransactions();

        int size = trxnModalArrayList.size();
        income_val=0;
        expense_val=0;
        for(int i=0;i<size;i++){
            String mType = trxnModalArrayList.get(i).getTtype();
            if(mType.equals("INCOME")){
                double x = trxnModalArrayList.get(i).getAmt();
                income_val+=x;
            }
            else if(mType.equals("EXPENSE")){
                double x = trxnModalArrayList.get(i).getAmt();
                expense_val+=x;
            }
        }
        if(income_val>0){
            String inc_val = String.valueOf(income_val);
            inc_text_val.setText("₹ "+inc_val);
        }else{
            inc_text_val.setText("₹ 0");
        }
        if(expense_val>0){
            String exp_val = String.valueOf(expense_val);
            exp_text_val.setText("₹ "+exp_val);
        }else{
            exp_text_val.setText("₹ 0");
        }

        double total_val = income_val-expense_val;
        if(total_val==0){
            total_text.setText("₹ 0");
        }
        else{
            String tot_val=String.valueOf(total_val);
            total_text.setText("₹ "+tot_val);
        }
        //calculate income,expense,total
    }

    private void extendedFAB() {
        if (!isAllFabsVisibile) {

            inc.show();
            exp.show();
            inc_text.setVisibility(View.VISIBLE);
            exp_text.setVisibility(View.VISIBLE);

            efab.extend();

            isAllFabsVisibile = true;
        } else {
            inc.hide();
            exp.hide();
            inc_text.setVisibility(View.GONE);
            exp_text.setVisibility(View.GONE);

            efab.shrink();

            isAllFabsVisibile = false;
        }
    }

    private void addExpense(View view) {
        dialogBuilder2 = new AlertDialog.Builder(getActivity());
        final View addExpense = getLayoutInflater().inflate(R.layout.add_expense_dialog, null);
        amount2 = (EditText) addExpense.findViewById(R.id.amt2);
        date2 = (EditText) addExpense.findViewById(R.id.date2);
        note2 = (EditText) addExpense.findViewById(R.id.note2);
        save2 = (Button) addExpense.findViewById(R.id.save2);
        cancel2 = (Button) addExpense.findViewById(R.id.cancel2);
        Spinner spin2 = addExpense.findViewById(R.id.type2);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        type = exp_types[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter ad2 = new ArrayAdapter(getActivity(), R.layout.spinner_item, exp_types);
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(ad2);

        dialogBuilder2.setView(addExpense);
        dialog = dialogBuilder2.create();
        dialog.show();


        //date
        date2.setFocusable(false);
        date2.setClickable(true);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build());
        builder.setTitleText("SELECT A DATE");
        builder.setPositiveButtonText("OK");
        final MaterialDatePicker materialDatePicker = builder.build();

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                date_selected = materialDatePicker.getHeaderText();
                date2.setText(date_selected);
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean res = addTrxn_Exp();
                if(res) {
                    calculateTotal();
                    dialog.dismiss();
                    extendedFAB();
                    showRecentTransactions();
                    Toast.makeText(getActivity(), "Transaction saved successfully !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void addIncome(View view) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View addIncome = getLayoutInflater().inflate(R.layout.add_income_dailog, null);
        amount = (EditText) addIncome.findViewById(R.id.amt1);
        date = (EditText) addIncome.findViewById(R.id.date);
        note = (EditText) addIncome.findViewById(R.id.note);
        save1 = (Button) addIncome.findViewById(R.id.savei);
        cancel1 = (Button) addIncome.findViewById(R.id.canceli);
        Spinner spin1 = addIncome.findViewById(R.id.type);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        type=inc_types[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter ad1 = new ArrayAdapter(getActivity(), R.layout.spinner_item, inc_types);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(ad1);

        dialogBuilder.setView(addIncome);
        dialog = dialogBuilder.create();
        dialog.show();


        //date button Calendar builder
        date.setFocusable(false);
        date.setClickable(true);

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build());
        builder.setTitleText("SELECT A DATE");
        builder.setPositiveButtonText("OK");
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
                date_selected = materialDatePicker.getHeaderText();
                date.setText(date_selected);
            }
        });

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("check","save clicked");

                boolean res = addTrxn_Inc();
                if(res) {
                    dialog.dismiss();
                    calculateTotal();
                    extendedFAB();
                    showRecentTransactions();
                    Toast.makeText(getActivity(), "Transaction saved successfully !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_income_fab:
                addIncome(view);
                break;
            case R.id.add_expense_fab:
                addExpense(view);
                break;
            case R.id.extended_fab:
                extendedFAB();
                break;
            case R.id.add_income:
                addIncome(view);
                break;
            case R.id.add_expense:
                addExpense(view);
                break;
        }
    }

    private boolean addTrxn_Inc() {
        boolean res = true;
        String amt_inc = amount.getText().toString();
        String date_inc = date.getText().toString();

        String note_inc = note.getText().toString();
        double amt_val;
        if(amt_inc.equals("")){
            Toast.makeText(getActivity(), "Please enter the amount", Toast.LENGTH_SHORT).show();
            res=false;
        }
        else if(date_inc.equals("")){
            Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
            res=false;
        }
        else {
            amt_val = Double.parseDouble(amt_inc);
            String date_format = getDateFromat(date_inc);
            db.addTrxn(amt_val, type, date_format, note_inc, "INCOME");
        }
        return res;

    }

    private String getDateFromat(String date_temp) {
        date_temp = date_temp.replaceAll(",","");
        String[] splt = date_temp.split(" ");
        String[] mon = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String mon_t="";
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
        return res;

    }

    private boolean addTrxn_Exp() {
        boolean res = true;
        String amt_exp = amount2.getText().toString();
        String date_exp = date2.getText().toString();
        String note_exp = note2.getText().toString();
        double amt_val;
        if(amt_exp==""){
            Toast.makeText(getActivity(), "Please enter the amount", Toast.LENGTH_SHORT).show();
            res=false;
        }
        else if(date_exp.equals("")){
            Toast.makeText(getActivity(), "Please select date", Toast.LENGTH_SHORT).show();
            res=false;
        }
        else {
            amt_val = Double.parseDouble(amt_exp);
            String date_format = getDateFromat(date_exp);
            db.addTrxn(amt_val,type,date_format,note_exp,"EXPENSE");
        }
        return res;
    }
}