package com.schule.server.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Path("kunden")
public class KundenResource {
    private static KundenModel kundenModel = KundenModel.getInstance();
    private static AblesungsModel ablesungsModel = AblesungsModel.getInstance();


    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    public Response deleteCustomer(@PathParam("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);
            Optional<Kunde> kundeOptional = kundenModel.getData().stream()
                .filter(k -> k.getId()
                .equals(uuid))
                .findFirst();

            if(kundeOptional.isPresent()){
                Kunde kunde = kundeOptional.get();
                List<Ablesung> kundenAblesung = ablesungsModel.getAblesungsList().stream()
                    .filter(k -> k.getKunde()!=null && k.getKunde().getId().equals(uuid))
                    .peek(e-> e.setKunde(null))
                    .collect(Collectors.toList());
        
                kundenModel.getData().removeIf(k -> k.getId().equals(uuid));

                Map<UUID,List<Ablesung>> result = new HashMap<>();
                result.put(kunde.getId(), kundenAblesung);

                return Response.status(Response.Status.OK).entity(result).build();
            }
        }catch(IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
        }catch(Exception e){
            e.printStackTrace();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
    }

    @Path("/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    public Response getCustomerById(@PathParam("id") String id) {
        try{
            UUID uuid = UUID.fromString(id);
            Optional<Kunde> kunde = kundenModel.getData().stream().filter(e->e.getId().equals(uuid)).findFirst();
            if(kunde.isPresent()){
            return Response.status(Response.Status.OK).entity(kunde.get()).build();
            }
        }catch(IllegalArgumentException e) {}
        return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKundendaten(){
        return Response.status(Response.Status.OK).entity(kundenModel.getData()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateKundendaten(Kunde k){
        if (k == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Kunde angeben").build();
        }
        for (Kunde kd:kundenModel.getData()) {
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
        if(kunde!=null){
            kunde.setId(UUID.randomUUID());
            kundenModel.getData().add(kunde);
            return Response.status(Response.Status.CREATED).entity(kunde).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Kein Kunden erhalten").build();
    }
}
