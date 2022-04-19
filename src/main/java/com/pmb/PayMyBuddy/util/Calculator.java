package com.pmb.PayMyBuddy.util;



import static com.pmb.PayMyBuddy.constants.Constants.FEE_PERCENTAGE;

public class Calculator {

    public static double feeCalculator( double amount){

        return (amount* FEE_PERCENTAGE) / 100;
    }

    public static double totalCalculator(double amount){
        return feeCalculator(amount)+amount;
    }
}
