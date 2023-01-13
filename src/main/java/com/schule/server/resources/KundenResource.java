package com.schule.server.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("kunden")
public class KundenResource {
    List<Kunde> kundenListe = new ArrayList<Kunde>();
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(Kunde kunde){
        System.out.println(kunde);
        if(kunde!=null){
            kunde.setId(UUID.randomUUID());
            kundenListe.add(kunde);
            return Response.status(Response.Status.CREATED).entity(kunde).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Kein Kunden erhalten").build();
    }
}
