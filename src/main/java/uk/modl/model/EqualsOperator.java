package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class EqualsOperator implements Operator {

    public static final EqualsOperator instance = new EqualsOperator();

    private EqualsOperator() {
    }

}
