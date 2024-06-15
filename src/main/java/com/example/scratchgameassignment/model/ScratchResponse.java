package com.example.scratchgameassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class ScratchResponse {
    @JsonIgnore
    private double bettingAmount;
    private String matrix;
    private double reward;
    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> winningCombinations;
    @JsonProperty("applied_bonus_symbol")
    private String appliedBonus;

    @Override
    public String toString() {
        var response = "{ </br> matrix: " + matrix + ", </br> reward: " + reward;
        if (Objects.nonNull(winningCombinations)) {
            response = response.concat(", </br> winningCombinations: " + winningCombinations);
        }
        if (Objects.nonNull(appliedBonus)) {
            response = response.concat(", </br> appliedBonus: " + appliedBonus );
        }
        return response.concat(" }");
    }
}
