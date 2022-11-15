package com.schule.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSONPersistance<T> implements Persistor<T> {
  static final String JSON_PATH = "target/zaehlerdaten.json";
  private Class<T> classToLoad;
  public JSONPersistance (Class<T> classToLoad){
    this.classToLoad = classToLoad;
  }

  @Override
  public void save(List<T> list) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(JSON_PATH), list);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<T> load() {
    List<T> list = new ArrayList<T>();
    try {
      ObjectMapper mapper = new ObjectMapper();

      list =
        mapper.readValue(
          new File(JSON_PATH),
          mapper.getTypeFactory().constructCollectionType(List.class, classToLoad)
        );
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return list;
  }
}
