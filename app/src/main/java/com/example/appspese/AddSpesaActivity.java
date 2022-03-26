package com.example.appspese;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddSpesaActivity extends AppCompatActivity {

    Realm realm;
    Spinner spinner;
    RealmResults<Category> categories;
    String testoNoCategory = "nessuna categoria";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spesa);

        final EditText titleIn = findViewById(R.id.titleSpesa);
        final EditText descIn = findViewById(R.id.descSpesa);
        final EditText priceIn = findViewById(R.id.prezzoSpesa);
        spinner = findViewById(R.id.spinner);
        //la data non c'è perche la imposto direttamente sotto
        Button btnSave = findViewById(R.id.btnAddSpesa);


        // REALM
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        popoulateSpinnerCategory();

        btnSave.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleIn.getText().toString();
                String desc = descIn.getText().toString();
                Double price = Double.parseDouble(priceIn.getText().toString());
                String category = spinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(title)) {
                    titleIn.setError("Please enter Title");
                } else if (price == null) {
                    priceIn.setError("Please enter Price");
                }
                else if (TextUtils.isEmpty(category)){
                    Toast.makeText(getApplicationContext(),"Pleas enter a Category", Toast.LENGTH_LONG);
                }
                else {

                    Number id = realm.where(Category.class).max("id");
                    int nextId = Utils.getNextId(id);

                    realm.beginTransaction();
                    Spesa spesa = realm.createObject(Spesa.class);
                    spesa.id = nextId;
                    spesa.title = title;
                    spesa.description = desc;
                    spesa.price = price;
                    spesa.date = Calendar.getInstance().getTime();

                    if(category != testoNoCategory) {
                        Category selCat = realm.where(Category.class).equalTo("name", category).findFirst();
                        spesa.idCategoria = selCat.id;
                    }

                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(), "Spesa Salvata", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }));

    }

    private void popoulateSpinnerCategory() {

        categories = realm.where(Category.class).findAll().sort("name", Sort.ASCENDING);

        ArrayList<String> catNames = new ArrayList<>();

        if (categories.size() != 0) {
            for (int i = 0; i < categories.size(); i++) {
                catNames.add(categories.get(i).name);
            }
        } else {
            catNames.add(testoNoCategory);
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, catNames);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(catAdapter);

    }
}
