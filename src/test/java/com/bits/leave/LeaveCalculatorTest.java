package com.bits.leave;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeaveCalculatorTest {

    // Test 1: Verify remaining leave balance
    @Test
    public void testLeaveBalance() {
        int balance = LeaveCalculator.calculateLeaveBalance(20, 5);
        assertEquals(15, balance);
    }

    // Test 2: Verify leave encashment amount
    @Test
    public void testLeaveEncashment() {
        double amount = LeaveCalculator.calculateLeaveEncashment(10, 1000.0);
        assertEquals(10000.0, amount, 0.001);
    }

    // Test 3: Verify invalid leave details
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLeaveDetails() {
        LeaveCalculator.calculateLeaveBalance(20, 25);
    }
}
