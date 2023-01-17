package com.schule.server.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@Path("ablesungen")
public class AblesungsResource {
    private static AblesungsModel ablesungsModel = AblesungsModel.getInstance();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    private Response updateAblesungdaten(Ablesung ablesung){
        if (ablesung == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Bitte Ablesungsdaten eingeben").build();
        }
        for (List<Ablesung> entry : ablesungsModel.getAblesungsMap().values()) {
            for (Ablesung ablesungEntry : entry) {
                if(ablesung.getId().equals(ablesungEntry.getId())){
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
