package com.schule.server.resources;

import java.util.UUID;

import com.schule.server.data.Ablesung;
import com.schule.server.data.Kunde;
import com.schule.server.model.AblesungsModel;
import com.schule.server.model.KundenModel;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("ablesungen")
public class AblesungsResource {
    private static AblesungsModel ablesungsModel = AblesungsModel.getInstance();
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAblesung(Ablesung ablesung){
        System.out.println(ablesung);
        if(ablesung!=null){
            ablesung.setId(UUID.randomUUID());
            ablesungsModel.getAblesungsMap().get(ablesung.getKunde()).add(ablesung);
            return Response.status(Response.Status.CREATED).entity(ablesung).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Keine Ablesung erhalten").build();
    }
}
