// src/test/java/com/example/demo/RequestHandlerTest.java
package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestHandler.class)
class RequestHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Valid add → sum")
    void testAddSuccess() throws Exception {
        mockMvc.perform(get("/calculator/add")
                        .param("operands", "2,3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value(9.0));
    }

    @Test
    @DisplayName("Valid multiply → product")
    void testMultiplySuccess() throws Exception {
        mockMvc.perform(get("/calculator/multiply")
                        .param("operands", "2,3,4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value(24.0));
    }

    @Test
    @DisplayName("Valid average → average")
    void testAverageSuccess() throws Exception {
        mockMvc.perform(get("/calculator/average")
                        .param("operands", "2,4,6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.average").value(4.0));
    }

    @Test
    @DisplayName("Valid stdev → stdev")
    void testStdevSuccess() throws Exception {
        mockMvc.perform(get("/calculator/stdev")
                        .param("operands", "2,4,4,4,5,5,7,9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stdev").value(2.0));
    }

    @Test
    @DisplayName("Malformed operands → 400 + error JSON")
    void testBadOperands() throws Exception {
        mockMvc.perform(get("/calculator/add")
                        .param("operands", "1,,x,4"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid operands: [\"\",\"x\"]"))
                .andExpect(jsonPath("$.expected").value("comma-separated numbers only"));
    }

    @Test
    @DisplayName("Missing operands → 400 + missing-param JSON")
    void testMissingParam() throws Exception {
        mockMvc.perform(get("/calculator/add"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing required parameter: operands"))
                .andExpect(jsonPath("$.expected").value("Use ?operands=<comma-separated numbers>"));
    }
}
