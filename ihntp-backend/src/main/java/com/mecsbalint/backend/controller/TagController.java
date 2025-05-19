package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.TagIdNameDto;
import com.mecsbalint.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/all")
    public List<TagIdNameDto> getAllTags() {
        return tagService.getAllTags();
    }
}
