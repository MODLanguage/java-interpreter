package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class EqualsOperator implements Operator {

    public static EqualsOperator instance = new EqualsOperator();

    private EqualsOperator() {
    }

}
