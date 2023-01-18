package com.schule.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Ablesung;

public class AblesungsModel {

    private List<Ablesung> ablesungsList = new ArrayList<Ablesung>();
    private static AblesungsModel instance;
    
    private AblesungsModel(){
    }
    
    public static AblesungsModel getInstance() {
        if(instance == null)
        return instance = new AblesungsModel();
        return instance;
    }

    public List<Ablesung> getAblesungsList() {
        return ablesungsList;
    }

    public void setAblesungsList(List<Ablesung> ablesungsList) {
        this.ablesungsList = ablesungsList;
    }

    public void add(Ablesung ablesung){
        ablesungsList.add(ablesung);
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

