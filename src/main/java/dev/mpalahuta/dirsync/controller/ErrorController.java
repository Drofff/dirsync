package dev.mpalahuta.dirsync.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler
    public ModelAndView handleException(Exception e) {
        log.error("caught exception in global handler: ", e);
        final var modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", "Oops. A technical problem occurred, please give us some time to fix it.");
        return modelAndView;
    }

}
