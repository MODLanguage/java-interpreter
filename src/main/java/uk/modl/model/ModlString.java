package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlString implements ModlText, ModlPrimitive {
  @NonNull
  String value;
}
