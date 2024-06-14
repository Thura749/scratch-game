package com.example.scratchgameassignment.business;

import com.example.scratchgameassignment.model.ScratchResponse;

public interface ScratchService {
    ScratchResponse execute(Double betAmount);
}
