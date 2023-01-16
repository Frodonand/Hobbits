package com.schule.server.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSONPersistance<T> implements Persistor<T> {
  private String path = "target/zaehlerdaten.json";
  private Class<T> classToLoad;
  public JSONPersistance (Class<T> classToLoad){
    this.classToLoad = classToLoad;
  }

  public JSONPersistance (Class<T> classToLoad,String path){
    this.classToLoad = classToLoad;
    this.path = path;
  }

  @Override
  public void save(List<T> list) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    File file = new File(path);
    if(!file.exists()){
      File pathToFile = new File(path.substring(0,path.lastIndexOf("/")));
      System.out.println(pathToFile);
      pathToFile.mkdir();
    }
    try {
      mapper.writeValue(file, list);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<T> load() {
    List<T> list = new ArrayList<T>();
    File file = new File(path);
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      if(file.exists()){
      list = mapper.readValue(
          file,
          mapper.getTypeFactory().constructCollectionType(List.class, classToLoad)
        );
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return list;
  }
}
