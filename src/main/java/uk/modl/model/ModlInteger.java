package uk.modl.model;

import lombok.Value;

/**
 * An object to match the ANTLR4 MODL grammar
 */
@Value
public class ModlInteger implements ModlNumber {
  int value;
}
