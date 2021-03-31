package uk.modl.interpreter;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import uk.modl.model.Modl;
import uk.modl.parser.antlr.MODLParser.ModlContext;

@UtilityClass
public class ModlParsedVisitor {

  public static final Modl visitModl(@NonNull final ModlContext ctx) {
    return null;
  }

}
