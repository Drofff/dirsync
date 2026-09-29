package dev.mpalahuta.dirsync.controller;

import dev.mpalahuta.dirsync.service.DirectoryCopyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DirectoryCopyController {

    private final DirectoryCopyService directoryCopyService;

    public DirectoryCopyController(DirectoryCopyService directoryCopyService) {
        this.directoryCopyService = directoryCopyService;
    }

    @GetMapping("/client")
    public ModelAndView index() {
        final var modelAndView = new ModelAndView("client-index");
        final var dirCopies = this.directoryCopyService.listCopies();
        modelAndView.addObject("dirCopies", dirCopies);
        return modelAndView;
    }

    @PostMapping("/client/copy")
    public ModelAndView addCopy(@RequestParam String serverIp, @RequestParam String localDirPath) {
        try {
            this.directoryCopyService.createCopy(serverIp, localDirPath);
            return new ModelAndView("redirect:/client");
        } catch (IllegalArgumentException e) {
            final var modelAndView = new ModelAndView("error");
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
    }

    @DeleteMapping("/client/copy/{id}")
    public ModelAndView deleteCopy(@PathVariable Long id) {
        try {
            this.directoryCopyService.deleteCopy(id);
            return new ModelAndView("redirect:/client");
        } catch (IllegalArgumentException e) {
            final var modelAndView = new ModelAndView("error");
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
    }

    @PutMapping("/client/copy/{id}/refresh")
    public ModelAndView refreshCopy(@PathVariable Long id) {
        try {
            this.directoryCopyService.refreshCopy(id);
            return new ModelAndView("redirect:/client");
        } catch (IllegalArgumentException e) {
            final var modelAndView = new ModelAndView("error");
            modelAndView.addObject("error", e.getMessage());
            return modelAndView;
        }
    }

}
