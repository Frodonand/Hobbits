package com.schule.server.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Kunde toDelete = kundenModel.get(uuid);

            if(toDelete!=null){
                List<Ablesung> kundenAblesung = ablesungsModel.getAll().stream()
                    .filter(k -> k.getKunde()!=null && k.getKunde().getId().equals(uuid))
                    .collect(Collectors.toList());
        
                kundenModel.delete(uuid);

                Map<UUID,List<Ablesung>> result = new HashMap<>();
                result.put(toDelete.getId(), kundenAblesung);

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
            Kunde kunde = kundenModel.get(uuid);
            if(kunde != null){
                return Response.status(Response.Status.OK).entity(kunde).build();
            }
        }catch(IllegalArgumentException e) {}
        return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKundendaten(){
        return Response.status(Response.Status.OK).entity(kundenModel.getAll()).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateKundendaten(Kunde k){
        if (k == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Kunde angeben").build();
        }
        Kunde updated = kundenModel.update(k);
        if(updated!=null){
            return Response.status(Response.Status.OK).entity("Update durchgeführt").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Kunde nicht gefunden").build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCustomer(Kunde kunde){
        if(kunde!=null){
            kunde.setId(UUID.randomUUID());
            kundenModel.add(kunde);
            return Response.status(Response.Status.CREATED).entity(kunde).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Kein Kunden erhalten").build();
    }
}
