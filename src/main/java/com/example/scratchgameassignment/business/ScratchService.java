package com.example.scratchgameassignment.business;

import com.example.scratchgameassignment.model.ScratchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ScratchService {
    ScratchResponse execute(Double betAmount) throws JsonProcessingException;
}
