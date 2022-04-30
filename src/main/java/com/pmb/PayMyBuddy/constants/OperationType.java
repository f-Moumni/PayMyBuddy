package com.pmb.PayMyBuddy.constants;

/**
 * Operation type enum
 */
public enum OperationType {

    DEBIT ("DEBIT") ,CREDIT("CREDIT");
    private final String operationType;


    OperationType(String type) {
        this.operationType = type;
    }

    public String getOperationType() {
        return operationType;
    }
}
