package com.example.loinasd.urbandict;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //--------LOG--------------//
    final static String FULL_PAGE_TAG = "FULL PAGE";
    //final static String MESSAGE_TAG = "MESSAGE TAG";
    final static String ACT_TAG = "ACT TAG";
    static Parser p;

    //-------------------------//

    TextView tNameView, tMeaningView, tExampleView, tRibbonView;
    EditText mainSearch;
    Button searchButton;
    LoadUrl lu;
    LinearLayout root, searchBar, linLayout;
    LayoutInflater itemInflater;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemInflater = getLayoutInflater();

        mainSearch = (EditText) findViewById(R.id.SearchField);
        searchButton = (Button) findViewById(R.id.searchButton);
        linLayout = (LinearLayout) findViewById(R.id.linLayout);
        root = (LinearLayout) findViewById(R.id.rootLayout);
        searchBar = (LinearLayout) findViewById(R.id.searchBarLayout);



        //---Listener -> Activity----//
        searchButton.setOnClickListener(this);
        mainSearch.setOnClickListener(this);
        //---------------------------//

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.searchButton) {

            p = new Parser();
            searchButton.clearFocus();
            lu = new LoadUrl(p, mainSearch.getText().toString());
            lu.execute();

        }

        if (v.getId() == R.id.SearchField){
            mainSearch.getText().clear();
        }
    }

    private class LoadUrl extends AsyncTask<Void, Void, Void> {

        private static final String ROOT_URL = "http://www.urbandictionary.com";
        public BufferedReader r;
        private Parser parser;
        private String request, token;

        LoadUrl(Parser p, String token){
            this.parser = p;
            this.token = token;
            this.request = "search.php?term=";
        }

        private URL toURL(String request, String token) throws MalformedURLException {
            return new URL(ROOT_URL + '/' + request + token.trim().replace(' ', '+'));
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            searchButton.setClickable(false);
            linLayout.removeAllViews();
        }

        @Override
        protected Void doInBackground(Void... params) {
            URL req = null;
            try {
                req = toURL(this.request, this.token);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection con = null;
            try {
                if (req != null) {
                    con = req.openConnection();
                } else System.out.println("req id null!");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                assert con != null;
                r = new BufferedReader( new InputStreamReader(con.getInputStream(), "UTF-8"));
                parser.setBufferedReader(r);
                parser.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            for (Item item : parser.getItems()) {

                View v = itemInflater.inflate(R.layout.item, linLayout, false);

                tRibbonView = (TextView) v.findViewById(R.id.RibbonVIew);

                tNameView = (TextView) v.findViewById(R.id.NameView);
                tMeaningView = (TextView) v.findViewById(R.id.MeaningView);
                tExampleView = (TextView) v.findViewById(R.id.ExampleView);

                tRibbonView.setText(item.getRibbon());
                tNameView.setText(item.getName());
                tMeaningView.setText(item.getMeaning());
                tExampleView.setText(item.getExample());

                if (item.getRibbon().isEmpty()) tRibbonView.setVisibility(View.GONE);
                if (item.getName().isEmpty()) tNameView.setVisibility(View.GONE);
                if (item.getMeaning().isEmpty()) tMeaningView.setVisibility(View.GONE);
                if (item.getExample().isEmpty()) tExampleView.setVisibility(View.GONE);

                linLayout.addView(v);

                searchButton.setClickable(true);
            }



        }

    }

}
