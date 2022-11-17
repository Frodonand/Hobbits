package com.schule.model;

import java.util.List;

import com.schule.data.Zaehlerdatum;
import com.schule.persistence.JSONPersistance;

public class ZaehlerDatenModel {
    

    private final JSONPersistance<Zaehlerdatum> persistance;
    private List<Zaehlerdatum> data;
    
    private static ZaehlerDatenModel instance;
    
    private ZaehlerDatenModel(){
        persistance = new JSONPersistance<Zaehlerdatum>(Zaehlerdatum.class);
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
    
    public List<Zaehlerdatum> getData() {
        return data;
    }

    public void updateEntry(int index, Zaehlerdatum newZaehlerdatum) {
        data.remove(index);
        data.add(index, newZaehlerdatum);
    }
    
    public void removeEntry(int index) {
        data.remove(index);
    }

    public Zaehlerdatum getEntry(int index) {
        return data.get(index);
    }
}
