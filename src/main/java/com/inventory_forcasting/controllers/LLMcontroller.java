package com.inventory_forcasting.controllers;

import com.inventory_forcasting.models.LLMdata;
import com.inventory_forcasting.models.LLModel;
import com.inventory_forcasting.models.StructuredSalesData;
import com.inventory_forcasting.service.LLMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/llm")
public class LLMcontroller {

    private final LLMService llmService;

    @Autowired
    public LLMcontroller(LLMService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/parse")
    public ResponseEntity<Object> parseUnstructuredData(@RequestBody String text){
        LLMdata data = LLMdata.builder().text(text)
                .model(LLModel.GEMINI_2_5_FLASH).build();
        return ResponseEntity.ok(llmService.parseUnstructuredData(data));
    }

}
