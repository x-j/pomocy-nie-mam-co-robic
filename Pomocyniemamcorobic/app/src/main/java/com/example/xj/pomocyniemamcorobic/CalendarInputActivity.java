package com.example.xj.pomocyniemamcorobic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class CalendarInputActivity extends AppCompatActivity {

    String NICKNAME;
    RelativeLayout relativeLayout;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;
    int counter;
    MyDialog myDialog;

    public MyCalendar CALENDAR;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        counter = 0;

        relativeLayout = (RelativeLayout) findViewById(R.id.calendar_layout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        NICKNAME = this.getIntent().getExtras().getString("NICKNAME");

        setContentView(R.layout.activity_calendar_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle("zalogowano jako " + NICKNAME);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Toast.makeText(this, "Hello, " + NICKNAME, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResume() {
        super.onResume();
        getFromCalendar();

        counter++;
    }

    public void button_onClick(View view) {

        Intent intent = new Intent(getBaseContext(), ActivityDetailsEntryActivity.class);
        startActivityForResult(intent, RESULT_OK);

    }

    public void getFromCalendar() {

        new GetBlocksTask().execute(MyCalendar.nickname);

    }

    public void findWindowsButton_onClick(View view) {

        Bundle b = new Bundle();
        b.putString("nickname", NICKNAME);
        myDialog = MyDialog.createInstance(this, NICKNAME);
        myDialog.setArguments(b);

        myDialog.show(getSupportFragmentManager(), "myDialog");

    }

    public void calendarSendButton_onClick(View view) {



    }

    public void szukajButton_onClick(View view) {

        String user2 = myDialog.getUsername();
        user2 = user2.trim();
        findWindows(user2);

    }

    public void findWindows(String user2) {

        new FindWindowsTask().execute(NICKNAME, user2);
    }

    public class FindWindowsTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... data) {
            String result = Communication.findWindows(data[0], data[1]);
            return result;
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(CalendarInputActivity.this, WindowsActivity.class);
            intent.putExtra("JSON", result);
            startActivity(intent);

            myDialog.dismiss();
        }
    }

    public class GetBlocksTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... data) {
            String result = Communication.getBlocks(data[0]);
            return result;
        }

        protected void onPostExecute(String result) {

            LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.calendar_verticalLayout);
            verticalLayout.removeAllViews();

            JSONObject json = null;
            try {
                json = new JSONObject(result);

                JSONArray array = json.getJSONArray("blocks");


                for (int i = 0; i < array.length(); i++) {

                    StringBuilder sb = new StringBuilder();
                    TextView mTextView = new TextView(CalendarInputActivity.this);
                    mTextView.setText(" ");
                    mTextView.setTextSize(16);
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    verticalLayout.addView(mTextView);

                    JSONObject object = array.getJSONObject(i);

                    //sb.append(object.get("description") + " @ " + object.get("location") + " :");

                    sb.append(object.get("description") + ": ");

                    String start = (String) object.get("start");
                    start = start.split(" ")[1].substring(0, 5);
                    sb.append(start);

                    sb.append(" -> ");
                    String end = (String) object.get("end");
                    String endtime = end.split(" ")[1].substring(0, 5);
                    sb.append(endtime);

                    sb.append(" dnia ");
                    sb.append(end.split(" ")[0].split("-")[1] + "-");
                    sb.append(end.split(" ")[0].split("-")[2]);
                    sb.append("\n");
                    mTextView.setText(sb.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}

