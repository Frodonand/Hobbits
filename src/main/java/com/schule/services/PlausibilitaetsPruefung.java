package com.schule.services;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class PlausibilitaetsPruefung {
    
    public static String machePlausabilitaetspruefung(String kundenummerText, String zaehlernummerText,
    String zaehlerstandText,boolean eingebautCheck, LocalDate date)  {
        String s ="";
        /*try{
            Integer.parseInt(kundenummerText);
            if (kundenummerText.length() != 8) {
                s +="Kundennummer zu lang oder zu kurz \n";
            }
        }catch(NumberFormatException e){
            s+="Kundennummer muss eine ganze Zahl sein \n";
        }*/
        if (zaehlernummerText.length() != 8) {
            s += "Zählernummer zu lang oder zu kurz\n";
        }else if (!checkIfStringIsASCII(zaehlernummerText)) {
            s += "Die Zählernummer enthält nicht ASCII Zeichen.\n";
        }
        if (!checkIfDateIsInvalid(date)) {
            s +="Das Datum ist entweder in der Zukunft oder älter als 2 Woche\n";
        }

        try{
        if (Integer.parseInt(zaehlerstandText) > 1000000 && s.equals("")) {
            int dialog = JOptionPane.showConfirmDialog(null, "Der Wert für den Zählerstand ist sehr hoch. Ist das gewollt?","Wert überprüfen",JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION) {
                s = "Daten wurden nicht gespeichet";
            }
        }else if (eingebautCheck && Integer.parseInt(zaehlerstandText) > 1000 && s.equals("")) {
            int dialog = JOptionPane.showConfirmDialog(null, "Der Wert für den Zählerstand ist für einen neu eingebauten Zähler sehr hoch. Ist das gewollt?","Wert überprüfen",JOptionPane.YES_NO_OPTION);
            if (dialog == JOptionPane.NO_OPTION) {
                s = "Daten wurden nicht gespeichet";
            }
        }
    
        }catch(NumberFormatException e){
            s+="Zählerstand muss eine ganze Zahl sein \n";
        }
        return s;
    }
    
    private static boolean checkIfStringIsASCII(String stringToCheck) {
      String upperString = stringToCheck.toUpperCase();
      for (int pos = 0; pos < upperString.length(); pos++) {
          int ascii = upperString.charAt(pos);
          if ((ascii < 48 || ascii > 57) && (ascii < 65 || ascii > 90)) {
              return false;
          }
      }
      return true;
    }
    
    private static boolean checkIfDateIsInvalid(LocalDate date) {
      boolean isInvalidDate = false;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      String enteredDay = date.getDayOfMonth() + "";
      String enteredMonth = date.getMonthValue() + 1 + "/";
      String enteredYear = date.getYear() + "/";
      try{
      String enteredDateStringAsString = enteredYear + enteredMonth + enteredDay;
      Date enteredDate = sdf.parse(enteredDateStringAsString);
    
      String tooEarlyString = subtractWeeksFromDate(2);
      Date tooEarlyDate = sdf.parse(tooEarlyString);
      
      if (enteredDate.after(Now()) || enteredDate.before(tooEarlyDate)) {
          isInvalidDate = true;
      }
      }catch(Exception e){
          return false;
      }
      return isInvalidDate;
    }
    
    private static String subtractWeeksFromDate(int weeks) {
      String wantedDate;
      Instant instant = Instant.now();
      ZoneId zone = ZoneId.of("Europe/Paris");
      ZonedDateTime zdtNow =instant.atZone(zone);
    
      ZonedDateTime zdtWeeksAgo = zdtNow.minusWeeks(weeks);
    
      String year = String.valueOf(zdtWeeksAgo.getYear());
      String month = String.valueOf(zdtWeeksAgo.getMonth().ordinal()+1);
      String day = String.valueOf(zdtWeeksAgo.getDayOfMonth());
    
      wantedDate = year + "/" + month + "/" + day;
    
      return wantedDate;
    }

    private static Date Now() {
        return Calendar.getInstance().getTime();
      }
    
}
