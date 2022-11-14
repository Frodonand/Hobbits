package com.schule.persistence;

import java.util.List;

public interface Persistor<T> {
  <T> void save(List<T> list);
  List<T> load();
}
