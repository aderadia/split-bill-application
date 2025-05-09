package com.hackethon.squad2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {
    private List<Map<String, Object>> contents;

    public OpenAiRequest(String prompt) {
        this.contents = List.of(
                Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", prompt))
                )
        );
    }

    public List<Map<String, Object>> getContents() {
        return contents;
    }

    public void setContents(List<Map<String, Object>> contents) {
        this.contents = contents;
    }
}
