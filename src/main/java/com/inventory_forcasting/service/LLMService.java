package com.inventory_forcasting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.LLMdata;
import com.inventory_forcasting.models.StructuredSalesData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LLMService {

    private final AuditService auditService;
    @Value("${gemini-key}")
    private String GOOGLE_API_KEY;

    public StructuredSalesData parseUnstructuredData(LLMdata data){
        auditService.logEvent(EventType.LLM_PARSE, "POST /llm/parse", "LLM parsing unstructured data");
        GenerateContentResponse response;
        StructuredSalesData structuredSalesData;
        switch (data.getModel()) {
            case GEMINI_2_5_FLASH:
                response= geminiGenerateContent(data);
                try {
                    structuredSalesData=parseString(Objects.requireNonNull(response.text()));
                    structuredSalesData.setModel(data.getModel());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error parsing LLM response  "+e);
                }
                return structuredSalesData;
            default:
                return null;
        }


    }
    private StructuredSalesData parseString(String llmResponse) throws JsonProcessingException {
        llmResponse = llmResponse
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        JsonNode json = mapper.readTree(llmResponse);
        StructuredSalesData structuredSalesData = mapper.readValue(llmResponse, StructuredSalesData.class);;
        return structuredSalesData;
    }

    private GenerateContentResponse geminiGenerateContent(LLMdata data){
        Client client = Client.builder()
                .apiKey(GOOGLE_API_KEY)
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        data.getText(),
                        null);

//        System.out.println(response.text());
        return response;
    }

}
