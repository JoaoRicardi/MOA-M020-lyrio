package com.example.lyrio.modules.recuperarSenha.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lyrio.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class EmailRecuperarSenha extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputEditText emailRecuperar;
    private Button botaoEnviarEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_recuperar_senha);

        emailRecuperar = findViewById(R.id.esqueceuSenhaLogin_id);
        botaoEnviarEmail = findViewById(R.id.botaoEnviarEmail_id);


        firebaseAuth = FirebaseAuth.getInstance();



    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {


    }


    public void reset( View view ){
        firebaseAuth
                .sendPasswordResetEmail(emailRecuperar.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(EmailRecuperarSenha.this, "Email de recuperação enviado com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailRecuperarSenha.this, "Email falhou, tente novamente", Toast.LENGTH_SHORT).show();


                        }

                    }
                });

    }

}
