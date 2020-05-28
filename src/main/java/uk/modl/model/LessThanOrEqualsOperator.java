package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class LessThanOrEqualsOperator implements Operator {

    public static LessThanOrEqualsOperator instance = new LessThanOrEqualsOperator();

    private LessThanOrEqualsOperator() {
    }

}
