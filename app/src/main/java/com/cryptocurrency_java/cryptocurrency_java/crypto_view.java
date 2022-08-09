package com.cryptocurrency_java.cryptocurrency_java;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Random;


public class crypto_view extends Fragment implements View.OnClickListener {

    public Context context;
    private int value;
    public int limit;
    LinearLayout main_layout;
    Random rnd = new Random();
    public TextView name_crypto_current, value_crypto_current, day_crypto, week_crypto;
    ImageView imageCrypto;
    String BTC = null;

    String[][] cryptoValueName = {
            {"BTC|Bitcoin", "35 USD", "1.86%", "-2.01%"},
            {"ETH|Ethereum", "100 USD", "2.45%", "-2.01%"},
            {"XRP|Ripple", "3 USD", "7.27%", "-2.01%"},
            {"LINK|Chainlink", "203 USD", "-0.58%", "-2.01%"},
            {"XLM|Stellar", "46 USD", "-4.02%", "1.01%"}
    };

    interface OnFragmentSendDataListener {
        void onSendText(String text);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    private OnFragmentSendDataListener fragmentSendDataListener;

    public crypto_view(int value) {
        this.value = value;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto_view, container, false);
        Init(view);
        return view;
    }

    private void Init(View view){
        name_crypto_current = (TextView) view.findViewById(R.id.name_crypto_current);
        value_crypto_current = (TextView) view.findViewById(R.id.value_crypto_current);
        day_crypto = (TextView) view.findViewById(R.id.day_crypto);
        week_crypto = (TextView) view.findViewById(R.id.week_crypto);
        main_layout = view.findViewById(R.id.main_layout);
        imageCrypto = view.findViewById(R.id.imageCrypto);
        if(value < cryptoValueName.length){
            name_crypto_current.setText(cryptoValueName[value][0]);
            value_crypto_current.setText(cryptoValueName[value][1]);
            day_crypto.setText(cryptoValueName[value][2]);
            week_crypto.setText(cryptoValueName[value][3]);
        }
        else {
            name_crypto_current.setText(cryptoValueName[rnd.nextInt(5)][0]);
            value_crypto_current.setText((Rate(rnd.nextInt(20000)) * 100) + " USD");
            day_crypto.setText(Rate(rnd.nextInt(20000)) + "%");
            week_crypto.setText(Rate(rnd.nextInt(20000)) + "%");
        }
        main_layout.setOnClickListener(this);
    }

    private float Rate(int rate){
        rate /= 100;
        rate -= 100;
        return rate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_layout:
                Toast.makeText(getActivity(), "value: " + value,
                        Toast.LENGTH_SHORT).show();
                fragmentSendDataListener.onSendText(BTC
//                        name_crypto_current.getText() + "\n"
//                                + value_crypto_current.getText() + "\n"
//                                + day_crypto.getText() + "\n"
//                                + week_crypto.getText()
                );
                break;
        }
    }

}