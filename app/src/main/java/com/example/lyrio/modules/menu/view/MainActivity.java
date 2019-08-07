package com.example.lyrio.modules.menu.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.lyrio.R;
import com.example.lyrio.adapters.ViewPagerAdapter;
import com.example.lyrio.modules.buscar.view.FragmentBuscar;
import com.example.lyrio.modules.home.view.FragmentHome;
import com.example.lyrio.modules.noticiasHotspot.view.FragmentNoticias;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Integer numeroDoFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_menu);
        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentHome(), "Home");
        adapter.AddFragment(new FragmentNoticias(), "Notícias");
        adapter.AddFragment(new FragmentBuscar(), "Buscar");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        try {
            numeroDoFragment = getIntent().getExtras().getInt("NUMERO");
        } catch (Exception e) {
            numeroDoFragment = null;
        }

        changeView(numeroDoFragment);


    }

    public void changeView(Integer pageNum) {
        if (pageNum != null) {
            viewPager.setCurrentItem(pageNum);
        } else {
            viewPager.setCurrentItem(1);
        }
    }


}

