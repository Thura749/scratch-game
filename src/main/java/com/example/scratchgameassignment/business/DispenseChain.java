package com.example.scratchgameassignment.business;

import com.example.scratchgameassignment.model.MatchedSymbol;
import com.example.scratchgameassignment.model.ScratchResponse;

import java.util.List;

public interface DispenseChain {
    void setNextChain(DispenseChain chain);
    void dispense(ScratchResponse scratch, List<MatchedSymbol> matchedSymbols);
}
