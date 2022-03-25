package com.pmb.PayMyBuddy.util;

import com.pmb.PayMyBuddy.constants.Fee;

public class Calculator {

    public static double feeCalculator( double amount){

        return (amount* Fee.FEE_PERCENTAGE) / 100;
    }

    public static double totalCalculator(double amount){
        return feeCalculator(amount)+amount;
    }
}
