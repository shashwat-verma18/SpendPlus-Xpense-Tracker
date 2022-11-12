package com.techalgo.spendplus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {
    View view;
    private RecyclerView rv_history;
    private TextView no_trxn_history;

    DBHandler db;
    private ArrayList<TransactionModal> transactionsList;
    private HistoryRVAdapter historyRVAdapter;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_history, container, false);

        initializeView(view);

        showTransactons();

        return view;
    }

    private void showTransactons() {
        transactionsList = db.readTransactions();
        int size = transactionsList.size();

        if(size>0){
            no_trxn_history.setVisibility(View.GONE);
        }
        else{
            no_trxn_history.setVisibility(View.VISIBLE);
        }

        historyRVAdapter = new HistoryRVAdapter(transactionsList, getActivity());
        rv_history = view.findViewById(R.id.recycler_history);

        // setting layout manager for our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rv_history.setLayoutManager(linearLayoutManager);

        // setting our adapter to recycler view.
        rv_history.setAdapter(historyRVAdapter);
    }

    private void initializeView(View view) {
        rv_history = view.findViewById(R.id.recycler_history);
        no_trxn_history = view.findViewById(R.id.history_trxn);

        db = new DBHandler(getActivity());
    }
}