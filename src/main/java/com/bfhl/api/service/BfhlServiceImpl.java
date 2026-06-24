package com.bfhl.api.service;

import com.bfhl.api.dto.BfhlRequest;
import com.bfhl.api.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BfhlServiceImpl implements BfhlService {

    private static final String USER_ID      = "hellcat1234";
    private static final String EMAIL        = "hellcat@hellcat.com";
    private static final String ROLL_NUMBER  = "hell1234";

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


    
    private boolean isNumeric(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    
    private boolean isAlphabetic(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    
    private String buildConcatString(List<String> alphabetTokens) {

        StringBuilder flat = new StringBuilder();
        for (String token : alphabetTokens) {
            flat.append(token);
        }

        String reversed = flat.reverse().toString();

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
