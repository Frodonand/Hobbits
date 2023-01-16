package com.schule.server.model;

import java.util.ArrayList;
import java.util.List;

import com.schule.server.data.Kunde;

public class KundenModel {
    
    private List<Kunde> data = new ArrayList<>();
    
    private static KundenModel instance;
    
    private KundenModel(){
    }
    
    public static KundenModel getInstance() {
        if(instance == null)
        return instance = new KundenModel();
        return instance;
    }


    public List<Kunde> getData() {
        return data;
    }

    public void setData(List<Kunde> data) {
        this.data = data;
    }

    public void updateEntry(int index, Kunde newKunde) {
        data.remove(index);
        data.add(index, newKunde);
    }
    
    public void removeEntry(int index) {
        data.remove(index);
    }

    public Kunde getEntry(int index) {
        return data.get(index);
    }
}

