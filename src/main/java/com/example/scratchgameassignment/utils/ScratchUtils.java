package com.example.scratchgameassignment.utils;

import com.example.scratchgameassignment.model.MatchIndex;
import com.example.scratchgameassignment.model.MatchedSymbol;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
@RequiredArgsConstructor
public class ScratchUtils {
    private static final Random RANDOM = new Random();
    private static boolean isBonusPicked = false;
    private final ConfigurationUtils configurationUtils;

    public String[][] generateScratch() {
        var properties = configurationUtils.getProperties();
        int rows = properties.getRows();
        int columns = properties.getColumns();
        var symbolList = new ArrayList<>(properties.getSymbols().keySet()
                .stream().filter(key-> !key.equals("MISS")).toList());
        String[][] scratch = new String[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int index = getRandomNumber(symbolList);
                scratch[i][j] = symbolList.get(index);
            }
        }
        isBonusPicked = false;
        return scratch;
    }

    private int getRandomNumber(List<String> symbols) {
        var properties = configurationUtils.getProperties();
        var symbolDetails = properties.getSymbols();
        int number = RANDOM.nextInt(symbols.size());
        var details = symbolDetails.get(symbols.get(number));
        if (details.getType().equals("bonus")) {
            if (isBonusPicked) {
                return getRandomNumber(symbols);
            }
            isBonusPicked = true;
        }
        return number;
    }

    public List<MatchedSymbol> findMatches(String[][] matrix) {
        var properties = configurationUtils.getProperties();
        var symbols = properties.getSymbols();
        List<MatchedSymbol> matchers = new ArrayList<>();
        AtomicBoolean standardMatched = new AtomicBoolean(false);
        symbols.keySet().forEach(symbol-> {
            var symbolDto = symbols.get(symbol);
            var indexes = findMatrixWithCharacter(matrix, symbol);
            if (symbolDto.getType().equals("standard") && indexes.size() > 2) {
                standardMatched.set(true);
                matchers.add(new MatchedSymbol(symbol, indexes));
            } else if (symbolDto.getType().equals("bonus") && indexes.size() > 0) {
                matchers.add(new MatchedSymbol(symbol, indexes));
            }
        });
        if (standardMatched.get()) {
            return matchers;
        } else {
            return List.of();
        }
    }

    private List<MatchIndex> findMatrixWithCharacter(String[][] matrix, String symbol) {
        List<MatchIndex> result = new ArrayList<>();
        int rows = matrix[0].length;
        int cols = matrix.length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (Objects.equals(matrix[i][j], symbol)) {
                    result.add(new MatchIndex(i, j));
                }
            }
        }
        return result;
    }

}
