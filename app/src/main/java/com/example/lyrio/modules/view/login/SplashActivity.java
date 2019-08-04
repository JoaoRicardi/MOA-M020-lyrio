package com.example.lyrio.modules.view.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lyrio.R;
import com.example.lyrio.modules.view.menu.TabMenu;

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
        Intent intent = new Intent(this, TabMenu.class);
        startActivity(intent);
        finish();
    }
}