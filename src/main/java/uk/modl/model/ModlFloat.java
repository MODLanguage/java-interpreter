package uk.modl.model;

import lombok.Value;

/**
 * An object to match the ANTLR4 MODL grammar
 */
@Value
public class ModlFloat implements ModlNumber {
  float value;
}
