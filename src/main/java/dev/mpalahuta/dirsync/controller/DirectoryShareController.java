package dev.mpalahuta.dirsync.controller;

import dev.mpalahuta.dirsync.controller.dto.ApiError;
import dev.mpalahuta.dirsync.controller.dto.DirectoryStateResponse;
import dev.mpalahuta.dirsync.service.DirectoryShareService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;

@Controller
public class DirectoryShareController {

    private final DirectoryShareService directoryShareService;

    public DirectoryShareController(DirectoryShareService directoryShareService) {
        this.directoryShareService = directoryShareService;
    }

    @GetMapping("/server")
    public ModelAndView index() {
        final var modelAndView = new ModelAndView("server-index");
        this.directoryShareService.getSharedDir()
                .ifPresent(dir -> modelAndView.addObject("sharedDir", dir));
        return modelAndView;
    }

    private ModelAndView errorView(String errMsg) {
        final var modelAndView = new ModelAndView("error");
        modelAndView.addObject("error", errMsg);
        return modelAndView;
    }

    @PutMapping("/server/directory")
    public ModelAndView startSharing(@RequestParam String directoryPath) {
        try {
            this.directoryShareService.startSharing(directoryPath);
        } catch (IllegalArgumentException e) {
            return errorView(e.getMessage());
        }

        return new ModelAndView("redirect:/server");
    }

    @DeleteMapping("/server/directory")
    public ModelAndView stopSharing() {
        try {
            this.directoryShareService.stopSharing();
        } catch (IllegalArgumentException e) {
            return errorView(e.getMessage());
        }

        return new ModelAndView("redirect:/server");
    }

    @GetMapping("/api/directory")
    @ResponseBody
    public ResponseEntity<?> viewDirState() {
        try {
            final var fileInfoList = this.directoryShareService.viewDirState();
            return ResponseEntity.ok(new DirectoryStateResponse(fileInfoList));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiError(e.getMessage()));
        }
    }

    @GetMapping("/api/directory/{fileName}")
    @ResponseBody
    public ResponseEntity<?> fetchFile(@PathVariable String fileName) {
        try {
            final var stream = this.directoryShareService.fetchFile(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                            .filename(fileName, StandardCharsets.UTF_8).build().toString())
                    .body(new InputStreamResource(stream));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiError(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiError(e.getMessage()));
        }
    }

}
