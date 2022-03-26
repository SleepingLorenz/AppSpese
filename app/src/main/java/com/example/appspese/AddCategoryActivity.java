package com.example.appspese;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AddCategoryActivity extends AppCompatActivity {

    ImageView imgColorWheel;
    TextView tvColorValues;
    EditText etNome;
    View vSelectedColor;
    Button bAddCategory;
    Bitmap bitmap;
    String hex,rgb;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        // REALM
        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        imgColorWheel = findViewById(R.id.colorPicker);
        tvColorValues = findViewById(R.id.displayValues);
        vSelectedColor = findViewById(R.id.displayColor);
        bAddCategory = findViewById(R.id.addNewCategory);
        etNome = findViewById(R.id.nomeCategoria);

        imgColorWheel.setDrawingCacheEnabled(true);
        imgColorWheel.buildDrawingCache(true);

        // PRENDI COLORE
        imgColorWheel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                {
                    bitmap = imgColorWheel.getDrawingCache();
                    int pixels = bitmap.getPixel((int)motionEvent.getX(), (int)motionEvent.getY());

                    int r = Color.red(pixels);
                    int g = Color.green(pixels);
                    int b = Color.blue(pixels);

                    hex = "#" + Integer.toHexString(pixels);
                    rgb = r + "," + g + "," + b;
                    vSelectedColor.setBackgroundColor(Color.rgb(r,g,b));
                    tvColorValues.setText("RGB: " + rgb + "  \nHEX:" + hex);

                }
                return false;
            }
        });

        // SALVA
        bAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeCategoria = etNome.getText().toString();

                if (hex == null) {
                    etNome.setError("Please enter a Color");
                } else if (TextUtils.isEmpty(nomeCategoria)) {
                    etNome.setError("Please enter a Name");
                } else {

                    Number id = realm.where(Category.class).max("id");
                    int nextId = Utils.getNextId(id);

                    realm.beginTransaction();
                    Category cat = realm.createObject(Category.class);
                    cat.id = nextId;
                    cat.name = nomeCategoria;
                    cat.hexColor1 = hex;
                    cat.hexColor2 = rgb;

                    realm.commitTransaction();
                    Toast.makeText(getApplicationContext(), "Categoria Salvata", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        });


    }


}