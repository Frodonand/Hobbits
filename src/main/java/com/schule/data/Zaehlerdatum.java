package com.schule.data;

import java.util.Date;
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
    private Date datum;
    private boolean eingebaut;
    private int zaehlerstand;
    private String kommentar;

    @Override
    public String toString() {
        String s = "<html>Kundennummer: " + kundennummer + "<br>";
        s += "Zählerart: "+zaehlerart;
        s += " Zählernummer: " + zaehlernummer + "<br>";
        s += "Datum: " + datum;
        s += " Neu eingebaut: " + eingebaut;
        s += " Zählerstand: " + zaehlerstand + "<br>";
        s += "Kommentar: " + kommentar + "</html>";
        return s;
    }
}
