package com.schule.server.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/kunden")
public class KundenResource {
    List<Kunde> kundenListe = new ArrayList<Kunde>();

@PUT
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public Response updateKundendaten(Kunde k){
    if (k == null) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Kunde angeben").build();
    }
    for (Kunde kd:kundenListe) {
        System.out.println(kd.getId());
        System.out.println(k.getId());
            if(k.getId() == kd.getId()){
                kd.setName(k.getName());
                kd.setVorname(k.getVorname());
                return Response.status(Response.Status.OK).entity("Update durchgeführt").build();
            }
    }
    return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
}

}
