package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlPair implements ModlStructure {
  @NonNull
  String key;

  @NonNull
  ModlStructure value;
}
