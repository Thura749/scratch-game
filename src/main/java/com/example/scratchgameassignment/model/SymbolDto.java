package com.example.scratchgameassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolDto {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    private int extra;
    private String type;
    private String impact;
}
