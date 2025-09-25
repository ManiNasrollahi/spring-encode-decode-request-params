Spring Encode-Decode Request Params
===================================

Spring Encode-Decode Request Params is a Spring Boot project demonstrating how to encode and decode query parameters, path variables, and request bodies in controllers. It uses AES encryption with Base64 URL-safe encoding and custom Spring annotations to handle encoded input and output seamlessly. This project serves as a reference or starting point for implementing secure parameter handling in Spring applications.

Features
--------

- Encode and decode query parameters transparently.
- Encode and decode path variables automatically.
- Encode and decode request bodies with JSON support.
- Integrates seamlessly with Spring MVC through custom annotations and argument resolvers.

Usage
-----

1. Controller Example:
```java
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/param")
    public String getParam(@DecodedRequestParam("id") String id) {
        return "Decoded param: " + id;
    }

    @GetMapping("/path/{userId}")
    public String getPath(@DecodedPathVariable("userId") Long userId) {
        return "Decoded path variable: " + userId;
    }

    @PostMapping("/body")
    public String postBody(@DecodedRequestBody User user) {
        return "Decoded body: " + user.getName() + ", age: " + user.getAge();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        private String name;
        private int age;

    }
}
```
2. Test Units:
```java
@Test
    void testDecodedRequestParam() throws Exception {
        String encodedId = apiEncoder.encode("12345");

        mockMvc.perform(get("/api/test/param")
                        .param("id", encodedId))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded param: 12345"));
    }

    @Test
    void testDecodedPathVariable() throws Exception {
        String encodedUserId = apiEncoder.encode("42");

        mockMvc.perform(get("/api/test/path/{userId}", encodedUserId))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded path variable: 42"));
    }

    @Test
    void testDecodedRequestBody() throws Exception {
        TestController.User user = new TestController.User("alice", 25);
        ObjectMapper objectMapper = new ObjectMapper();
        String encodedUser = objectMapper.writeValueAsString(user);
        String encodedJson = apiEncoder.encode(encodedUser);

        mockMvc.perform(post("/api/test/body")
                        .content(encodedJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Decoded body: alice, age: 25"));
    }
```
Packages
--------
```java
com.maninasrolahi.spring.encoded.decoded.requestparams
 ├── annotations       # @DecodedRequestParam, @DecodedPathVariable, @DecodedRequestBody
 ├── resolvers         # HandlerMethodArgumentResolvers
 ├── crypto            # ApiEncoder (AES encode/decode)
 ├── config            # Auto-configuration
 ├── exceptions        # DecodingException
 └── testcontroller    # TestController for manual testing
```

Contributing
------------

Contributions are welcome! Feel free to open issues and pull requests.
