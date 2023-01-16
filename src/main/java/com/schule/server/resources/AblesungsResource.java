package com.schule.server.resources;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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


    @GET
    @Path("/vorZweiJahrenHeute")
    public Response getAblesungenTwoYears(){
        Collection<List<Ablesung>> list = ablesungsModel.getAblesungsMap().values();
        List<Ablesung> flat = list.stream()
            .flatMap(List<Ablesung>::stream)
            .filter(e->e.getDatum().getYear()>=LocalDate.now().getYear()-2)
            .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(flat).build();
    }
}
