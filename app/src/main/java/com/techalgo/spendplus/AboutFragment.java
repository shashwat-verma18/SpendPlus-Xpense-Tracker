package com.techalgo.spendplus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AboutFragment extends Fragment {
    String email,body;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        EditText sender = view.findViewById(R.id.email);
        EditText message = view.findViewById(R.id.body);
        Button send = view.findViewById(R.id.sendQuery);
        ImageView address = view.findViewById(R.id.address);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = sender.getText().toString();
                body = message.getText().toString();
                Log.d("body",body);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"contact.techalgo@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Query from SpendPlus");
                i.putExtra(Intent.EXTRA_TEXT   , body);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.google.com/maps/place/Bengaluru,+Karnataka/@12.9533684,77.0565375,10z/data=!3m1!4b1!4m5!3m4!1s0x3bae1670c9b44e6d:0xf8dfc3e8517e4fe0!8m2!3d12.9715987!4d77.5945627"));
                startActivity(intent);
            }
        });

        return view;
    }


}