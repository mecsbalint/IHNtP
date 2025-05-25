package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.PublisherForAdd;
import com.mecsbalint.backend.controller.dto.PublisherIdNameDto;
import com.mecsbalint.backend.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/all")
    public List<PublisherIdNameDto> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    @PostMapping("/add")
    public List<Long> addPublishers(@RequestBody List<PublisherForAdd> publishersToAdd) {
        return publisherService.addPublishers(publishersToAdd);
    }
}
