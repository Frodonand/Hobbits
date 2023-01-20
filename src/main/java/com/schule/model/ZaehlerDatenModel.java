package com.schule.model;

import java.util.List;

import com.schule.data.Ablesung;
import com.schule.persistence.JSONPersistance;

public class ZaehlerDatenModel {
    

    private final JSONPersistance<Ablesung> persistance;
    private List<Ablesung> data;
    
    private static ZaehlerDatenModel instance;
    
    private ZaehlerDatenModel(){
        persistance = new JSONPersistance<Ablesung>(Ablesung.class);
        data = persistance.load();
        
    }
    
    public static ZaehlerDatenModel getInstance() {
        if(instance == null)
        return instance = new ZaehlerDatenModel();
        return instance;
    }

    public void save(){
        persistance.save(data);
    }
    
    public List<Ablesung> getData() {
        return data;
    }

    public void updateEntry(int index, Ablesung newZaehlerdatum) {
        data.remove(index);
        data.add(index, newZaehlerdatum);
    }
    
    public void removeEntry(int index) {
        data.remove(index);
    }

    public Ablesung getEntry(int index) {
        return data.get(index);
    }
/*
    public int getIndex(Zaehlerdatum curr) {
        return data.indexOf(curr);
    }*/
}
