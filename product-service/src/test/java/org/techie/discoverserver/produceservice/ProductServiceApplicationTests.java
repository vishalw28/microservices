package org.techie.discoverserver.produceservice;

import org.techie.produceservice.dto.ProductRequest;
import org.techie.produceservice.repository.ProductRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5.0.21");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        String payload = objectMapper.writeValueAsString(getProductRequest());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(payload)
        ).andExpect(status().isCreated());

        Assert.assertTrue(productRepository.findAll().size() == 1);

    }

    private ProductRequest getProductRequest() {
        return ProductRequest.builder().
            name("iPhone 14").
            description("iPhone 14").
            price(BigDecimal.valueOf(250))
            .build();
    }


}
