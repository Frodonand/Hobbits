package com.schule.server.resources;

import com.schule.server.data.Ablesung;
import com.schule.server.model.AblesungsModel;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("ablesungen")
public class AblesungsResource {
    private static AblesungsModel ablesungsModel = AblesungsModel.getInstance();


    @GET
    @Path("/vorZweiJahrenHeute")
    public Response getAblesungenTwoYears() {
        Collection<List<Ablesung>> list = ablesungsModel.getAblesungsMap().values();
        List<Ablesung> flat = list.stream()
                .flatMap(List<Ablesung>::stream)
                .filter(e -> e.getDatum().getYear() >= LocalDate.now().getYear() - 2)
                .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(flat).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAblesung(Ablesung ablesung) {

        System.out.println(ablesung);


        if (ablesung != null) {
            if (ablesungsModel.getAblesungsMap().get(ablesung.getKunde()) == null) {
                return Response.status(Response.Status.NOT_FOUND).entity(ablesung).build();
            }
            ablesung.setId(UUID.randomUUID());
            ablesungsModel.getAblesungsMap().get(ablesung.getKunde()).add(ablesung);
            return Response.status(Response.Status.CREATED).entity(ablesung).build();
        }


        return Response.status(Response.Status.BAD_REQUEST).entity("Keine Ablesung erhalten").build();
    }
}
