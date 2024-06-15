package com.example.scratchgameassignment.utils;

import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.business.impl.BonusRewardDispenser;
import com.example.scratchgameassignment.business.impl.RewardDispenser;
import com.example.scratchgameassignment.business.impl.SameAndLinearSymbolsRewardDispenser;
import com.example.scratchgameassignment.config.ConfigurationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class ConfigurationUtils {
    private static ConfigurationProperties PROPERTIES;
    private static DispenseChain DISPENSER;
    private final ObjectMapper objectMapper;

    public void prepare(String configPath) throws IOException {
        PROPERTIES = configurationProperties(configPath);
        DISPENSER = dispenseChain(PROPERTIES);
    }

    public ConfigurationProperties getProperties() {
        return PROPERTIES;
    }

    public DispenseChain getDispenser() {
        return DISPENSER;
    }

    private ConfigurationProperties configurationProperties(String configPath) throws IOException {
        var resource = Files.readString(Path.of(configPath));
        var properties = objectMapper.readValue(resource, ConfigurationProperties.class);
        if (properties.getRows()==0) {
            properties.setRows(3);
        }
        if (properties.getColumns()==0) {
            properties.setColumns(3);
        }
        return properties;
    }

    private DispenseChain dispenseChain(ConfigurationProperties properties) {
        DispenseChain chain1 = new RewardDispenser();
        DispenseChain chain2 = new SameAndLinearSymbolsRewardDispenser(properties);
        DispenseChain chain3 = new BonusRewardDispenser(properties);
        if (properties.getCombinations().keySet().stream().anyMatch(key-> key.contains("times"))) {
            chain1.setNextChain(chain2);
        }
        if (properties.getSymbols().values().stream().anyMatch(symbol-> symbol.getType().equals("bonus"))) {
            chain2.setNextChain(chain3);
        }
        return chain1;
    }
}
