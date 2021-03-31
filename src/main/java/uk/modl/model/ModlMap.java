package uk.modl.model;

import io.vavr.collection.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class ModlMap implements ModlValue, ModlStructure {
  @NonNull
  List<ModlPair> items;
}
