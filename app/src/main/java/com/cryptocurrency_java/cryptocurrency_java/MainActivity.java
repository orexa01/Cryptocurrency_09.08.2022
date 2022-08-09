package com.cryptocurrency_java.cryptocurrency_java;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity implements crypto_view.OnFragmentSendDataListener, Postman, View.OnClickListener, ToolBar.ToolBarButtonClick{

    crypto_view[] crypto_views = new crypto_view[40];
    LinearLayout mainLinerLayout;
    androidx.fragment.app.FragmentContainerView fragmentContainerView;
    public int screenID = 0;

    Bitmap[] bitmap = new Bitmap[100];
    ImageView imageView;

    MyTask mt;

    String[] BTC = new String[100];
    String[] Bitcoin = new String[100];
    String[] BTC_Dollar = new String[100];
    String[] BTC_Euro = new String[100];
    String[] BTC_Rub = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        imageView = findViewById(R.id.image);
        mt = new MyTask();
        mt.execute("https://www.vbr.ru/crypto/");//https://https://www.binance.com/ru/trade/BTC_BUSD

        mainLinerLayout = findViewById(R.id.hi);
        fragmentContainerView = findViewById(R.id.volatility_chart);
        for (int i = 0; i < crypto_views.length; i++){
            crypto_views[i] = new crypto_view(i);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.hi, crypto_views[i], null)
                    .commit();
        }


    }

    @Override
    public void onSendText(String text) {
        VolatilityChart volatilityChart = (VolatilityChart) getSupportFragmentManager().findFragmentById(R.id.volatility_chart);
        if (volatilityChart != null) {
            GoneView(fragmentContainerView);
            volatilityChart.setSelectedItem(text);
        }
    }

    @Override
    public void VolatilityChartButtonClick(boolean flag) {
        if(flag){
            GoneView(mainLinerLayout);
        }
        else {
            GoneView(mainLinerLayout);
        }
    }

    @Override
    public void ToolBarClick(int idButtonClick) {
        switch (idButtonClick){
            case 0: GoneView(mainLinerLayout); break; // next
            case 1: alertDialog(); break; // menu
        }
    }

    public void GoneView(View view){
        fragmentContainerView.setVisibility(View.GONE);
        mainLinerLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        if(view == mainLinerLayout) screenID = 0;
        else screenID = 1;
    }

    public void alertDialog(){
        final String[] currencyList = getResources().getStringArray(R.array.currency_list);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.bitcoin)
                .setTitle(R.string.settings)
//                .setMessage("Exiting will call finish() method")
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(),"No",Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(),"Yes",Toast.LENGTH_LONG).show();

                    }
                })
                .setSingleChoiceItems(currencyList, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
//                                mt.execute("https://www.vbr.ru/crypto/");
                                Toast.makeText(getApplicationContext(), getString(R.string.currency_selected) + currencyList[item], Toast.LENGTH_SHORT)
                                        .show();

                            }
                        })
                .show();
//        try {
//            Document doc = Jsoup.connect("https://yandex.ru/")
//                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
//                    .referrer("http://www.google.com")
//                    .get();
//            Elements listNews = doc.select("div#tabnews_newsc.content-tabs__items.content-tabs__items_active_true");
//            for (Element element : listNews.select("a"))
//                Log.d("HTML:", element.text());
//            //System.out.println(element.text());
//
//            Log.d("HTML:", listNews.select("a").get(2).text());
//            //System.out.println(listNews.select("a").get(2).text());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public class MyTask extends AsyncTask<String, Void, String> {
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            // params - это массив входных параметров
            // в params[0] будет хранится адрес сайта, который мы парсим

            String title; // Тут храним значение заголовка сайта
            String cryptoName;
            String cryptoNameFinans;
            String cryptoValue;
            Elements listNews = null;
            Document document = null; // Здесь хранится будет разобранный HTML документ
            Element sampleDiv;
            String Exit = null;
//            Handler handler = new Handler();
//            final Runnable r = new Runnable() {
//                public void run() {
//                    slav_time();
//                    handler.postDelayed(this, 1000);
//                }
//            };
//            handler.postDelayed(r, 1000);

            try {
                Log.d("HTML","Start");
                document = Jsoup.connect(params[0])
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
//                Log.d("HTML title", document.title());// css-1ucpw95
                Elements paragraphs = document.select("div"); // div.css-1wugc2s

                int intBTC = 0;
                int intBitcoin = 0;
                int intBTC_Dollar = 0;
                int intBTC_Euro = 0;
                int intBTC_Rub = 0;
                for (Element paragraph : paragraphs) {
//                    Log.d("HTML paragraph", paragraph.getElementsByClass("mobile-hide").text());
                    String arr = paragraph.getElementsByClass("mobile-hide").text();
                    if(arr != ""){
                        if(arr.length() < 10){
                            BTC[intBTC] = arr;
                            intBTC++;
                        }
                    }

//                    Log.d("HTML paragraph", paragraph.getElementsByClass("mobile-show black-link").text());
                    arr = paragraph.getElementsByClass("mobile-show black-link").text();
                    if(arr != ""){
                        if(arr.length() < 40){
                            Bitcoin[intBitcoin] = arr;
                            intBitcoin++;
                        }
                    }

//                    Log.d("HTML paragraph", paragraph.getElementsByClass("rates-calc-block -big-sum").text());
                    arr = paragraph.getElementsByClass("rates-calc-block -big-sum").text();
                    if(arr != ""){
                        if(arr.length() < 15){
                            if(arr.contains("$")){
                                BTC_Dollar[intBTC_Dollar] = arr;
                                intBTC_Dollar++;
                            }
                            else if(arr.contains("€")){
                                BTC_Euro[intBTC_Euro] = arr;
                                intBTC_Euro++;
                            }
                            else if(arr.contains("₽")){
                                BTC_Rub[intBTC_Rub] = arr;
                                intBTC_Rub++;
                            }
                        }
                    }
                }

//                https://www.vbr.ru/api/currency/crypto-currency-chart/?currency=BTC&priceCurrency=RUB&interval=1

            } catch (IOException e) {
                // Если не получилось считать
                e.printStackTrace();
            }


            return /*title + "\n" +*/ Exit;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TextView textView = findViewById(R.id.textView);
//            imageView.setImageBitmap(bitmap);
            textView.setText(result);
            Log.d("HTML title", "Finish");

            for (int i = 0; i < crypto_views.length; i++){
                crypto_views[i].name_crypto_current.setText(BTC[i] + "|" + Bitcoin[i]);
                crypto_views[i].value_crypto_current.setText(BTC_Dollar[i]);
                crypto_views[i].BTC = BTC[i];
//                crypto_views[i].imageCrypto.setImageBitmap(getImageBitmap("https://cdn.viberu.ru/files/images/crypto_currency/ETH.svg"));
//                crypto_views[i].day_crypto.setText(BTC_Euro[i]);
//                crypto_views[i].week_crypto.setText(BTC_Rub[i]);
            }
        }
    }

//    public class DownImage extends AsyncTask<String, Void, Bitmap> {
//        @SuppressLint("WrongThread")
//        @Override
//        protected Bitmap doInBackground(String... params) {
//
//
//            return getImageBitmap(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//
//            for (int i = 0; i < crypto_views.length; i++){
////                crypto_views[i].name_crypto_current.setText(BTC[i] + "|" + Bitcoin[i]);
////                crypto_views[i].value_crypto_current.setText(BTC_Dollar[i]);
//                crypto_views[i].imageCrypto.setImageBitmap(getImageBitmap("https://cdn.viberu.ru/files/images/crypto_currency/ETH.svg"));
////                crypto_views[i].day_crypto.setText(BTC_Euro[i]);
////                crypto_views[i].week_crypto.setText(BTC_Rub[i]);
//            }
//
//        }
//    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("HTML TAG", "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(screenID == 0 || screenID == -1){
                if(screenID == -1) finish();
                else {
                    Toast.makeText(this, "Нажмите еще раз!", Toast.LENGTH_SHORT).show();
                    screenID = -1;
                }
            }
            else GoneView(mainLinerLayout);
        }
        return true;
    }

}