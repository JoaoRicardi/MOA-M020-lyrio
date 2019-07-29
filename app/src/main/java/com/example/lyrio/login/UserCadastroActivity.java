package com.example.lyrio.login;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.lyrio.R;

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

        if (senhaValida(editTextSenha.getEditableText().toString())) {
            if (!editTextSenha.getEditableText().toString().equals(editTextConfirmarSenha.getEditableText().toString())) {
                editTextSenha.setError("As senhas não conferem!");
                editTextConfirmarSenha.setError("As senhas não conferem!");
            } else if (editTextNome.getEditableText().toString().equals("")) {
                editTextNome.setError("Campo obrigatório!");
            } else if(editTextEmail.getEditableText().toString().equals("")){
                editTextEmail.setError("Campo Obrigatório");
            } else if (!editTextEmail.getEditableText().toString().matches(emailRegex)) {
                editTextEmail.setError("O email deve conter @ e .");
            } else if (editTextSenha.getEditableText().toString().equals("")) {
                editTextSenha.setError("Campo obrigatório!");
            } else if (editTextConfirmarSenha.getEditableText().toString().equals("")) {
                editTextConfirmarSenha.setError("Campo obrigatório!");
            } else {
                Snackbar.make(view, "Cadastro realizado com sucesso!", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        irParaLogin();
                    }
                }).setActionTextColor(getResources().getColor(R.color.azulClaro)).show();
            }
        }else{
            editTextSenha.setError("A senha deve ter tamanho entre 6 a 14 caracteres contendo números, letras maiusculas e minusculas ");
        }
    }

    private void irParaLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
      startActivity(intent);
    }

    private boolean senhaValida(String senha) {
        senha = senha.trim();
        return senha.length() >= 6 && senha.length() <= 14 && textPattern.matcher(senha).matches();
    }

}

