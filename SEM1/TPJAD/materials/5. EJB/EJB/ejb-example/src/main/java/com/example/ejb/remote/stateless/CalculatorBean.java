package com.example.ejb.remote.stateless;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;


@Stateless
@Remote(RemoteCalculator.class)
public class CalculatorBean implements RemoteCalculator {

    @Override
    public int add(int a, int b) {
        System.out.println("CalculatorBean: Calculating: " + a + "+" + b);
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        System.out.println("CalculatorBean: Calculating: " + a + "-" + b);
        return a - b;
    }
}
