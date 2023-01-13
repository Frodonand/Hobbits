package com.schule.server.resources;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/kunden")
public class KundenResource {
    @POST
    @Path("")
    public Response addCustomer(Kunde kunde){
        return null;
    }
}
