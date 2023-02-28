package com.schule.server;

import java.net.URI;
import java.util.*;

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
        startServer("http://localhost:8080/");
        //stopServer(true);
    }

    public static void startServer(String url){
        if(server == null){
            final ResourceConfig rc = new ResourceConfig().packages(PACK);
            server = JdkHttpServerFactory.createHttpServer(
            URI.create(url),
            rc
        );
        }
    }

    public static void stopServer(){
        if(server != null){
        server.stop(0);   
        server = null;
    }
    }
}
