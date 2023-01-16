package com.schule.server;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.schule.server.data.Ablesung;
import com.schule.server.data.Kunde;
import com.schule.server.model.AblesungsModel;
import com.schule.server.model.KundenModel;
import com.schule.server.persistence.JSONPersistance;
import com.sun.net.httpserver.HttpServer;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Server {
    
    private static HttpServer server;
    private final static String PACK="com.schule.server.resources";

    private static KundenModel kundenModel = KundenModel.getInstance();
    private static AblesungsModel ablesungModel = AblesungsModel.getInstance();

    private final static JSONPersistance<Kunde> kundenPersistance =
        new JSONPersistance<Kunde>(Kunde.class,"target/server/Kunden.json");
    private final static JSONPersistance<Ablesung> ablesungenPersistance =
        new JSONPersistance<Ablesung>(Ablesung.class,"target/server/Ablesung.json");

    public static void main(String[] args) {
        startServer("http://localhost:8080/", true);
    }

    public static void startServer(String url, boolean loadFromFile){
        if(server == null){
            if(loadFromFile){
                HashMap<Kunde,List<Ablesung>> ablesungenMap = new HashMap<>();
                List<Kunde> alleKunden = kundenPersistance.load();
                for(Kunde k : alleKunden){
                    ablesungenMap.put(k,new ArrayList<Ablesung>());
                }
                kundenModel.setData(alleKunden);
                List<Ablesung> alleAblesungen = ablesungenPersistance.load();
                for(Ablesung a : alleAblesungen){
                    for(Kunde k : alleKunden){
                        if(k.getId().equals(a.getKunde().getId())){
                            ablesungenMap.get(k).add(a);
                            a.setKunde(k);
                            break;
                        }
                    }
                }
            }
            final ResourceConfig rc = new ResourceConfig().packages(PACK);
            server = JdkHttpServerFactory.createHttpServer(
            URI.create(url),
            rc
        );
    }
    }


    public static void stopServer(boolean saveToFile){
        if(saveToFile){
            kundenPersistance.save(kundenModel.getData());
            List<Ablesung> ablesungenToSave = new ArrayList<>();
            Collection<List<Ablesung>> ablesungen = ablesungModel.getAblesungsMap().values();
            for (List<Ablesung> list : ablesungen){
                ablesungenToSave.addAll(list);
            }
            ablesungenPersistance.save(ablesungenToSave);
        }
        if(server != null){
        server.stop(0);   
        server = null;
    }
    }
}
