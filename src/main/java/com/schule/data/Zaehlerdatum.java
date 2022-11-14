package com.schule.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zaehlerdatum {

    private int kundennummer;
    private String zaehlerart;
    private String zaehlernummer;
    private String datum;
    private boolean eingebaut;
    private int zaehlerstand;
    private String kommentar;
}
