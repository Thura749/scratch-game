package com.example.scratchgameassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ScratchResponse {
    @JsonIgnore
    private double bettingAmount;
    private String[][] matrix;
    private double reward;
    @JsonProperty("applied_winning_combinations")
    private Map<String, List<String>> winningCombinations;
    @JsonProperty("applied_bonus_symbol")
    private String appliedBonus;
}
