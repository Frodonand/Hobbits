package com.schule.server.model;
import java.util.List;
import java.util.UUID;

import com.schule.persistence.MariaDBPersistanceAblesung;
import com.schule.server.data.Ablesung;

public class AblesungsModel {
    private static AblesungsModel instance;
    private MariaDBPersistanceAblesung persistance = new MariaDBPersistanceAblesung();
    
    private AblesungsModel(){

    }
    
    public static AblesungsModel getInstance() {
        if(instance == null)
        return instance = new AblesungsModel();
        return instance;
    }

    public List<Ablesung> getAll() {
        return persistance.getAll();
    }

    public void add(Ablesung ablesung){
        persistance.create(ablesung);
    }

    public Ablesung update(Ablesung newAblesung) {
        return persistance.update(newAblesung);
    }
    
    public boolean delete(UUID uuid) {
        return persistance.delete(uuid);
    }

    public Ablesung get(UUID uuid) {
        return persistance.get(uuid);
    }
}

