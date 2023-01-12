package com.schule.server;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Kunde {
    private UUID id;
    private String name;
    private String vorname;
    
    public Kunde(String name, String vorname) {
        this.name = name;
        this.vorname = vorname;
    }

    
}
