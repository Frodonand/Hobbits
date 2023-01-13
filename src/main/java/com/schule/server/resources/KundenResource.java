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

    @Path("kunden/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") UUID id) {
        try {
            kundenListe.removeIf(k -> k.getId() == id);
            return Response.status(Response.Status.OK).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(kundenListe).build();
        }
    }
}

