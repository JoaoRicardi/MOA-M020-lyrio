package com.example.lyrio.modules.configuracoes.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lyrio.R;
import com.example.lyrio.modules.menu.view.MainActivity;
import com.example.lyrio.modules.login.view.LoginActivity;

public class ConfiguracoesActivity extends AppCompatActivity {

    private ImageView setaVoltar;
    private Button logoutButton;
    private TextView configuracoes;
    private TextView infosPessoais;
    private Switch salvarSwitch;
    private Switch notificacoesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        setaVoltar = findViewById(R.id.voltar_imageView);
        logoutButton = findViewById(R.id.logout_button);
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
                                fazerLogout();
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


    private void fazerLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void voltarParaHome() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("NUMERO", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
