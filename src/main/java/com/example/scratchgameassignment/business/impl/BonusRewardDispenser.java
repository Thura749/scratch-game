package com.example.scratchgameassignment.business.impl;

import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.config.ConfigurationProperties;
import com.example.scratchgameassignment.model.MatchedSymbol;
import com.example.scratchgameassignment.model.ScratchResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class BonusRewardDispenser implements DispenseChain {
    private final ConfigurationProperties properties;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain chain) {
        this.chain = chain;
    }

    @Override
    public void dispense(ScratchResponse scratch, List<MatchedSymbol> matchedSymbols) {
        AtomicReference<Double> rewardAmount = new AtomicReference<>(scratch.getReward());
        var matchedKey = matchedSymbols.stream().map(MatchedSymbol::getSymbolName).toList();
        var originalBonus = properties.getSymbols().entrySet().stream()
                .filter(symbol-> symbol.getValue().getType().equals("bonus") && matchedKey.contains(symbol.getKey())).toList();
        originalBonus.forEach(bonus-> {
            if (bonus.getKey().startsWith("+")) {
                rewardAmount.getAndUpdate(i-> i + bonus.getValue().getExtra());
            } else if (bonus.getKey().endsWith("x")) {
                rewardAmount.getAndUpdate(i-> i * bonus.getValue().getRewardMultiplier());
            }
            scratch.setAppliedBonus(bonus.getKey());
        });
        scratch.setReward(rewardAmount.get());
    }
}
