package com.schule.server.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(
  include = JsonTypeInfo.As.WRAPPER_OBJECT,
  use = JsonTypeInfo.Id.NAME
)
@JsonTypeName(value = "kunde")
public class Kunde {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String vorname;

    public Kunde(String name, String vorname) {
        id = UUID.randomUUID();
        this.name = name;
        this.vorname = vorname;
    }

    
}
