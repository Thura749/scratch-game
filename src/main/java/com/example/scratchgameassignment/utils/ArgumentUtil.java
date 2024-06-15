package com.example.scratchgameassignment.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArgumentUtil {
    
    public String getArgumentValue(String argName, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argName) && (i + 1) < args.length) {
                return args[i + 1];
            }
        }
        return null;
    }
}
