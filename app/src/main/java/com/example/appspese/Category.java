package com.example.appspese;

import io.realm.RealmObject;

public class Category extends RealmObject {

    int id;
    String name;
    String hexColor1;
    String hexColor2;

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String name) {
        this.name = name;
    }

    public void setHexColor1(String hexColor1) {
        this.hexColor1 = hexColor1;
    }

    public void setHexColor2(String hexColor2) {
        this.hexColor2 = hexColor2;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return name;
    }

    public String getHexColor1() {
        return hexColor1;
    }

    public String getHexColor2() {
        return hexColor2;
    }
}
