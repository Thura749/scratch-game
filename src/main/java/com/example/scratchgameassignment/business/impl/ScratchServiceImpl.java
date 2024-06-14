package com.example.scratchgameassignment.business.impl;

import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.business.ScratchService;
import com.example.scratchgameassignment.model.ScratchResponse;
import com.example.scratchgameassignment.utils.ScratchUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScratchServiceImpl implements ScratchService {
    private final ScratchUtils scratchUtils;
    private final DispenseChain chain;

    @Override
    public ScratchResponse execute(Double betAmount) {
        var matrix = scratchUtils.generateScratch();
        var matchedSymbols = scratchUtils.findMatches(matrix);
        var response = new ScratchResponse();
        response.setBettingAmount(betAmount);
        response.setMatrix(matrix);
        try {
            chain.dispense(response, matchedSymbols);
        } catch (Throwable e) {
            System.err.println(e.getMessage());
        }
        return response;
    }
}
