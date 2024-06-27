package com.fz.filmFinder.filmFinder.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    @GetMapping("/error")
    public String weHaveError() {
        return "error";
    }
}
