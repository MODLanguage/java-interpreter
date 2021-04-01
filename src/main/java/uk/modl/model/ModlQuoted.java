package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlQuoted implements ModlPrimitive {
  @NonNull
  String value;
}
