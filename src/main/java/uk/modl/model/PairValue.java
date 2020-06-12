package uk.modl.model;

import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;

public interface PairValue extends Parent, Child {

    Number numericValue();

}
