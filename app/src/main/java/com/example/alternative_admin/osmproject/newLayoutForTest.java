package com.example.alternative_admin.osmproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Created by alternative-admin on 2017/6/19.
 */

public class newLayoutForTest extends Activity{
    private Button back;
    private Button pullData;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.pullresorce);
    }
}
