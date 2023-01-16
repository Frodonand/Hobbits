package com.schule.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.schule.server.data.Ablesung;
import com.schule.server.data.Kunde;

public class AblesungsModel {
    
    private List<Ablesung> data = new ArrayList<>();
    
    private static AblesungsModel instance;
    
    private AblesungsModel(){
    }
    
    public static AblesungsModel getInstance() {
        if(instance == null)
        return instance = new AblesungsModel();
        return instance;
    }


    public List<Ablesung> getData() {
        return data;
    }

    public void setData(List<Ablesung> data) {
        this.data = data;
    }

    public void updateEntry(int index, Ablesung newAblesung) {
        data.remove(index);
        data.add(index, newAblesung);
    }
    
    public void removeEntry(int index) {
        data.remove(index);
    }

    public Ablesung getEntry(int index) {
        return data.get(index);
    }
}

