package com.example.xj.pomocyniemamcorobic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xj on 05/06/2016.
 */
public class MyDialog extends DialogFragment {

    private EditText mEditText;
    String nickname;
    Activity parent;

    public String getUsername() {
        return mEditText.getText().toString();
    }

    public interface UserNameListener {
        void onFinishUserDialog(String user);
    }

    public static MyDialog createInstance(Activity activity, String nickname) {
        MyDialog md = new MyDialog();

        md.parent = activity;
        md.nickname = nickname;

        return md;

    }

    // Empty constructor required for DialogFragment
    public MyDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mydialog, container);
        mEditText = (EditText) view.findViewById(R.id.friends_username_field);

        // set this instance as callback for editor action
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                UserNameListener activity = (UserNameListener) getActivity();
                activity.onFinishUserDialog(mEditText.getText().toString());
                return true;
            }
        });
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Please enter username");

        return view;
    }


}