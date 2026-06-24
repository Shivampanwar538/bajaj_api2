package com.bfhl.api;

import com.bfhl.api.dto.BfhlRequest;
import com.bfhl.api.dto.BfhlResponse;
import com.bfhl.api.service.BfhlServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }


    @Test
    @DisplayName("Example A: mixed numbers, letters, special chars")
    void testExampleA() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of("1"),         res.getOddNumbers());
        assertEquals(List.of("334", "4"),  res.getEvenNumbers());
        assertEquals(List.of("A", "R"),    res.getAlphabets());
        assertEquals(List.of("$"),         res.getSpecialCharacters());
        assertEquals("339",               res.getSum());
        assertEquals("Ra",                res.getConcatString());
    }

    @Test
    @DisplayName("Example B: multiple special chars and letters")
    void testExampleB() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertEquals(List.of("5"),              res.getOddNumbers());
        assertEquals(List.of("2", "4", "92"),   res.getEvenNumbers());
        assertEquals(List.of("A", "Y", "B"),    res.getAlphabets());
        assertEquals(List.of("&", "-", "*"),    res.getSpecialCharacters());
        assertEquals("103",                     res.getSum());
        assertEquals("ByA",                     res.getConcatString());
    }

    @Test
    @DisplayName("Example C: only multi-char alphabetic tokens")
    void testExampleC() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertTrue(res.getOddNumbers().isEmpty());
        assertTrue(res.getEvenNumbers().isEmpty());
        assertEquals(List.of("A", "ABCD", "DOE"), res.getAlphabets());
        assertTrue(res.getSpecialCharacters().isEmpty());
        assertEquals("0",        res.getSum());
        assertEquals("EoDdCbAa", res.getConcatString());
    }

    @Test
    @DisplayName("Empty data array should return zeros and empty lists")
    void testEmptyInput() {
        BfhlRequest req = new BfhlRequest(List.of());
        BfhlResponse res = service.processData(req);

        assertTrue(res.isSuccess());
        assertEquals("0", res.getSum());
        assertEquals("",  res.getConcatString());
        assertTrue(res.getOddNumbers().isEmpty());
        assertTrue(res.getEvenNumbers().isEmpty());
    }

    @Test
    @DisplayName("Only numbers in input")
    void testOnlyNumbers() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("3", "6", "11", "100"));
        BfhlResponse res = service.processData(req);

        assertEquals(List.of("3", "11"), res.getOddNumbers());
        assertEquals(List.of("6", "100"), res.getEvenNumbers());
        assertEquals("120", res.getSum());
        assertTrue(res.getAlphabets().isEmpty());
        assertTrue(res.getSpecialCharacters().isEmpty());
        assertEquals("", res.getConcatString());
    }

    @Test
    @DisplayName("Alphabets are stored in uppercase regardless of input case")
    void testAlphabetUppercase() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("abc", "XyZ"));
        BfhlResponse res = service.processData(req);

        assertEquals(List.of("ABC", "XYZ"), res.getAlphabets());
    }


    @Test
    @DisplayName("POST /bfhl returns 200 for valid request")
    void testEndpointReturns200() throws Exception {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "b", "2"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").isNotEmpty())
                .andExpect(jsonPath("$.sum").value("3"));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 when data field is missing")
    void testMissingDataField() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for malformed JSON")
    void testMalformedJson() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not-json"))
                .andExpect(status().isBadRequest());
    }
}
