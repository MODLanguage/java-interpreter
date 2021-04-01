package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlString implements ModlPrimitive {
  @NonNull
  String value;
}
