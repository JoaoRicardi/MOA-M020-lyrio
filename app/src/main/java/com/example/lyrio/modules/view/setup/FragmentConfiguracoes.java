package com.example.lyrio.modules.view.setup;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.lyrio.R;
import com.example.lyrio.modules.view.menu.FragmentHome;
import com.example.lyrio.modules.view.login.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConfiguracoes extends Fragment {


    private ImageView setaVoltar;
    private Button logoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_configuracoes, container, false);


        setaVoltar = view.findViewById(R.id.voltar_imageView);
        logoutButton = view.findViewById(R.id.logout_button);


        setaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                voltarParaHome();
            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(container.getContext())
                        .setTitle("ATENÇÃO")
                        .setMessage("Deseja realmente fazer LogOut no APP?")
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                fecharApp();
                            }
                        })
                        .setNegativeButton("NÂO", null);
                alert.create();
                alert.show();
            }
        });

        return view;
    }


    private void fecharApp() {

        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);


    }

    private void voltarParaHome() {
        Intent intent = new Intent(getContext(), FragmentHome.class);
        startActivity(intent);
    }
}
