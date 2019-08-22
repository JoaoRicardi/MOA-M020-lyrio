package com.example.lyrio.modules.login.view;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lyrio.R;
import com.example.lyrio.modules.cadastro.view.UserCadastroActivity;
import com.example.lyrio.modules.login.viewmodel.LoginViewModel;
import com.example.lyrio.modules.recuperarSenha.view.EmailRecuperarSenha;
import com.example.lyrio.modules.recuperarSenha.view.UserEsqueciMinhaSenha;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.android.gms.auth.api.Auth.GOOGLE_SIGN_IN_API;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;
    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private EditText emailEditText;
    private EditText senhaEditText;
    private Button botaoLogin ;
    private TextView registro ;
    private SignInButton loginGoogle;
    private TextView esqueceuSenha ;
    private ProgressBar progressBar;
    private LoginViewModel loginViewModel;
    public static final int SIGN_IN_CODE = 777;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailDigitado);
        senhaEditText = findViewById(R.id.senhaLogin);

        registro = findViewById(R.id.registreSe);
        botaoLogin = findViewById(R.id.botaoLogin);
        loginGoogle = findViewById(R.id.botaoLoginGoogle);
        esqueceuSenha = findViewById(R.id.esqueceuSenha);
        progressBar = findViewById(R.id.progressBar);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Login com Google
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        botaoLogin.setOnClickListener( view -> logar());

        registro.setOnClickListener(view -> LoginActivity.this.irParaRegistro());

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE );


            }
        });


        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esqueceuSenha();
            }
        });

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.getAutenticadoLiveData()
                .observe(this, autenticado -> {
                    if (autenticado) {
                    irParaHome();
                    } else {
                        Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                    }
                });

        loginViewModel.getLoaderLiveData()
                .observe(this, showLoader -> progressBar.setVisibility(showLoader ? View.VISIBLE : View.GONE));

    }




    private void logar() {

        String email = emailEditText.getEditableText().toString();
        String senha = senhaEditText.getEditableText().toString();

        loginViewModel.autenticarUsuario(email, senha);

        irParaHome();


    }

    public void botaoClicado(View view) {

        emailEditText.setError(null);
        senhaEditText.setError(null);

        if (emailEditText.getEditableText().toString().equals("")) {
            emailEditText.setError("Informe seu email");
        } else if (!emailInvalido(emailEditText.getEditableText().toString())) {
            emailEditText.setError("e-mail não foi digitado corretamente");
        } else if (senhaEditText.getEditableText().toString().equals("")) {
            senhaEditText.setError("Informe sua senha");
        } else if (senhaValida(senhaEditText.getEditableText().toString())) {
            senhaEditText.setError("senha inválida");
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
        Intent intentHome = new Intent(this, MainActivity.class);
        startActivity(intentHome);
    }
    // ir para esqueci a minha senha
    private void esqueceuSenha (){
        Intent intent = new Intent(this, EmailRecuperarSenha.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignResult(result);
        }

    }

    private void handleSignResult(GoogleSignInResult result){

        if(result.isSuccess()){
            Toast.makeText(this,"Login com Google efetuado com sucesso", Toast.LENGTH_SHORT).show();
            irParaHome();
        } else {
            Toast.makeText(this,"Não foi possivel logar com Google", Toast.LENGTH_SHORT).show();

        }

    }


}
