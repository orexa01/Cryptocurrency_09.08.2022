package com.cryptocurrency_java.cryptocurrency_java;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class VolatilityChart extends Fragment implements View.OnClickListener, Postman{


    TextView view;
    Button buttonExit;
    Activity activity;
    MyTaskTable myTaskTable;
    ListView list_arr;
    String urlCrypt;
    String data = null;
    String[] Bitcoin = new String[100];
    CurrencyValue[] currencyValue = new CurrencyValue[100];
    LinearLayout linearLayout;
    ArrayList<String[]> ListCryptoValueOfDay = new ArrayList<>(100);
    public VolatilityChart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.volatility_chart, container, false);
        // Inflate the layout for this fragment
        buttonExit = view.findViewById(R.id.buttonExit);
        list_arr = view.findViewById(R.id.list_arr);
        buttonExit.setOnClickListener(this);
        linearLayout = view.findViewById(R.id.linearLayout);
        Init();
//        myTaskTable.execute("https://www.vbr.ru/crypto/btc/");
        return view;
    }

    public void Init(){
        for(int i = 0; i < 100; i++) currencyValue[i] = new CurrencyValue(getContext());

    }

    public void setSelectedItem(String data2) {
        view = getView().findViewById(R.id.detailsText);
//        view.setText(data);
        urlCrypt = "https://www.vbr.ru/crypto/" + data2.toLowerCase() + "/";
        Toast.makeText(
                activity.getApplicationContext(),
                        urlCrypt,
                        Toast.LENGTH_SHORT
                ).show();
        myTaskTable = new MyTaskTable();
        myTaskTable.execute(urlCrypt);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            activity =(Activity) context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonExit:
                ((Postman)activity).VolatilityChartButtonClick(true);
                break;
        }
    }

    public class MyTaskTable extends AsyncTask<String, Void, String> {
        @SuppressLint({"WrongThread", "LongLogTag"})
        @Override
        protected String doInBackground(String... params) {

            String title = null; // Тут храним значение заголовка сайта
            String title2 = null;
            Document document = null; // Здесь хранится будет разобранный HTML документ
            String Exit = null;
            String DataTime = null;
            String ValuePercent = null;
            int intBitcoin = 0;
            data = "";
            try {
                Log.d("HTML","Start");
                document = Jsoup.connect(params[0])
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                Elements paragraphs = document.select(
                        "body > div.b-center.b-center-main > main > div.rates-wrapper > div:nth-child(2) > div.rates-dynamics > div.rates-dynamics-side > div:nth-child(3) > div > table > tbody"
                ); // div.css-1wugc2s
                title2 = paragraphs.text();
                for (Element paragraph : paragraphs) {
                    ValuePercent = paragraph.getElementsByClass("rates-val").text();
                    Bitcoin[intBitcoin] = ValuePercent;
//                    title = intBitcoin + ".  " + Bitcoin[intBitcoin] + "\n";
                    data += title;
//                    Log.d("HTML VolatilityChart", title);
                    intBitcoin++;
                }
                for (Element paragraph : paragraphs) {
                    DataTime = paragraph.getElementsByClass("common-val nowrap").text();
                    Log.d("HTML currencyTime", DataTime);
                }
                SortDataTime(ParserDataTime(DataTime));
                // создаем адаптер
                Exit = "";
                String[] parser = Parser(Bitcoin);
                for(int i = 0; i < parser.length; i++){
                    if(parser[i] != null){
                        Exit += parser[i] + "\n";
                    }
                }
                Log.d("HTML Exit += parser[i]", "Exit += parser[i]");
//                Exit = data;
                // устанавливаем для списка адаптер
//                list_arr.setAdapter(adapter);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return Exit;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String[] addMass = Parser(Bitcoin);
            Log.d("HTML title", "Finish");
//            ListCryptoValueOfDay = ListItem();
            String[][] massSplitExit = Split(addMass);
            list_arr.setAdapter(ListItem());

            view.setText(result);



        }
    }

    public String[] Parser(String massParser){
        massParser = ClearStr(massParser);
        String[] massParserExit = new String[massParser.length()];
        String[] peopleArray = massParser.split("% ");
        int massParserInt = 0;

        for (String human : peopleArray) {
            massParserExit[massParserInt] = human;
            massParserInt++;
        }

        Log.d("HTML massParser", "massParserExit");
        return massParserExit;
    }

    public String[] Parser(String[] massParser){
        String massParserExit = null;

        for(int i = 0; i < massParser.length; i++){
            if(massParser[i] != null){
                massParserExit += massParser[i] + " ";
            }
        }
        Log.d("HTML massParserExit", massParserExit);
        return Parser(massParserExit);
    }

    public String[] ParserDataTime(String Parser){
        String[] massParser = new String[100];

//        int num = Parser.indexOf("в") + 5;
        int num = 18;
        int numConst = 0, numStr = 0;
        for(int i = 0; i < massParser.length; i++) massParser[i] = "";
        for(int i = 0; i < Parser.length(); i++){
            numStr++;
            if(numStr <= num) {
                massParser[numConst] = ClearStr(massParser[numConst]);
                massParser[numConst] += Parser.charAt(i);
            }
            else {
                numConst++;
                numStr = 0;
            }
        }
        return massParser;
    }

    public void SortDataTime(String[] massSplit){
        for(int i = 0; i < massSplit.length; i++){
            if(massSplit[i] != null) currencyValue[i].dataTime = massSplit[i];
            else break;
        }
    }

    public String[][] Split(String[] massSplit){

        String[][] massSplitExit = new String[massSplit.length][2];

        try {
            for(int i = 0; i < massSplit.length; i++){
                int dollar = massSplit[i].indexOf("$");
                for(int j = 0; j < massSplit[i].length(); j++){
                    if(j <= dollar) massSplitExit[i][0] += massSplit[i].charAt(j);
                    else massSplitExit[i][1] += massSplit[i].charAt(j);
                }
                massSplitExit[i][0] = ClearStr(massSplitExit[i][0]);
                massSplitExit[i][1] = ClearStr(massSplitExit[i][1]);
                currencyValue[i].value = ClearStr(massSplitExit[i][0]);
                currencyValue[i].percent = ClearStr(massSplitExit[i][1]);
            }
            Log.d("HTML title", "Finish");
        }catch (Exception e){
            e.printStackTrace();
        }


        return massSplitExit;
    }

    public String ClearStr(String strClear){
        strClear = strClear.replaceAll("null", "");
        return strClear;
    }

    public int LengthNum(Class[] mass){
        int lengthNum = 0;
        boolean flag = true;
        while (flag){
            if(mass != null) lengthNum++;
            else flag = false;
        }
        return lengthNum;
    }

    public ArrayAdapter<String> ListItem(){
        ArrayList<String[]> arrayListLarge = new ArrayList<>(100);
        String[] arrayListLargeStr = new String[3];
        for(int i = 0; i < 100; i++){
            if(currencyValue[i] != null){
                arrayListLargeStr[0] = currencyValue[i].dataTime;
                arrayListLargeStr[1] = currencyValue[i].value;
                arrayListLargeStr[2] = currencyValue[i].percent;
                arrayListLarge.add(arrayListLargeStr);
            }
            else break;
        }

        ArrayAdapter<String> arrayAdapterLarge = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                arrayListLarge
        );
        return arrayAdapterLarge;
    }

    @Override
    public void VolatilityChartButtonClick(boolean flag) {

    }


}