package com;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmericanExpressCardTest {

    @Test
    public void testValidateAmericanExpCardType() {
        AmericanExpress ax = new AmericanExpress("3.41E+14");
        String result = ax.CardTypeValidation("3.41E+14");
        assertEquals("AmericanExpress", result);
    }

    @Test
    public void testNoAmericanExpNumber(){
        AmericanExpress ax = new AmericanExpress("");
        String result = ax.CardTypeValidation("");
        assertEquals("Invalid", result);
    }

    @Test
    public void testSecondDigitAmericanExpNumber(){
        AmericanExpress ax = new AmericanExpress("351000000000000");
        String result = ax.CardTypeValidation("351000000000000");
        assertEquals("Invalid", result);
    }

    @Test
    public void Should_ReturnValid_When_SecondDigitIs7(){
        AmericanExpress ax = new AmericanExpress("371000000000000");
        String result = ax.CardTypeValidation("371000000000000");
        assertEquals("AmericanExpress", result);
    }

    @Test
    public void testAmericanExpWithSpecialCharacters(){
        AmericanExpress ax = new AmericanExpress("37100000,000000");
        String result = ax.CardTypeValidation("37100000,000000");
        assertEquals("Invalid", result);
    }

    @Test
    public void testAmericanExpWithWhiteSpacesInBetween(){
        AmericanExpress ax = new AmericanExpress("37100000  000000");
        String result = ax.CardTypeValidation("37100000  000000");
        assertEquals("Invalid", result);
    }

    @Test
    public void testAmericanExpWithOnlyWhiteSpaces(){
        AmericanExpress ax = new AmericanExpress("   ");
        String result = ax.CardTypeValidation("   ");
        assertEquals("Invalid", result);
    }

    @Test
    public void testAmericanExpWithFirstDigit(){
        AmericanExpress ax = new AmericanExpress("471000000000000");
        String result = ax.CardTypeValidation("471000000000000");
        assertEquals("Invalid", result);
    }

}