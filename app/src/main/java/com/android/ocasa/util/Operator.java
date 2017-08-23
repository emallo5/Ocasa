package com.android.ocasa.util;

/**
 * Created by leandro on 12/7/17.
 */

public enum Operator {

    EQUALS("=") {
        @Override
        public boolean operate(String pivot, String other) {
            if (pivot==null) return true;
            return pivot.equalsIgnoreCase(other);
        }
    }, DIFFERENT("<>") {
        @Override
        public boolean operate(String pivot, String other) {
            return !pivot.equalsIgnoreCase(other);
        }
    }, AND("AND") {
        @Override
        public boolean operate(boolean first, boolean second) {
            return first && second;
        }
    }, OR("OR") {
        @Override
        public boolean operate(boolean first, boolean second) {
            return first || second;
        }
    };

    String type;

    Operator (String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Operator findOperator(String type) {
        for (Operator o : Operator.values())
            if (type.equals(o.getType())) return o;
        return EQUALS; // por defecto va el "igual"
    }

    public boolean operate (String pivot, String other) {
        return false;
    }

    public boolean operate (boolean first, boolean second) {
        return false;
    }
}
