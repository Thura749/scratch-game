package com.example.scratchgameassignment.business.impl;

import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.config.ConfigurationProperties;
import com.example.scratchgameassignment.exception.UnknownCombinationException;
import com.example.scratchgameassignment.model.MatchedSymbol;
import com.example.scratchgameassignment.model.ScratchResponse;
import com.example.scratchgameassignment.model.SymbolDto;
import com.example.scratchgameassignment.model.WinCombinationDto;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SameAndLinearSymbolsRewardDispenser implements DispenseChain {
    private final ConfigurationProperties properties;
    private DispenseChain chain;

    @Override
    public void setNextChain(DispenseChain chain) {
        this.chain = chain;
    }

    @Override
    public void dispense(ScratchResponse scratch, List<MatchedSymbol> matchedSymbols) {
        var originalSymbols = properties.getSymbols();
        Map<String, List<String>> winningCombinations = new HashMap<>();
        List<MatchedSymbol> calculatedList = new ArrayList<>();

        matchedSymbols.forEach(matched -> {
            var symbolProperties = originalSymbols.get(matched.getSymbolName());
            if (isStandardSymbol(symbolProperties)) {
                AtomicReference<Double> winningAmount = new AtomicReference<>(scratch.getBettingAmount());
                updateWinningAmount(winningAmount, symbolProperties.getRewardMultiplier());

                List<String> combinationKeys = calculateCombinationKeys(matched, winningAmount);

                winningCombinations.put(matched.getSymbolName(), combinationKeys);
                scratch.setReward(scratch.getReward() + winningAmount.get());
                calculatedList.add(matched);
            }
        });
        updateScratchResponse(scratch, winningCombinations, matchedSymbols, calculatedList);
    }

    private boolean isStandardSymbol(SymbolDto symbolProperties) {
        return Objects.nonNull(symbolProperties) && "standard".equals(symbolProperties.getType());
    }

    private void updateWinningAmount(AtomicReference<Double> winningAmount, double multiplier) {
        winningAmount.getAndUpdate(amount -> amount * multiplier);
    }

    private List<String> calculateCombinationKeys(MatchedSymbol matched, AtomicReference<Double> winningAmount) {
        List<String> combinationKeys = new ArrayList<>();

        var timesCombination = getMatchedTimesCombination(matched);
        updateWinningAmount(winningAmount, timesCombination.getValue().getRewardMultiplier());
        combinationKeys.add(timesCombination.getKey());

        var linearCombinations = getMatchedLinearCombination(matched);
        linearCombinations.forEach(combination -> {
            updateWinningAmount(winningAmount, combination.getValue().getRewardMultiplier());
            combinationKeys.add(combination.getKey());
        });

        return combinationKeys;
    }

    private void updateScratchResponse(ScratchResponse scratch, Map<String, List<String>> winningCombinations,
                                       List<MatchedSymbol> matchedSymbols, List<MatchedSymbol> calculatedList) {
        scratch.setWinningCombinations(winningCombinations);
        matchedSymbols.removeAll(calculatedList);

        if (!matchedSymbols.isEmpty() && Objects.nonNull(this.chain)) {
            this.chain.dispense(scratch, matchedSymbols);
        } else if (matchedSymbols.isEmpty()) {
            scratch.setAppliedBonus("MISS");
        }
    }

    private Map.Entry<String, WinCombinationDto> getMatchedTimesCombination(MatchedSymbol matched) {
        return properties.getCombinations().entrySet().stream()
                .filter(combination -> combination.getValue().getCombinationCount() == matched.getMatchIndices().size())
                .findFirst()
                .orElseThrow(() -> new UnknownCombinationException("Combination not matched!"));
    }

    private List<Map.Entry<String, WinCombinationDto>> getMatchedLinearCombination(MatchedSymbol matched) {
        return properties.getCombinations().entrySet().stream()
                .filter(combination -> Objects.nonNull(combination.getValue().getCoveredAreas()))
                .filter(combination -> combination.getValue().getCoveredAreas().stream()
                        .anyMatch(areas -> areas.stream()
                                .allMatch(area -> matched.getMatchIndices().stream()
                                        .anyMatch(m -> m.compareIndex(area)))))
                .collect(Collectors.toList());
    }
}
