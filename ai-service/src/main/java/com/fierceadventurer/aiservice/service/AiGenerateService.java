package com.fierceadventurer.aiservice.service;

import com.fierceadventurer.aiservice.dto.GenerateRequestDto;
import com.fierceadventurer.aiservice.dto.GenerateResponseDto;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public interface AiGenerateService {
    GenerateResponseDto generateContent(GenerateRequestDto request);

}
