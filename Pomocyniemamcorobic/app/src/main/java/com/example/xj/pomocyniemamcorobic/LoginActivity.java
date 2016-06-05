package com.example.xj.pomocyniemamcorobic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private AutoCompleteTextView mNameField;
    private EditText mPasswordView;
    private EditText mNicknameView;
    private View mLoginFormView;
    private Switch mSwitch;

    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mNameField = (AutoCompleteTextView) findViewById(R.id.nameField);
        mPasswordView = ((EditText) findViewById(R.id.passwordField));
        mNicknameView = ((EditText) findViewById(R.id.nicknameField));
        mSwitch = (Switch) findViewById(R.id.switcher);

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
    }

    private void attemptLogin() {
// Reset errors.
        mNameField.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameField.getText().toString();
        String password = mPasswordView.getText().toString();
        nickname = mNicknameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid description
        if (!isPasswordValid(password)) {
            mNameField.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isNicknameValid(nickname)) {
            mNicknameView.setError(getString(R.string.error_field_required));
            focusView = mNicknameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (mSwitch.isChecked()) {

                new LoginTask().execute(nickname, password);

            } else {
                //registration
                if (TextUtils.isEmpty(name)) {
                    mNameField.setError(getString(R.string.error_field_required));
                    focusView = mNameField;
                    cancel = true;
                } else if (!isNameValid(name)) {
                    mNameField.setError(getString(R.string.error_invalid_email));
                    focusView = mNameField;
                } else new RegisterTask().execute(name, nickname, password);

            }
        }
    }

    private boolean isNicknameValid(String nickname) {
        nickname = nickname.trim();
        if (nickname.isEmpty()) return false;
        if (nickname.contains(" ")) return false;
        return true;
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    private boolean isNameValid(String name) {
        return name.split(" ").length == 2;
    }

    public void switcher_onClick(View view) {
        if (mSwitch.isChecked()) {
            //log in
            mNameField.setVisibility(View.INVISIBLE);
        } else {
            //register
            mNameField.setVisibility(View.VISIBLE);
        }
    }

    private class RegisterTask extends AsyncTask<String, Boolean, Boolean> {
        protected Boolean doInBackground(String... data) {
            boolean result = Communication.tryRegister(data[0], data[1], data[2]);
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (!result) {
                mNicknameView.setError(getString(R.string.error_nickname_taken));
                mNicknameView.requestFocus();
            } else {
                MyCalendar.content.clear();
                MyCalendar.nickname = nickname;

                Intent intent = new Intent(getBaseContext(), CalendarInputActivity.class);
                intent.putExtra("NICKNAME", nickname);
                startActivity(intent);
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Boolean, Boolean> {
        protected Boolean doInBackground(String... data) {
            boolean result = Communication.tryLogIn(data[0], data[1]);
            return result;
        }

        protected void onPostExecute(Boolean result) {
            if (!result) {
                mPasswordView.setError("Nie ma takiego użytkownika albo złe hasło!");
                mNicknameView.requestFocus();
            } else {
                MyCalendar.content.clear();
                MyCalendar.nickname = nickname;

                Intent intent = new Intent(getBaseContext(), CalendarInputActivity.class);
                intent.putExtra("NICKNAME", nickname);
                startActivity(intent);
            }
        }
    }
}

