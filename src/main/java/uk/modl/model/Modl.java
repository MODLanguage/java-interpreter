package uk.modl.model;

import java.util.List;

import lombok.NonNull;
import lombok.Value;

@Value
public class Modl {
  @NonNull
  List<ModlStructure> structures;
}
