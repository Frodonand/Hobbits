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
import lombok.ToString;

@Getter
@Setter
@ToString
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
        this.zaehlernummer = zaehlernummer;
        this.datum = datum;
        this.kunde = kunde;
        this.kommentar = kommentar;
        this.neuEingebaut = neuEingebaut;
        this.zaehlerstand = zaehlerstand;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((zaehlernummer == null) ? 0 : zaehlernummer.hashCode());
        result = prime * result + ((datum == null) ? 0 : datum.hashCode());
        result = prime * result + ((kunde == null) ? 0 : kunde.hashCode());
        result = prime * result + ((kommentar == null) ? 0 : kommentar.hashCode());
        result = prime * result + (neuEingebaut ? 1231 : 1237);
        result = prime * result + ((zaehlerstand == null) ? 0 : zaehlerstand.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ablesung other = (Ablesung) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (zaehlernummer == null) {
            if (other.zaehlernummer != null)
                return false;
        } else if (!zaehlernummer.equals(other.zaehlernummer))
            return false;
        if (datum == null) {
            if (other.datum != null)
                return false;
        } else if (!datum.equals(other.datum))
            return false;
        if (kunde == null) {
            if (other.kunde != null)
                return false;
        } else if (!kunde.equals(other.kunde))
            return false;
        if (kommentar == null) {
            if (other.kommentar != null)
                return false;
        } else if (!kommentar.equals(other.kommentar))
            return false;
        if (neuEingebaut != other.neuEingebaut)
            return false;
        if (zaehlerstand == null) {
            if (other.zaehlerstand != null)
                return false;
        } else if (!zaehlerstand.equals(other.zaehlerstand))
            return false;
        return true;
    }

    
}
