package com.example.xj.pomocyniemamcorobic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

public class CalendarInputActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;
    int counter;
    UsernamePromptDialog myDialog;
    String friend;
    HateMailNotification hateMailNotification;

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
        MyCalendar.nickname = this.getIntent().getExtras().getString("NICKNAME");

        setContentView(R.layout.activity_calendar_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle("zalogowano jako " + MyCalendar.nickname);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Toast.makeText(this, "Hello, " + MyCalendar.nickname, Toast.LENGTH_SHORT).show();




    }

    private void lookForHateMail() {

        //new CheckHateMailTask().execute(MyCalendar.nickname);

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
        lookForHateMail();

    }

    public void getFromCalendar() {

        new GetBlocksTask().execute(MyCalendar.nickname);

    }

    public void findWindowsButton_onClick(View view) {

        Bundle b = new Bundle();
        b.putString("nickname", MyCalendar.nickname);
        myDialog = UsernamePromptDialog.createInstance(this, MyCalendar.nickname);
        myDialog.setArguments(b);

        myDialog.show(getSupportFragmentManager(), "myDialog");

    }

    public void calendarSendButton_onClick(View view) {
        Toast.makeText(this, "Ta funkcjonalność dostępna jest TYLKO w wersji premium.", Toast.LENGTH_SHORT).show();

        //jk tak serio to nie

//        CalendarProvider
    }

    public void szukajButton_onClick(View view) {

        String user2 = myDialog.getUsername();
        user2 = user2.trim();
        findWindows(user2);
        friend = user2;

    }

    public void findWindows(String user2) {

        new FindWindowsTask().execute(MyCalendar.nickname, user2);
    }

    public void otherponiechajbutton_onClick(View view) {

        hateMailNotification.dismiss();

    }

    public void akceptujButton_onClick(View view) {

        Toast.makeText(this, "Brawo, akceptowałeś/aś.", Toast.LENGTH_SHORT).show();


    }

    public class FindWindowsTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... data) {
            String result = Communication.findWindows(data[0], data[1]);
            return result;
        }

        protected void onPostExecute(String result) {
            Intent intent = new Intent(CalendarInputActivity.this, WindowsActivity.class);
            intent.putExtra("JSON", result);
            intent.putExtra("FRIEND", friend);
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

                    sb.append(", czas trwania: "+object.get("length").toString().substring(0, 4) +", ");

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
    }

    public class CheckHateMailTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... data) {
            String result = Communication.checkHateMail(data[0]);
            return result;
        }

        protected void onPostExecute(String result) {

            try {
                JSONObject json = new JSONObject(result);

                friend = (String) json.get("friend");
                JSONObject event = (JSONObject) json.get("event");
                String hateMail = json.getString("hate_mail");

                hateMailNotification = HateMailNotification.createInstance(CalendarInputActivity.this, friend, hateMail, event);
                hateMailNotification.show(getSupportFragmentManager(), "hateMailNotification");
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}

