// BITS ID: 2024sl70011
package com.bits.leave;

public class LeaveCalculator {

    // Calculate remaining leave balance
    public static int calculateLeaveBalance(int totalLeaves, int leavesTaken) {
        if (totalLeaves < 0 || leavesTaken < 0 || leavesTaken > totalLeaves) {
            throw new IllegalArgumentException("Invalid leave details");
        }

        return totalLeaves - leavesTaken;
    }

    // Calculate leave encashment amount
    public static double calculateLeaveEncashment(int remainingLeaves, double dailySalary) {
        if (remainingLeaves < 0 || dailySalary < 0) {
            throw new IllegalArgumentException("Invalid salary or leave balance");
        }

        return remainingLeaves * dailySalary;
    }
}
