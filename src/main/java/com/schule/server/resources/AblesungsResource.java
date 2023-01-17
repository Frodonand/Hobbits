package com.schule.server.resources;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    @DELETE
    @Path("/{id}")
    public Response deleteAblesung(@PathParam("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);
            Collection<List<Ablesung>> list = ablesungsModel.getAblesungsMap().values();
            Optional<Ablesung> flat = list.stream()
                    .flatMap(List<Ablesung>::stream)
                    .filter(e->e.getId().equals(uuid))
                    .findFirst();
            if(flat.isPresent()){
                Ablesung a = flat.get();
                List<Ablesung> kundeAblesungen = ablesungsModel.getAblesungsMap().get(a.getKunde().getId());
                kundeAblesungen.removeIf(e -> e.getId().equals(a.getId()));
                return Response.status(Response.Status.OK).entity(a).build();
            }
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Konnte keine UUID aus der ID machen").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Nutzer nicht gefunden").build();
    }


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
