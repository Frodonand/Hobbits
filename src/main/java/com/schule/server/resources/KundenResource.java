package com.schule.server.resources;

import java.util.ArrayList;
import java.util.List;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/kunden")
public class KundenResource {
    List<Kunde> kundenListe = new ArrayList<Kunde>();

    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKundendaten(){
        return Response.status(Response.Status.OK).entity(kundenListe).build();
    }

}
