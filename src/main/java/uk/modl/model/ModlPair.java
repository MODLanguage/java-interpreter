package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlPair implements ModlStructure {
  @NonNull
  ModlText key;

  @NonNull
  ModlValue value;
}
