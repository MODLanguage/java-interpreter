package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

/**
 * An object to match the ANTLR4 MODL grammar
 */
@Value
public class ModlPair implements ModlStructure, ModlValue {
  @NonNull
  String key;

  @NonNull
  ModlStructure value;
}
