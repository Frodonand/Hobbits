package com.schule.server;

import java.net.URI;

import com.sun.net.httpserver.HttpServer;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class Server {
    
    private static HttpServer server;
    private final static String PACK="com.schule.server.resources";

    public static void startServer(String url, boolean loadFromFile){
        if(server == null){
        final ResourceConfig rc = new ResourceConfig().packages(PACK);
        server = JdkHttpServerFactory.createHttpServer(
          URI.create(url),
          rc
        );
    }
    }


    public static void stopServer(boolean loadFromFile){
        if(server != null){
        server.stop(0);   
        server = null;
    }
    }
}
