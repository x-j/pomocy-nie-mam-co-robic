package com.example.xj.pomocyniemamcorobic;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WindowsActivity extends AppCompatActivity {

    String friend;
    HateMailPromptDialog promptDialog;
    JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows);


        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.verticalLayout);

        String rawJson = getIntent().getExtras().getString("JSON");
        friend = getIntent().getExtras().getString("FRIEND");

        promptDialog = HateMailPromptDialog.createInstance(this, friend);

        try {
            JSONObject json = new JSONObject(rawJson);

            array = json.getJSONArray("windows");

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < array.length(); i++) {

                TextView mTextView = new TextView(this);
                mTextView.setText(" ");
                mTextView.setTag(i);
                mTextView.setLongClickable(true);
                mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        promptDialog.show(getSupportFragmentManager(), "hateMailPrompt");

                        return true;
                    }
                });
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

                sb.append(", czas trwania: " + object.get("length").toString().substring(0, 4) + ", ");

                sb.append("\n dnia ");
                sb.append(end.split(" ")[0].split("-")[1] + "-");
                sb.append(end.split(" ")[0].split("-")[2]);
                sb.append("\n");
                mTextView.setText(sb.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void dalejButton_onClick(View view) {

        JSONObject jsonObject = null;

        TextView view1 = (TextView) view;

        int index = 0;

        try {
            jsonObject = array.getJSONObject(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendHateMailTask().execute(MyCalendar.nickname, friend, jsonObject.toString(), promptDialog.getHateMail());

    }

    public void poniechajButton_onClick(View view) {
        promptDialog.dismiss();
    }

    private class SendHateMailTask extends AsyncTask<Object, Boolean, Boolean> {

        protected Boolean doInBackground(Object... data) {
            boolean result = false;
            try {
                result = Communication.sendHateMail(((String) data[0]), ((String) data[1]), new JSONObject((String) data[2]), ((String) data[3]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Boolean result) {

            WindowsActivity.this.promptDialog.dismiss();

            Toast.makeText(WindowsActivity.this, "Wysłano!", Toast.LENGTH_SHORT).show();

        }
    }

}
