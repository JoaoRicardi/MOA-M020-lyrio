package com.example.lyrio.modules.cadastro.view;

import android.content.Intent;
import android.os.Bundle;

import com.example.lyrio.modules.login.view.LoginActivity;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lyrio.R;
import com.example.lyrio.database.LyrioDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCadastroActivity extends AppCompatActivity {

    private static final String TAG = "CadastroActivity";
    private FirebaseAuth firebaseAuth;
    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    private TextInputEditText editTextNome;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextSenha;
    private TextInputEditText editTextConfirmarSenha;
    private Button confirmarButton;
    private LyrioDatabase lyrioDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cadastro);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextNome = findViewById(R.id.cadastro_edit_text_nome);
        editTextEmail = findViewById(R.id.cadastro_edit_text_email);
        editTextSenha = findViewById(R.id.cadastro_edit_text_senha);
        editTextConfirmarSenha = findViewById(R.id.cadastro_edit_text_confirma_senha);
        confirmarButton = findViewById(R.id.cadastro_button_confirmar);

        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });


    }



    private void cadastrarUsuario() {


        String email = editTextEmail.getEditableText().toString();
        String senha = editTextSenha.getEditableText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Usuário criado com sucesso!");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            atualizarDadosUsuario(user);
                            irParaHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Falha na criação do usuário :-(", task.getException());
                            Toast.makeText(UserCadastroActivity.this, "Falha de autenticação",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void atualizarDadosUsuario(FirebaseUser user) {
        String nome = editTextNome.getEditableText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            finish();
                        }
                    }
                });

    }


//    public void botaoClicado(View view) {
//        editTextNome.setError(null);
//        editTextEmail.setError(null);
//        editTextSenha.setError(null);
//        editTextConfirmarSenha.setError(null);
//
//        if (!editTextSenha.getEditableText().toString().equals(editTextConfirmarSenha.getEditableText().toString())) {
//            editTextSenha.setError("As senhas não conferem");
//            editTextConfirmarSenha.setError("As senhas não conferem");
//        } else if (editTextSenha.getEditableText().toString().equals("")) {
//            editTextSenha.setError("Campo obrigatório");
//        } else if (editTextEmail.getEditableText().toString().equals("")) {
//            editTextEmail.setError("Campo obrigatório");
//        } else {
//
//            Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
//                    .setAction("OK", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            irParaLogin();
//                        }
//                    }).setActionTextColor(getResources().getColor(R.color.azulClaro)).show();
//        }
//    }

    private void irParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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

    private void irParaHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

