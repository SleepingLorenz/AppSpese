package com.example.appspese;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{

    public static final String EXTRA_NOME = "com.appspese.EXTRA_NOME";
    public static final String EXTRA_HEX1 = "com.appspese.EXTRA_HEX1";
    public static final String EXTRA_HEX2 = "com.appspese.EXTRA_HEX2";
    public static final String EXTRA_ID = "com.appspese.EXTRA_ID";

    Context context;
    RealmResults<Category> categoryList;

    public CategoryAdapter(Context context, RealmResults<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        Realm realm = Realm.getDefaultInstance();

        Category cat = categoryList.get(position);

        Log.e("ID-CATEGORIA", String.valueOf(cat.getId()));
        Log.e("NOME-CATEGORIA", cat.getNome());
        Log.e("ID-HEX", cat.getHexColor1());

        holder.idOut.setText(String.valueOf(cat.getId()));
        holder.nomeOut.setText(cat.getNome());
        holder.hex1Out.setText(cat.getHexColor1());
        holder.hex2Out.setText(cat.getHexColor2());

        String color1 = "#" + cat.getHexColor1();
        String[] rgbSplit = cat.getHexColor2().split(",");

        //holder.hex1ColorOut.setBackgroundColor(Color.parseColor(color1));
        holder.hex2ColorOut.setBackgroundColor(Color.rgb(Integer.parseInt(rgbSplit[0]),Integer.parseInt(rgbSplit[1]),Integer.parseInt(rgbSplit[2])));

/*
        //MODIFICA CATEGORIA
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Mod.class);
                intent.putExtra(EXTRA_ID,cat.getId());
                intent.putExtra(EXTRA_NOME,cat.getNome());
                intent.putExtra(EXTRA_HEX1,cat.getHexColor1());
                intent.putExtra(EXTRA_HEX2,cat.getHexColor2());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                realm.close();

                context.startActivity(intent);
            }
        });
*/
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
                            cat.deleteFromRealm();
                            realm.commitTransaction();
                            realm.close();
                            Toast.makeText(context, "Categoria rimossa", Toast.LENGTH_SHORT).show();
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
        return categoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView idOut;
        TextView nomeOut;
        TextView hex1Out;
        TextView hex2Out;
        View hex1ColorOut;
        View hex2ColorOut;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            idOut = itemView.findViewById(R.id.idCatView);
            nomeOut = itemView.findViewById(R.id.nomeCatView);
            hex1Out = itemView.findViewById(R.id.hex1CatView);
            hex1ColorOut = itemView.findViewById((R.id.color1CatView));
            hex2Out = itemView.findViewById(R.id.hex2CatView);
            hex2ColorOut = itemView.findViewById((R.id.color2CatView));
        }
    }

}
