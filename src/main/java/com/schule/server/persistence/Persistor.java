package com.schule.server.persistence;

import java.util.List;

public interface Persistor<T> {
  void save(List<T> list);
  List<T> load();
}
