package com.example.alternative_admin.osmproject;

/**
 * Created by alternative-admin on 2017/6/12.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Map extends Activity {


    private Button NextPage;
    private Button GetInfo;
    private TextView Info;
    String title;
    String url = "http://www.cwb.gov.tw/V7/forecast/taiwan/Tainan_City.htm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        NextPage = (Button) findViewById(R.id.next);
        GetInfo = (Button) findViewById(R.id.GetInfo);
        Info = (TextView) findViewById(R.id.WebInfo);
        NextPage.setOnClickListener(new button_set());
        GetInfo.setOnClickListener(new button_set());

    }

    private class button_set implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.next) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(Map.this, MainActivity.class);
                startActivity(intent);
            } else if (v.getId() == R.id.GetInfo) {
                //Info.setText("New Info From WebPage");
                new Thread(runnable).start();
            }

        }

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements weather = doc.select("td");
                title = weather.get(0).text();
            } catch (IOException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Info.setText(title);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

