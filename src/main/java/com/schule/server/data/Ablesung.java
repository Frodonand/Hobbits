package com.schule.server.data;

import java.time.LocalDate;
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
        id = UUID.randomUUID();
        this.zaehlernummer = zaehlernummer;
        this.datum = datum;
        this.kunde = kunde;
        this.kommentar = kommentar;
        this.neuEingebaut = neuEingebaut;
        this.zaehlerstand = zaehlerstand;
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public String toSQLValue() {
        String sqlString = "(";
        
        if(id==null||kunde==null||kunde.getId()==null)return "";

        sqlString+= "\"" + id.toString().replace("-", "")+"\",";
        sqlString+= "\"" + zaehlernummer+"\",";
        sqlString+= "\"" + datum+"\",";
        sqlString+= "\"" + kunde.getId().toString().replace("-", "")+"\",";
        sqlString+= "\"" + kommentar+"\",";
        sqlString+= "\"" + (neuEingebaut?1:0)+"\",";
        sqlString+= zaehlerstand+")";

        return sqlString;
    }

}
