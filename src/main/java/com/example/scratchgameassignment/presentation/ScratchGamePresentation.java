package com.example.scratchgameassignment.presentation;

import com.example.scratchgameassignment.business.ScratchService;
import com.example.scratchgameassignment.model.ScratchResponse;
import com.example.scratchgameassignment.utils.ConfigurationUtils;
import com.example.scratchgameassignment.utils.ArgumentUtil;
import dnl.utils.text.table.TextTable;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ScratchGamePresentation implements CommandLineRunner {
    private final ConfigurationUtils preparation;
    private final ScratchService service;

    @Override
    public void run(String... args) throws Exception {
        var arguments = Arrays.asList(args);
        if (arguments.size() != 4 || !arguments.contains("--config") || !arguments.contains("--betting-amount")) {
            System.err.println("Required argument format --config (config-file-path) --betting-amount (amount)");
        } else {
            try {
                preparation.prepare(ArgumentUtil.getArgumentValue("--config", args));
                String inputString = ArgumentUtil.getArgumentValue("--betting-amount", args);
                Double betAmount = Double.parseDouble(inputString);
                var response = service.execute(betAmount);
                pintOutput(betAmount, response);
            } catch (NumberFormatException e) {
                System.err.println("Betting Amount Should be Number!");
            } catch (NullPointerException e) {
                System.err.println("betting-amount is mandatory");
            } catch (IOException e) {
                System.err.println("Config File Path Not Found");
            }
        }
    }

    private void pintOutput(Double betAmount, ScratchResponse result) {
        var output = new Object[1][2];
        output[0][0] = betAmount;
        output[0][1] = result;
        TextTable table = new TextTable(new String[]{"Input", "Output"}, output);
        table.printTable();
    }
}
