package uk.modl.model;

import java.util.List;

import lombok.NonNull;
import lombok.Value;

@Value
public class ModlArray implements ModlValue, ModlStructure {
  @NonNull
  List<ModlValue> items;
}
