package com.example.scratchgameassignment.config;

import com.example.scratchgameassignment.business.impl.BonusRewardDispenser;
import com.example.scratchgameassignment.business.DispenseChain;
import com.example.scratchgameassignment.business.impl.RewardDispenser;
import com.example.scratchgameassignment.business.impl.SameAndLinearSymbolsRewardDispenser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
public class ScratchConfiguration {
    @Value("${json.config-path}")
    private String configPath;

    @Bean
    public LineReader lineReader() {
        return LineReaderBuilder.builder().build();
    }

    @Bean
    public ConfigurationProperties configurationProperties(ObjectMapper objectMapper, ResourceLoader resourceLoader) throws IOException {
        var resource = resourceLoader.getResource(configPath);
        var properties = objectMapper.readValue(resource.getInputStream(), ConfigurationProperties.class);
        if (properties.getRows()==0) {
            properties.setRows(3);
        }
        if (properties.getColumns()==0) {
            properties.setColumns(3);
        }
        return properties;
    }

    @Bean
    public DispenseChain dispenseChain(ConfigurationProperties properties) {
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

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
