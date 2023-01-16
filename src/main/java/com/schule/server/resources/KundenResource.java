package com.schule.server.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("kunden")
public class KundenResource {
    private static List<Kunde> kundenListe = new ArrayList<Kunde>();

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    public Response deleteCustomer(@PathParam("id") String id) {
        System.out.println(id);
        try{
            UUID uuid = UUID.fromString(id);
            kundenListe.removeIf(k -> k.getId().equals(uuid));
            return Response.status(Response.Status.OK).build();
        }catch(IllegalArgumentException e) {
                return Response.status(Response.Status.NOT_FOUND).entity("ich bin toll").build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKundendaten(){
        return Response.status(Response.Status.OK).entity(kundenListe).build();
    }
    public static List<Kunde> getKundenListe() {
        return kundenListe;
    }
    public static void setKundenListe(List<Kunde> kundenListe) {
        KundenResource.kundenListe = kundenListe;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateKundendaten(Kunde k){
        if (k == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Kunde angeben").build();
        }
        for (Kunde kd:kundenListe) {
            if(k.getId().equals(kd.getId())){
                kd.setName(k.getName());
                kd.setVorname(k.getVorname());
                return Response.status(Response.Status.OK).entity("Update durchgeführt").build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(Kunde kunde){
        System.out.println(kunde);
        if(kunde!=null){
            kunde.setId(UUID.randomUUID());
            kundenListe.add(kunde);
            return Response.status(Response.Status.CREATED).entity(kunde).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Kein Kunden erhalten").build();
    }
}
