package com.example.lyrio.modules.view.login;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lyrio.R;
import com.example.lyrio.interfaces.LoginResultadoCallBack;
import com.example.lyrio.modules.model.Usuario;
import com.example.lyrio.modules.view.menu.TabMenu;
import com.example.lyrio.modules.viewModel.LoginViewModel;
import com.example.lyrio.util.Constantes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity{

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button botaoLogin ;
    private TextView registro ;
    private Button buttonFacebook;
    private Button registreComGoogle;
    private TextView esqueceuSenha ;

    LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getUsuarioLiveData()
                .observe(this, usuario -> {
                    loginViewModel
                });

        final Button confirmarButton = findViewById(R.id.botaoLogin);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               loginViewModel.botaoClicado(view);

            }
        });

//        registro.setOnClickListener(new View.OnClickListener() {
  //          @Override
    //        public void onClick(View v) {
      //          irParaRegistro();
        //    }
      //  });


//        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
 //           @Override
  //          public void onClick(View v) {
  //              esqueceuSenha();
  //          }
  //      });

        SharedPreferences preferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (preferences.contains(Constantes.EMAIL)) {
            usernameEditText.setText(preferences.getString(Constantes.EMAIL, ""));
        }


    }

    //intent ir para registro
    private void irParaRegistro () {
        Intent intent = new Intent(this, UserCadastroActivity.class);
        startActivity(intent);
    }

    //ir para Home  - por enquanto esta indo para registro ate criar a Tela
    private void irParaHome(){
        Intent intent = new Intent(this, TabMenu.class);
        startActivity(intent);
    }
    // ir para esqueci a minha senha
    private void esqueceuSenha (){
        Intent intent = new Intent(this, UserEsqueciMinhaSenha.class);
        startActivity(intent);
    }
}
