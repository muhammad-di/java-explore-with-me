package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class PublicController {

    @GetMapping("/compilations")
    public ResponseEntity<?> getAllCompilations(
            @RequestParam(name = "pinned", required = true) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "0") Integer size) {
        log.info("Get compilations from {} size end {} with pinned = {}", from, size, pinned);
        return null;
    }
}