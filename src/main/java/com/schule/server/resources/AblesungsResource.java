package com.schule.server.resources;

import java.util.UUID;

import com.schule.server.data.Ablesung;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Path("ablesungen")
public class AblesungsResource {
    private static final AblesungsModel ablesungsModel = AblesungsModel.getInstance();
    private static final KundenModel kundenModel = KundenModel.getInstance();

    @DELETE
    @Path("/{id}")
    public Response deleteAblesung(@PathParam("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);
            Ablesung a = ablesungsModel.get(uuid);
            if(ablesungsModel.delete(uuid)){
                return Response.status(Response.Status.OK).entity(a).build();
            }
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Konnte keine UUID aus der ID machen").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Nutzer nicht gefunden").build();
    }


    @GET
    @Path("/{id}")
    public Response getAblesungById(@PathParam("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);

            Ablesung a = ablesungsModel.get(uuid);
            if(a!=null){
                return Response.status(Response.Status.OK).entity(a).build();
            }
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.NOT_FOUND).entity("Konnte keine UUID aus der ID machen").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Ablesung nicht gefunden").build();
    }

    @GET
    @Path("/vorZweiJahrenHeute")
    public Response getAblesungenTwoYears() {
        List<Ablesung> flat = ablesungsModel.getAll().stream()
                .filter(e -> e.getDatum().getYear() >= LocalDate.now().getYear() - 2)
                .collect(Collectors.toList());
        return Response.status(Response.Status.OK).entity(flat).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAblesung(Ablesung ablesung) {
        if (ablesung != null) {
            if (kundenModel.get(ablesung.getKunde().getId())==null) {
                return Response.status(Response.Status.NOT_FOUND).entity(ablesung).build();
            }
            ablesungsModel.add(ablesung);
            return Response.status(Response.Status.CREATED).entity(ablesung).build();
        }


        return Response.status(Response.Status.BAD_REQUEST).entity("Keine Ablesung erhalten").build();
    }
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateAblesungdaten(Ablesung ablesung){
        if (ablesung == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Bitte Ablesungsdaten eingeben").build();
        }

        Ablesung a = ablesungsModel.update(ablesung);
        if(a!=null){
            return Response.status(Response.Status.OK).entity("Update der Ablesung wurde durchgeführt").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Keine Ablesung gefunden").build();
    }

    @GET
    public Response get(@QueryParam("kunde") String kunde,
        @QueryParam("beginn") String beginn,
        @QueryParam("ende") String ende) {
            UUID uuid = null;
            try {
                uuid = UUID.fromString(kunde);
            } catch (Exception ignored) {}

            try{
                LocalDate beginnDate = LocalDate.MIN;
                LocalDate endeDate = LocalDate.MIN;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
                
                if(beginn != null){
                    beginnDate = LocalDate.parse(beginn, formatter);
                }
                if(ende != null){
                    endeDate = LocalDate.parse(ende, formatter);
                }

                System.out.println(beginn);
                System.out.println(ende);

                Stream<Ablesung> stream = ablesungsModel.getAll().stream();

                if(uuid != null){
                    final UUID finalUuid = uuid;
                    stream = stream.filter(e-> e.getKunde()!=null && e.getKunde().getId().equals(finalUuid));
                }
                if(!beginnDate.equals(LocalDate.MIN)){
                    final LocalDate finalBeginnDate = beginnDate;
                    stream = stream.filter(e-> (e.getDatum().isAfter(finalBeginnDate)||e.getDatum().equals(finalBeginnDate)));
                }
                if(!endeDate.equals(LocalDate.MIN)){
                    final LocalDate finalendeDate = endeDate;
                    stream = stream.filter(e-> e.getDatum().isBefore(finalendeDate)||e.getDatum().equals(finalendeDate));
                }
    
                List<Ablesung> flat = stream.collect(Collectors.toList());
                
                return Response.status(Response.Status.OK).entity(flat).build();
                
    

            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity("Falsches Datums Format " + kunde).build();
            }
    }
}
