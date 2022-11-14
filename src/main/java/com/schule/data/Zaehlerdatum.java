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
}
