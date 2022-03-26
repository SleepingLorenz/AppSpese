package com.example.appspese;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import io.realm.Realm;

public class ModifySpesaActivity extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_spesa);

        realm = Realm.getDefaultInstance();

        Intent myIntent = getIntent();
            int id =  myIntent.getIntExtra(SpesaAdapter.EXTRA_ID,0);
            String title = myIntent.getStringExtra(SpesaAdapter.EXTRA_TITLE);
            String desc = myIntent.getStringExtra(SpesaAdapter.EXTRA_DESC);
            double price = myIntent.getDoubleExtra(SpesaAdapter.EXTRA_PRICE, 0);
            String date = myIntent.getStringExtra(SpesaAdapter.EXTRA_DATA);

        EditText vwTitle = findViewById(R.id.modTitle);
            vwTitle.setText(title);

        EditText vwDesc = findViewById(R.id.modDesc);
            vwDesc.setText(desc);

        EditText vwPrice = findViewById(R.id.modPrice);
            vwPrice.setText("" + price);

        EditText vwDate = findViewById(R.id.modDate);
            vwDate.setText(""+date);

        Button btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sTitle = vwTitle.getText().toString();
                String sDesc = vwDesc.getText().toString();
                Double sPrice = Double.parseDouble(vwPrice.getText().toString());

                Date sDate = null;
                try {
                    sDate = MainActivity.simpleDateFormat.parse(vwDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(sTitle)) {
                    vwTitle.setError("Please enter Title");
                } else if (sPrice == null) {
                    vwPrice.setError("Please enter Price");
                } else if (sDate == null) {
                    vwDate.setError("Please enter Date");
                } else {
                    //se sono in questo if vuol dire che
                    // ci sono tutti i dati per salvare la Spesa
                    updateSpesa(sTitle, sDesc, sPrice, sDate, id);
                }

                Toast.makeText(ModifySpesaActivity.this, "Spesa Aggiornata", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ModifySpesaActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void updateSpesa(String title, String desc, Double price, Date date, int id) {

        realm.beginTransaction();
        Spesa s = realm.where(Spesa.class).equalTo("id", id).findFirst();
            s.title = title;
            s.description = desc;
            s.price = price;
            s.date = date;

        realm.commitTransaction();
        realm.close();
    }
}

