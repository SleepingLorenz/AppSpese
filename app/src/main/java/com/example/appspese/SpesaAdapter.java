package com.example.appspese;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class SpesaAdapter extends RecyclerView.Adapter<SpesaAdapter.MyViewHolder>{

    public static final String EXTRA_TITLE = "com.appspese.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.appspese.EXTRA_DESC";
    public static final String EXTRA_PRICE = "com.appspese.EXTRA_PRICE";
    public static final String EXTRA_DATA = "com.appspese.EXTRA_DATA";
    public static final String EXTRA_ID_CATEGORY = "com.appspese.EXTRA_ID_CATEGORY";
    public static final String EXTRA_ID = "com.appspese.EXTRA_ID";

    Context context;
    RealmResults<Spesa> spesaList;
    RealmResults<Category> categoryList;
    int color;
    GradientDrawable bgGradient;

    public SpesaAdapter(Context context, RealmResults<Spesa> spesaList) {
        this.context = context;
        this.spesaList = spesaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.spesa_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        Spesa spesa = spesaList.get(position);
        holder.titleOut.setText(spesa.getTitle());
        holder.descOut.setText(spesa.getDescription());

        String strPrice = String.valueOf(spesa.getPrice());
        holder.priceOut.setText(strPrice + "€");

        String strDate = MainActivity.simpleDateFormat.format(spesa.getDate());
        holder.dateOut.setText(strDate);

        bgGradient = (GradientDrawable) holder.itemView.getBackground();

        if (Integer.valueOf(spesa.idCategoria) != 0) {

            Category category = realm.where(Category.class).equalTo("id", spesa.idCategoria).findFirst();

            if (!category.hexColor2.equals("")) {
                String[] rgbSplit = category.getHexColor2().split(",");
                color = Color.rgb(Integer.parseInt(rgbSplit[0]),Integer.parseInt(rgbSplit[1]),Integer.parseInt(rgbSplit[2]));
                bgGradient.setStroke(20, color);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ModifySpesaActivity.class);
                    intent.putExtra(EXTRA_ID,spesa.getId());
                    intent.putExtra(EXTRA_TITLE,spesa.getTitle());
                    intent.putExtra(EXTRA_DESC,spesa.getDescription());
                    intent.putExtra(EXTRA_PRICE,spesa.getPrice());
                    intent.putExtra(EXTRA_ID_CATEGORY,spesa.getIdCategoria());
                    intent.putExtra(EXTRA_DATA, MainActivity.simpleDateFormat.format(spesa.getDate()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                realm.close();

                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenu().add("ELIMINA");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("ELIMINA"))
                        {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            spesa.deleteFromRealm();
                            realm.commitTransaction();
                            realm.close();
                            Toast.makeText(context, "Spesa rimossa", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return spesaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOut;
        TextView descOut;
        TextView priceOut;
        TextView dateOut;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleOut = itemView.findViewById(R.id.titleoutput);
            descOut = itemView.findViewById(R.id.descoutput);
            priceOut = itemView.findViewById(R.id.priceoutput);
            dateOut = itemView.findViewById(R.id.dataoutput);
        }
    }

}
