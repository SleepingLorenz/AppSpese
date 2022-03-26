package com.example.appspese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Realm realm;

    public static  final SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("dd/MM/yy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            // PULSANTI HOME ---------------------------------------------------------------------------
            // PULSANTE SPESA
            findViewById(R.id.addSpesa).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddSpesaActivity.class));
                }
            });

            // PULSANTE CATEGORIA
            findViewById(R.id.addCategoria).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, AddCategoryActivity.class));
                }
            });
            //------------------------------------------------------------------------------------------
            //------------------------------------------------------------------------------------------
            // PULSANTI SIDEMENU
            // SIDE MENU TOGGLE
            drawerLayout = findViewById(R.id.drawerLayout);
            navigationView = findViewById(R.id.mainNavView);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menuBtnSpese:
                            item.setChecked(true);
                            Toast.makeText(getApplicationContext(), "GIA STAI QUA AO", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                        break;
                        case R.id.menuBtnCategorie:
                            item.setChecked(true);
                            if(realm.where(Category.class).findAll().size() > 0)
                                startActivity(new Intent(MainActivity.this, CategoryListActivity.class));
                            else
                                Toast.makeText(getApplicationContext(), "DEVI CREARE PRIMA UNA CATEGORIA", Toast.LENGTH_LONG).show();
                            drawerLayout.closeDrawers();
                        break;

                        case R.id.menuBtnSettings:
                            item.setChecked(true);
                            Toast.makeText(getApplicationContext(), "SETTINGS", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawers();
                        break;
                    }
                    return false;
                }
            });
/*
            // ELIMINA TUTTO
            findViewById(R.id.toolbarLogo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    realm.beginTransaction();
                    realm.deleteAll();
                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(), "DATI ELIMINATI", Toast.LENGTH_SHORT).show();
                }
            });
*/
            findViewById(R.id.toolbarMenuIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            });

            //------------------------------------------------------------------------------------------
            //------------------------------------------------------------------------------------------

            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration.Builder().build();
            Realm.setDefaultConfiguration(config);
            realm = Realm.getDefaultInstance();

            RealmResults<Spesa> spesaList = realm.where(Spesa.class).findAll().sort("date", Sort.DESCENDING);

            if(spesaList.size() > 0)
                aggiornaTotale(spesaList);

            RecyclerView recyclerView = findViewById(R.id.recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            SpesaAdapter spesaAdapter = new SpesaAdapter(getApplicationContext(), spesaList);
            recyclerView.setAdapter(spesaAdapter);

            spesaList.addChangeListener(new RealmChangeListener<RealmResults<Spesa>>() {
                @Override
                public void onChange(RealmResults<Spesa> spese) {
                    spesaAdapter.notifyDataSetChanged();
                    aggiornaTotale(spese);
                }
            });
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }

    public void aggiornaTotale(RealmResults<Spesa> spesaList) {

        TextView totale = findViewById(R.id.totale);
        double tot = 0;
        for (int i = 0; i < spesaList.size(); i++) {
            tot += spesaList.get(i).price;
        }
        totale.setText(String.valueOf(tot) + "€");
    }

}
