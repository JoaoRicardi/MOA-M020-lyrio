package com.example.lyrio.modules.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyrio.R;
import com.example.lyrio.modules.menu.view.MainActivity;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                irParaUserLoginActivity();
            }
        }, 2000);
    }

    private void irParaUserLoginActivity () {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}