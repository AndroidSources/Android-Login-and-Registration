package com.androidsources.loginandregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidsources.loginandregistration.app.AppConfig;
import com.androidsources.loginandregistration.app.AppController;
import com.androidsources.loginandregistration.app.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gowthamandroiddeveloperlogin Chandrasekar on 04-09-2015.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    Button registerHere;
    Button signIn;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;

    private ProgressDialog progressDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //initializing toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        //initializing views
        registerHere=(Button)findViewById(R.id.registerhere_button);
        signIn=(Button)findViewById(R.id.signin_button);
        emailLogin=(TextInputLayout)findViewById(R.id.email_loginlayout);
        passwordLogin=(TextInputLayout)findViewById(R.id.password_loginlayout);
        etEmailLogin = (EditText) findViewById(R.id.email_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);
        //setting onclick listeners
        registerHere.setOnClickListener(this);
        signIn.setOnClickListener(this);

        //setting progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        session = new SessionManager(getApplicationContext());


        //If the session is logged in move to MainActivity
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * function to verify login details
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String userId= jObj.getString("uid");

                    if (userId!=null) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Launching  main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // login error
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Post params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag", "login");
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to  queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*
    function to show dialog
     */
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /*
    function to hide dialog
     */
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            //on clicking register button move to Register Activity
            case R.id.registerhere_button:
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(intent);
                finish();
                break;

            //on clicking the signin button check for the empty field then call the checkLogin() function
            case R.id.signin_button:
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                // Check for empty data
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // show snackbar to enter credentials
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }
}
