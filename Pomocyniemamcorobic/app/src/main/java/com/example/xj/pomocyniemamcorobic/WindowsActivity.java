package com.example.xj.pomocyniemamcorobic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WindowsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows);

        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.verticalLayout);

        String rawJson = getIntent().getExtras().getString("JSON");
        try {
            JSONObject json = new JSONObject(rawJson);

            JSONArray array = json.getJSONArray("windows");

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length(); i++) {

                TextView mTextView = new TextView(this);
                mTextView.setText(" ");
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                verticalLayout.addView(mTextView);

                JSONObject object = array.getJSONObject(i);
                String start = (String) object.get("start");
                start = start.split(" ")[1].substring(0, 5);
                sb.append(start);

                sb.append(" -> ");
                String end = (String) object.get("end");
                String endtime = end.split(" ")[1].substring(0, 5);
                sb.append(endtime);

                sb.append(" dnia ");
                sb.append(end.split(" ")[0].split("-")[1]);
                sb.append(end.split(" ")[0].split("-")[2]);
                sb.append("\n");
                mTextView.setText(sb.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
