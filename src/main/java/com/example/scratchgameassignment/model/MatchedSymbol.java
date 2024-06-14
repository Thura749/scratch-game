package com.example.scratchgameassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MatchedSymbol {
    private String symbolName;
    private List<MatchIndex> matchIndices;

    @Override
    public String toString() {
        return "MatchedSymbol{" +
                "symbolName='" + symbolName + '\'' +
                ", matchIndices=" + matchIndices +
                '}';
    }
}
