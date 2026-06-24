package com.bfhl.api.service;

import com.bfhl.api.dto.BfhlRequest;
import com.bfhl.api.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Core implementation of BfhlService.
 * Processes the incoming string array and separates it into
 * numbers (odd/even), alphabets, and special characters,
 * then computes derived fields like sum and concat_string.
 */
@Service
public class BfhlServiceImpl implements BfhlService {

    // --- Hardcoded user identity fields (change these to your own details) ---
    private static final String USER_ID      = "john_doe_17091999";
    private static final String EMAIL        = "john@xyz.com";
    private static final String ROLL_NUMBER  = "ABCD123";

    @Override
    public BfhlResponse processData(BfhlRequest request) {

        List<String> input = request.getData();

        List<String> evenNumbers      = new ArrayList<>();
        List<String> oddNumbers       = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         numericalSum     = 0;

        for (String token : input) {
            if (isNumeric(token)) {
                long value = Long.parseLong(token);
                numericalSum += value;
                if (value % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlphabetic(token)) {
                // Store each element uppercased, preserving multi-char strings like "ABCD"
                alphabets.add(token.toUpperCase());
            } else {
                specialChars.add(token);
            }
        }

        BfhlResponse response = new BfhlResponse();
        response.setSuccess(true);
        response.setUserId(USER_ID);
        response.setEmail(EMAIL);
        response.setRollNumber(ROLL_NUMBER);
        response.setEvenNumbers(evenNumbers);
        response.setOddNumbers(oddNumbers);
        response.setAlphabets(alphabets);
        response.setSpecialCharacters(specialChars);
        response.setSum(String.valueOf(numericalSum));
        response.setConcatString(buildConcatString(alphabets));

        return response;
    }

    // ---------- helpers ----------

    /**
     * Returns true when every character in the token is a digit
     * (handles multi-digit strings like "334").
     */
    private boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true when every character in the token is a letter
     * (handles multi-char strings like "ABCD").
     */
    private boolean isAlphabetic(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds the concat_string:
     *  1. Flatten all alphabetic tokens into one character sequence.
     *  2. Reverse the sequence.
     *  3. Apply alternating caps — index 0 → uppercase, index 1 → lowercase, etc.
     *
     * Example: alphabets = ["A", "ABCD", "DOE"]
     *   flattened  →  A A B C D D O E
     *   reversed   →  E O D D C B A A
     *   alt-caps   →  E o D d C b A a  →  "EoDdCbAa"
     */
    private String buildConcatString(List<String> alphabetTokens) {
        // Flatten all alphabet tokens into a single char list
        StringBuilder flat = new StringBuilder();
        for (String token : alphabetTokens) {
            flat.append(token);
        }

        // Reverse the whole thing
        String reversed = flat.reverse().toString();

        // Apply alternating caps
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }
}
