package com.example.scratchgameassignment.business.impl;

import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.model.MatchedSymbol;
import com.example.scratchgameassignment.model.ScratchResponse;

import java.util.List;
import java.util.Objects;

public class RewardDispenser implements DispenseChain {
    private DispenseChain chain;
    @Override
    public void setNextChain(DispenseChain chain) {
        this.chain = chain;
    }

    @Override
    public void dispense(ScratchResponse scratch, List<MatchedSymbol> matchedSymbols) {
        if (matchedSymbols.isEmpty()) {
            scratch.setReward(0);
        } else {
            if (Objects.nonNull(this.chain)) {
                chain.dispense(scratch, matchedSymbols);
            }
        }
    }
}
