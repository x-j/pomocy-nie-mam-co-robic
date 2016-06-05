package com.example.xj.pomocyniemamcorobic;

import android.os.AsyncTask;

public class SendTimetableTask extends AsyncTask<String, Integer, Integer> {

    int RESULT;

    protected Integer doInBackground(String... data) {
        int result = Communication.sendTimetable(MyCalendar.nickname);
        return  result;
    }

    protected void onPostExecute(Integer result) {
        RESULT = result;
    }
}
