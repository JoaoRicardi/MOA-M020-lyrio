package com.example.lyrio.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyrio.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCadastroActivity extends AppCompatActivity {

    public final Pattern textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    private String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    private TextInputEditText editTextNome;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextSenha;
    private TextInputEditText editTextConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cadastro);

        Button confirmarButton = findViewById(R.id.cadastro_button_confirmar);
        confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botaoClicado(view);
            }
        });

        editTextNome = findViewById(R.id.cadastro_edit_text_nome);
        editTextEmail = findViewById(R.id.cadastro_edit_text_email);
        editTextSenha = findViewById(R.id.cadastro_edit_text_senha);
        editTextConfirmarSenha = findViewById(R.id.cadastro_edit_text_confirma_senha);
    }


    public void botaoClicado(View view) {
        editTextNome.setError(null);
        editTextEmail.setError(null);
        editTextSenha.setError(null);
        editTextConfirmarSenha.setError(null);

        if (!editTextSenha.getEditableText().toString().equals(editTextConfirmarSenha.getEditableText().toString())) {
            editTextSenha.setError("As senhas não conferem");
            editTextConfirmarSenha.setError("As senhas não conferem");
        } else if (editTextSenha.getEditableText().toString().equals("")) {
            editTextSenha.setError("Campo obrigatório");
        } else if (editTextEmail.getEditableText().toString().equals("")) {
            editTextEmail.setError("Campo obrigatório");
        } else {

            Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            irParaLogin();
                        }
                    }).setActionTextColor(getResources().getColor(R.color.azulClaro)).show();
        }
    }

    private void irParaLogin() {
        String enviarEmail = String.valueOf(editTextEmail);
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

}

