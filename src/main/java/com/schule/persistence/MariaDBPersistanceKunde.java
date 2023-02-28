package com.schule.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.schule.server.data.Kunde;

public class MariaDBPersistanceKunde{
  
  static final String DB_PATH = "localhost:3306/hobbits";
  //macht man nicht so
  static final String DB_USER = "root";
  static final String DB_PASSWORD = "root";


  public MariaDBPersistanceKunde (){
  }

  public boolean create(Kunde kunde){
    String statementString = "insert into kunde (uuid,vorname,nachname) values " + kunde.toSQLValue() ;
    System.out.println(statementString);
    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {

        return statement.execute(statementString);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
  }

  public Kunde update(Kunde kunde){
    String statementString = "update kunde set "+
      "nachname=\"" +kunde.getName() +"\","+
      "vorname=\"" +kunde.getVorname() +"\""+
      "where uuid=\""+kunde.getId()+"\"";

      try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
          Statement statement = connection.createStatement();) {

          int result = statement.executeUpdate(statementString);
          if(result>0){
            return kunde;
          }
          return null;
      } catch (SQLException e) {
          e.printStackTrace();
      }
      return null;
  }

  public boolean delete(UUID uuid){
    String statementString = "delete from kunde where uuid=\"" + uuid +"\"" ;
    System.out.println(statementString);
    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {

        return statement.execute(statementString);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
  }

  public List<Kunde> getAll(){
    List<Kunde> list = new ArrayList<Kunde>();
    String statementKundenString = "select * from kunde";

    try ( Connection connection = DriverManager.getConnection("jdbc:mariadb://"+DB_PATH+"?user="+DB_USER+"&password="+DB_PASSWORD);
        Statement statement = connection.createStatement();) {
        ResultSet kunden = statement.executeQuery(statementKundenString);
        while(kunden.next()){
          UUID uuid = UUID.fromString(kunden.getString("uuid"));
          String name = kunden.getString("nachname");
          String vorname = kunden.getString("vorname");
          list.add(new Kunde(uuid,name,vorname));
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
  }
}
