package com.schule.server.model;

import java.util.List;
import java.util.UUID;

import com.schule.persistence.MariaDBPersistanceKunde;
import com.schule.server.data.Kunde;

public class KundenModel {
    
    private static KundenModel instance;
    
    private MariaDBPersistanceKunde persistance = new MariaDBPersistanceKunde();

    private KundenModel(){
    }
    
    public static KundenModel getInstance() {
        if(instance == null)
        return instance = new KundenModel();
        return instance;
    }

    public List<Kunde> getAll() {
        return persistance.getAll();
    }

    public void add(Kunde ablesung){
        persistance.create(ablesung);
    }

    public Kunde update(Kunde newAblesung) {
        return persistance.update(newAblesung);
    }
    
    public boolean delete(UUID uuid) {
        return persistance.delete(uuid);
    }

    public Kunde get(UUID uuid) {
        return persistance.get(uuid);
    }
}

