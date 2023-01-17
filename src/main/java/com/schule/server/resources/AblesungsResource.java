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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Path("ablesungen")
public class AblesungsResource {
    private static AblesungsModel ablesungsModel = AblesungsModel.getInstance();
    private static KundenModel kundenModel = KundenModel.getInstance();

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
        if (ablesung != null) {
            if (!kundenModel.getData().contains(ablesung.getKunde())) {
                return Response.status(Response.Status.NOT_FOUND).entity(ablesung).build();
            } else if (ablesungsModel.getAblesungsMap().get(ablesung.getKunde())==null){
                ablesungsModel.getAblesungsMap().put(ablesung.getKunde().getId(), new ArrayList<>());
            }
            ablesungsModel.getAblesungsMap().get(ablesung.getKunde().getId()).add(ablesung);
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
        for (List<Ablesung> entry : ablesungsModel.getAblesungsMap().values()) {
            for (Ablesung ablesungEntry : entry) {
                if(ablesungEntry.getId().equals(ablesung.getId())){
                    ablesungEntry.setZaehlernummer(ablesung.getZaehlernummer());
                    ablesungEntry.setDatum(ablesung.getDatum());
                    ablesungEntry.setKunde(ablesung.getKunde());
                    ablesungEntry.setKommentar(ablesung.getKommentar());
                    ablesungEntry.setNeuEingebaut(ablesung.isNeuEingebaut());
                    ablesungEntry.setZaehlerstand(ablesung.getZaehlerstand());
                    return Response.status(Response.Status.OK).entity("Update der Ablesung wurde durchgeführt").build();
                }
            }
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Keine Ablesung gefunden").build();
    }

}
