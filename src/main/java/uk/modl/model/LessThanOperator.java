package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class LessThanOperator implements Operator {
    public static LessThanOperator instance = new LessThanOperator();

    private LessThanOperator() {
    }
}
