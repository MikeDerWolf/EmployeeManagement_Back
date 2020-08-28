package com.ausy_technologies.demospring.RestErrorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomException extends ResponseStatusException {

    private static boolean debugMode = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().
            stream().anyMatch(s -> s.contains("jdwp"));
    public static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void DisplayException(CustomException customException, Level level){
        if(debugMode) {
            customException.printStackTrace();
        }
        else {
            logger.log(level,customException.getMessage());
        }

    }

    public CustomException(HttpStatus httpStatus){
        super(httpStatus);
    }

    public CustomException(HttpStatus httpStatus, String errorMessage) {
        super(httpStatus, errorMessage);

    }

    public CustomException(HttpStatus httpStatus, String errorMessage, Throwable cause) {
        super(httpStatus,errorMessage, cause);

    }



}
