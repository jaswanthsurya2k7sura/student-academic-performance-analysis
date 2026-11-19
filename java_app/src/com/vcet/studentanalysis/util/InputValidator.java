package com.vcet.studentanalysis.util;

import com.vcet.studentanalysis.exception.InvalidMarkException;

/**
 * Utility for verifying inputs and enforcing domain rules.
 */
public class InputValidator {

    public static void validateMarks(double internal, double external) throws InvalidMarkException {
        if (internal < 0 || internal > 40) {
            throw new InvalidMarkException("Internal marks must be between 0 and 40. Provided: " + internal);
        }
        if (external < 0 || external > 60) {
            throw new InvalidMarkException("External/Semester marks must be between 0 and 60. Provided: " + external);
        }
    }

    public static void validateRegisterNo(String regNo) throws IllegalArgumentException {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Register Number cannot be empty.");
        }
        if (!regNo.matches("^[a-zA-Z0-9]{5,20}$")) {
            throw new IllegalArgumentException("Register Number format is invalid: " + regNo);
        }
    }
}
