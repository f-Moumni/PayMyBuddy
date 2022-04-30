package com.pmb.PayMyBuddy.util;




import org.springframework.stereotype.Service;

import static com.pmb.PayMyBuddy.constants.Constants.FEE_PERCENTAGE;

/**
 *  Calculator class for the math calculations
 */
@Service
public class Calculator {
    /**
     * calculated free of transaction
     * @param amount
     * @return fee
     */
    public  double feeCalculator( double amount){
        return (amount* FEE_PERCENTAGE) / 100;
    }

    /**
     * calculates the total price of the operation
     * @param amount
     * @return total
     */
    public  double totalCalculator(double amount){
        return feeCalculator(amount)+amount;
    }
}
