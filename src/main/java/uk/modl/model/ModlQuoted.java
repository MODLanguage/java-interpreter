package uk.modl.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlQuoted implements ModlText, ModlPrimitive {
  @NonNull
  String value;
}
