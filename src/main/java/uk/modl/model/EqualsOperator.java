package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.modl.ancestry.Child;
import uk.modl.ancestry.Parent;

@ToString
@EqualsAndHashCode
public class EqualsOperator implements Operator, Parent, Child {

    public static EqualsOperator instance = new EqualsOperator();

    private EqualsOperator() {
    }

}
