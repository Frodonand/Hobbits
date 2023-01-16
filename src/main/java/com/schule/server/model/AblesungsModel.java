package com.schule.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.schule.server.data.Ablesung;
import com.schule.server.data.Kunde;

public class AblesungsModel {
    private HashMap<Kunde,List<Ablesung>> ablesungsMap = new HashMap<Kunde, List<Ablesung>>();
    
    private static AblesungsModel instance;
    
    private AblesungsModel(){
    }
    
    public static AblesungsModel getInstance() {
        if(instance == null)
        return instance = new AblesungsModel();
        return instance;
    }

    public HashMap<Kunde, List<Ablesung>> getAblesungsMap() {
        return ablesungsMap;
    }

    public void setAblesungsMap(HashMap<Kunde, List<Ablesung>> ablesungsMap) {
        this.ablesungsMap = ablesungsMap;
    }

    public void add(Kunde kunde,Ablesung ablesung){
        List<Ablesung> list = ablesungsMap.get(kunde);
        if(list == null){
            ablesungsMap.put(kunde,new ArrayList<Ablesung>());
            list = ablesungsMap.get(kunde);
            list.add(ablesung);
        }else {
            list.add(ablesung);
        }

    }

   /* public void updateEntry(int index, Ablesung newAblesung) {
        data.remove(index);
        data.add(index, newAblesung);
    }
    
    public void removeEntry(int index) {
        data.remove(index);
    }

    public Ablesung getEntry(int index) {
        return data.get(index);
    }*/
}

