package uk.modl.model;

import java.util.List;

import lombok.NonNull;
import lombok.Value;

/**
 * An object to match the ANTLR4 MODL grammar
 */
@Value
public class Modl {
  @NonNull
  List<ModlStructure> structures;
}
