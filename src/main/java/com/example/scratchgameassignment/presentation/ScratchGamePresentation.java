package com.example.scratchgameassignment.presentation;

import com.example.scratchgameassignment.business.ScratchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScratchGamePresentation implements CommandLineRunner {
    private final LineReader lineReader;
    private final ScratchService service;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        Double betAmount = getInput();
        var response = service.execute(betAmount);
        System.out.println("\nOutput : \n" + objectMapper.writeValueAsString(response));
    }

    private Double getInput() {
        double input;
        try {
            String inputString = lineReader.readLine("\n\"bet_amount\": ");
            input = Double.parseDouble(inputString);
        } catch (NumberFormatException | UserInterruptException e) {
            System.err.println("Betting Amount Should be Number! Please try again...");
            input = getInput();
        }
        return input;
    }
}
