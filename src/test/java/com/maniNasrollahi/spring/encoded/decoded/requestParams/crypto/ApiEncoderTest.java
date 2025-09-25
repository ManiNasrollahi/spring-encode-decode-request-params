package com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApiEncoderTest {

    private ApiEncoder apiEncoder;

    @BeforeEach
    void setUp() throws Exception {
        apiEncoder = new ApiEncoder();
    }

    @Test
    void testEncodeDecode() throws Exception {
        String original = "Super secret phrase that has all companies data!";
        String encoded = apiEncoder.encode(original);
        assertNotEquals(original, encoded); // must be different
        assertEquals(original, apiEncoder.decode(encoded)); // must decode back
    }

    @Test
    void testDecodeInvalidString() {
        String invalid = "%%%notbase64%%%";
        assertThrows(Exception.class, () -> apiEncoder.decode(invalid));
    }
}
