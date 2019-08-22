package com.example.lyrio.modules.configuracoes.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lyrio.R;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.example.lyrio.modules.login.view.LoginActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageView setaVoltar;
    private Button logoutButton;
    private TextView configuracoes;
    private TextView infosPessoais;
    private Switch salvarSwitch;
    private Switch notificacoesSwitch;
    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        setaVoltar = findViewById(R.id.voltar_imageView);
        logoutButton = findViewById(R.id.sair_aplicativo_id);
        configuracoes = findViewById(R.id.configuracoes);
        infosPessoais = findViewById(R.id.informacoes_text_view);
        salvarSwitch = findViewById(R.id.salva_celular_switch);
        notificacoesSwitch = findViewById(R.id.notificacoes_switch);




        infosPessoais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibirDialog();
            }
        });

        setaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltarParaHome();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ConfiguracoesActivity.this)
                        .setTitle("ATENÇÃO")
                        .setMessage("Deseja realmente fazer LogOut no APP?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        })
                        .setNegativeButton("NÂO", null);
                alert.create();
                alert.show();
            }
        });




        salvarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        notificacoesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    private void exibirDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_infos_pessoais);
        dialog.show();

        Button cancelarButton = dialog.findViewById(R.id.cancelar_info_button);
        cancelarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button okButton = dialog.findViewById(R.id.ok_info_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    // Logout com Google
        private void logOut (View view){
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if(status.isSuccess()){
                        goLogInScreen();
                    }else{
                        Toast.makeText(view.getContext(),"logOut não foi possível", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    // metodo do Google
    private void goLogInScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    private void fazerLogout() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//    }

    private void voltarParaHome() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("NUMERO", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
