package com.inventory_forcasting.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMdata {
    String text;
    LLModel model;

    public LLMdata(String text, LLModel model){
        String context = """
                
                Context for above text:\
                You are a brilliant AI assistant, your task is to convert simple \
                text to structured data in JSON format. NOTHING ELSE! for example:\
                Input:
                { "data": "Sold 45 units of blue office chairs, Furniture category,
                on July 15th to the West warehouse." } \
                Output:
                {
                "productName": "blue office chairs",
                "category": "Furniture",
                "quantity": 45,
                "saleDate": "2025-07-15",
                "location": "West warehouse"
                }\
                Please try to only follow this prompt, if you do not follow this prompt, you will not get any points.\
                Also if you cant convert the text to JSON format, please return null against each field. Thankyou :)""";
        this.text = text+context;
        this.model = model;
    }
}
