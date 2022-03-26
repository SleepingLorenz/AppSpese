package com.example.appspese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class CategoryListActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Category> categoryList = realm.where(Category.class).findAll().sort("name", Sort.DESCENDING);

        RecyclerView recyclerView = findViewById(R.id.recyclerCategoryView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryList);
        recyclerView.setAdapter(categoryAdapter);

        categoryList.addChangeListener(new RealmChangeListener<RealmResults<Category>>() {
            @Override
            public void onChange(RealmResults<Category> categories) {
                categoryAdapter.notifyDataSetChanged();
            }
        });

        // PULSANTE CATEGORIA
        findViewById(R.id.addCategoria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryListActivity.this, AddCategoryActivity.class));
            }
        });

        //------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
        // PULSANTI SIDEMENU
        // SIDE MENU TOGGLE
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.catNavView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuBtnSpese:
                        item.setChecked(true);
                        startActivity(new Intent(CategoryListActivity.this, MainActivity.class));
                        drawerLayout.closeDrawers();
                    break;

                    case R.id.menuBtnCategorie:
                        item.setChecked(true);
                        Toast.makeText(getApplicationContext(), "GIA STAI QUA AO", Toast.LENGTH_SHORT).show();
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

        findViewById(R.id.toolbarMenuIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------


    }
}