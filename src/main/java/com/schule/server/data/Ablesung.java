package com.schule.server.data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value = "ablseung")
public class Ablesung {

    @JsonProperty
    private UUID id;
    @JsonProperty
    private String zaehlernummer;
    @JsonProperty
    private LocalDate datum;
    @JsonProperty
    private Kunde kunde;
    @JsonProperty
    private String kommentar;
    @JsonProperty
    private boolean neuEingebaut;
    @JsonProperty
    private Number zaehlerstand;

    public Ablesung(String zaehlernummer, LocalDate datum, Kunde kunde, String kommentar, boolean neuEingebaut,
    Number zaehlerstand) {
        id = UUID.randomUUID();
        this.zaehlernummer = zaehlernummer;
        this.datum = datum;
        this.kunde = kunde;
        this.kommentar = kommentar;
        this.neuEingebaut = neuEingebaut;
        this.zaehlerstand = zaehlerstand;
    }
}
