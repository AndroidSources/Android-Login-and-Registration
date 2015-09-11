package com.androidsources.loginandregistration;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidsources.loginandregistration.app.AppController;
import com.androidsources.loginandregistration.app.SessionManager;

public class MainActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtEmail;
    Button btnLogout;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        //setting toolbar
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);


        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        String name = AppController.getString(getApplicationContext(), "name");
        String email = AppController.getString(getApplicationContext(), "email");
        txtName.setText(name);
        txtEmail.setText(email);


        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }


    private void logoutUser() {
        session.setLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
