package com.pmb.PayMyBuddy.util;



import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.pmb.PayMyBuddy.constants.Constants.FEE_PERCENTAGE;
@Service
public class Calculator {

    public  double feeCalculator( double amount){
        return (amount* FEE_PERCENTAGE) / 100;
    }

    public  double totalCalculator(double amount){
        return feeCalculator(amount)+amount;
    }
}
