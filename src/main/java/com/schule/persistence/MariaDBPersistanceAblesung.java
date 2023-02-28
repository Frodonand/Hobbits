package com.schule.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Ablesung;
import com.schule.server.data.Kunde;

public class MariaDBPersistanceAblesung{
  
  static final String DB_PATH = "localhost:3306/hobbits";
  //macht man nicht so
  static final String DB_USER = "root";
  static final String DB_PASSWORD = "root";


  public MariaDBPersistanceAblesung (){
  }

  public boolean create(Ablesung ablesung){
    String statementString = "insert into ablesung (uuid,zaehlernummer,datum,kunde,kommentar,neu_eingebaut,zaehlerstand) values " + ablesung.toSQLValue() ;
    System.out.println(statementString);
    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {

        return statement.execute(statementString);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
  }

  public Ablesung update(Ablesung ablesung){
    String statementString = "update ablesung set "+
      "zaehlernummer=\"" +ablesung.getZaehlernummer() +"\","+
      "datum=\"" +ablesung.getDatum() +"\","+
      "kunde=\"" +ablesung.getKunde().getId() +"\","+
      "kommentar=\"" +ablesung.getKommentar() +"\","+
      "neu_eingebaut=\"" + (ablesung.isNeuEingebaut()?1:0) +"\","+
      "zaehlerstand=" +ablesung.getZaehlerstand() +" " +
      "where uuid=\""+ablesung.getId()+"\"";

      try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
          Statement statement = connection.createStatement();) {

          int result = statement.executeUpdate(statementString);
          if(result>0){
            return ablesung;
          }
          return null;
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return null;
  }

  public boolean delete(UUID uuid){
    String statementString = "delete from ablesung where uuid=\"" + uuid +"\"" ;
    System.out.println(statementString);
    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {

        return statement.execute(statementString);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
  }

  public List<Ablesung> getAll(){
    List<Ablesung> list = new ArrayList<Ablesung>();

    String statementAblesungString = "select * from ablesung";
    String statementKundenString = "select * from kunde";

    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {
        ResultSet kunden = statement.executeQuery(statementKundenString);
        List<Kunde> kundenliste = new ArrayList<>();
        while(kunden.next()){
          UUID uuid = UUID.fromString(kunden.getString("uuid"));
          String name = kunden.getString("nachname");
          String vorname = kunden.getString("vorname");
          kundenliste.add(new Kunde(uuid,name,vorname));
        }

        ResultSet ablesung = statement.executeQuery(statementAblesungString);
        while(ablesung.next()){
          UUID uuid = UUID.fromString(ablesung.getString("uuid"));
          String zaehlernummer = ablesung.getString("zaehlernummer");
          Date datum = ablesung.getDate("datum");
          UUID uuidKunde = UUID.fromString(ablesung.getString("kunde"));
          String kommentar = ablesung.getString("kommentar");
          boolean neuEingebaut = ablesung.getBoolean("neu_eingebaut");
          float zaehlerstand = ablesung.getFloat("zaehlerstand");
          
          Kunde kunde =kundenliste.stream().filter(e->e.getId().equals(uuidKunde)).findFirst().orElse(null);

          list.add(new Ablesung(uuid, zaehlernummer, datum.toLocalDate(), kunde, kommentar, neuEingebaut, zaehlerstand));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
  }
}
