package com.schule.server.resources;

import java.util.UUID;

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
    private static final AblesungsModel ablesungsModel = AblesungsModel.getInstance();

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAblesung(@PathParam("id") String id) {
        try {
            UUID uuid = UUID.fromString(id);
            ablesungsModel.getData().removeIf(a -> a.getId() == uuid);
            return Response.status(Response.Status.OK).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Konnte keine UUID aus der ID machen").build();
        }
    }
}
