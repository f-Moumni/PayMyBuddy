package com.pmb.PayMyBuddy.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.pmb.PayMyBuddy.constants.Constants.FEE_PERCENTAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() throws Exception {
        calculator= new Calculator();
    }
    @Test
    @Tag("Calculate")
    @DisplayName("fee calculator should return 0.5 % of given amount ")
    void feeCalculator_Test_shouldReturnTheTransactionFee() {
        //ACT
        double result = calculator.feeCalculator(20);
        //ASSERT
        assertThat(result).isEqualTo(0.1);

    }

    @Test
    @Tag("Calculate")
    @DisplayName("Total calculator should return Total of 0.5 % of given amount plus amount ")
    void TotalCalculator_Test_shouldReturnTheFinalCost() {
        //ACT
        double result = calculator.totalCalculator(20);
        //ASSERT
        assertThat(result).isEqualTo(20.1);

    }
}
