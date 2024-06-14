package com.example.scratchgameassignment.config;

import com.example.scratchgameassignment.model.SymbolDto;
import com.example.scratchgameassignment.model.WinCombinationDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationProperties {
    @JsonProperty("columns")
    private int columns;
    @JsonProperty("rows")
    private int rows;
    @JsonProperty("symbols")
    private Map<String, SymbolDto> symbols;
    @JsonProperty("win_combinations")
    private Map<String, WinCombinationDto> combinations;
}
