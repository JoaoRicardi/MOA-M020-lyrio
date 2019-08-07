package com.example.lyrio.modules.login.view;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lyrio.R;
import com.example.lyrio.modules.cadastro.view.UserCadastroActivity;
import com.example.lyrio.modules.recuperarSenha.view.UserEsqueciMinhaSenha;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.example.lyrio.util.Constantes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button botaoLogin ;
    private TextView registro ;
    private Button buttonFacebook;
    private Button registreComGoogle;
    private TextView esqueceuSenha ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        final Button confirmarButton = findViewById(R.id.botaoLogin);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoClicado(view);

            }
        });

        usernameEditText= findViewById(R.id.emailDigitado);
        passwordEditText = findViewById(R.id.senhaLogin);

        registro = findViewById(R.id.registreSe);
        buttonFacebook = findViewById(R.id.botaoLoginFacebook);
        registreComGoogle = findViewById(R.id.botaoLoginGoogle);
        esqueceuSenha = findViewById(R.id.esqueceuSenha);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaRegistro();
            }
        });


        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esqueceuSenha();
            }
        });

        SharedPreferences preferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        if (preferences.contains(Constantes.EMAIL)) {
            usernameEditText.setText(preferences.getString(Constantes.EMAIL, ""));
        }


    }

    public void botaoClicado(View view) {

        usernameEditText.setError(null);
        passwordEditText.setError(null);

        if (usernameEditText.getEditableText().toString().equals("")) {
            usernameEditText.setError("Informe seu email");
        } else if (!emailInvalido(usernameEditText.getEditableText().toString())) {
            usernameEditText.setError("e-mail não foi digitado corretamente");
        } else if (passwordEditText.getEditableText().toString().equals("")) {
            passwordEditText.setError("Informe sua senha");
        } else if (senhaValida(passwordEditText.getEditableText().toString())) {
            passwordEditText.setError("senha inválida");
        } else {
            irParaHome();
        }
    }

    // confirmar se o formato da senha é valido
    private boolean senhaValida(String senha) {
        senha = senha.trim();
        return senha.length() >= 6 && senha.length() < 14 && textPattern.matcher(senha).matches();
    }

    // conferir se o email é invalido

    public static boolean emailInvalido(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }

    //intent ir para registro
    private void irParaRegistro () {
        Intent intent = new Intent(this, UserCadastroActivity.class);
        startActivity(intent);
    }

    //ir para Home  - por enquanto esta indo para registro ate criar a Tela
    private void irParaHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    // ir para esqueci a minha senha
    private void esqueceuSenha (){
        Intent intent = new Intent(this, UserEsqueciMinhaSenha.class);
        startActivity(intent);
    }
}
