package com.schule.server.resources;

import java.util.UUID;

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

}
