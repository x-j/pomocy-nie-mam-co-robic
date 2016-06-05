package com.example.xj.pomocyniemamcorobic;

import android.os.AsyncTask;
import android.os.Looper;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.ResponseHandler;

/**
 * Created by xj on 04/06/2016.
 */
public class Communication {

    private static final String URL = "http://192.168.43.25:8000";

    public static AsyncHttpClient syncHttpClient = new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return URL + relativeUrl;
    }

    public static boolean tryRegister(String name, String nickname, String password) {
        RequestParams params = new RequestParams();
        params.put("name", name.trim());
        params.put("nickname", nickname.trim());
        params.put("password", password.trim());

        final boolean poop[] = new boolean[1];

        BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(rawJsonResponse);
                    poop[0] = (boolean) object.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                JSONObject object = null;
                try {
                    object = new JSONObject(rawJsonData);
                    poop[0] = (boolean) object.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure) return null;
                else {
                    JSONObject jsonObject = new JSONObject(rawJsonData);
                    String raw = rawJsonData;
                    if (raw.contains("true")) poop[0] = true;
                    return (jsonObject.get("result").equals("true"));
                }
            }
        };
        Communication.post("/register/", params, baseJsonHttpResponseHandler);

        return poop[0];

    }

    public static int sendTimetable(String nickname) {

        String format = "yyyy-MM-dd HH:mm";

        RequestParams params = new RequestParams();
        try {
            params.put("nickname", nickname);
            JSONArray array = new JSONArray();
            for (Excuse e : MyCalendar.content) {
                JSONObject object = new JSONObject();
                object.put("description", e.description);
                object.put("location", e.location);
                String beginDateString = new SimpleDateFormat(format).format(e.begin);
                object.put("start_date", beginDateString);
                String endDateString = new SimpleDateFormat(format).format(e.end);
                object.put("end_date", endDateString);
                array.put(object);
            }
            params.put("event_list", array);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        final int[] result = new int[1];

        AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                result[0] = Integer.parseInt(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                result[0] = 0;
            }
        };

        post("/save_timetable/", params, responseHandler);

        return result[0];
    }

    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }

    public static String findWindows(String nickname, String user2)  {

        RequestParams params = new RequestParams();
        params.put("user1", nickname.trim());
        params.put("user2", user2.trim());

        final String[] received = new String[1];

        BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                received[0] = new String(rawJsonResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                received[0] = new String(rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new String(rawJsonData);
            }
        };
        get("/get_windows/", params, baseJsonHttpResponseHandler);

        return received[0];
    }

    public static String getBlocks(String nickname)  {

        RequestParams params = new RequestParams();
        params.put("nickname", nickname.trim());

        final String[] received = new String[1];

        BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                received[0] = new String(rawJsonResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                received[0] = new String(rawJsonData);
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new String(rawJsonData);
            }
        };
        get("/get_blocks/", params, baseJsonHttpResponseHandler);

        return received[0];
    }

    public static boolean tryLogIn(String nickname, String password) {

        RequestParams params = new RequestParams();
        params.put("nickname", nickname.trim());
        params.put("password", password.trim());


        final boolean poop[] = new boolean[1];

        BaseJsonHttpResponseHandler baseJsonHttpResponseHandler = new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(rawJsonResponse);
                    poop[0] = (boolean) object.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                JSONObject object = null;
                try {
                    object = new JSONObject(rawJsonData);
                    poop[0] = (boolean) object.get("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                if (isFailure) return null;
                else {
                    JSONObject jsonObject = new JSONObject(rawJsonData);
                    String raw = rawJsonData;
                    if (raw.contains("true")) poop[0] = true;
                    return (jsonObject.get("result").equals("true"));
                }
            }
        };
        Communication.post("/login/", params, baseJsonHttpResponseHandler);

        return poop[0];

    }
}
