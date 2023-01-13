package com.schule.server.resources;

import java.util.ArrayList;
import java.util.List;

import com.schule.server.data.Kunde;

import jakarta.ws.rs.Path;


@Path("/kunden")
public class KundenResource {
    List<Kunde> kundenListe = new ArrayList<Kunde>();
}
