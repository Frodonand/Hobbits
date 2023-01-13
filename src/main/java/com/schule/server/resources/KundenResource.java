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
public Response updateKundendaten(@PathParam("id") UUID id, Kunde k){
    for (Kunde kd:kundenListe) {
            if(id == kd.getId()){
                kd.setName(k.getName());
                kd.setVorname(k.getVorname());
                return Response.status(Response.Status.OK).entity(kd).build();
            } else if (k == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(kd).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(kd).build();
            }
    }
    return Response.status(Response.Status.OK).entity(k).build();
}

}
