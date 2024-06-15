package com.example.scratchgameassignment.business.impl;

import com.example.scratchgameassignment.business.ScratchService;
import com.example.scratchgameassignment.model.ScratchResponse;
import com.example.scratchgameassignment.utils.ConfigurationUtils;
import com.example.scratchgameassignment.utils.ScratchUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScratchServiceImpl implements ScratchService {
    private final ScratchUtils scratchUtils;
    private final ConfigurationUtils configurationUtils;
    private final ObjectMapper objectMapper;

    @Override
    public ScratchResponse execute(Double betAmount) throws JsonProcessingException {
        var matrix = scratchUtils.generateScratch();
        var matchedSymbols = scratchUtils.findMatches(matrix);
        var response = new ScratchResponse();
        response.setBettingAmount(betAmount);
        response.setMatrix(objectMapper.writeValueAsString(matrix));
        try {
            configurationUtils.getDispenser().dispense(response, matchedSymbols);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
        return response;
    }
}
