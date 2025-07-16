package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.tag.TagToAdd;
import com.mecsbalint.backend.controller.dto.tag.TagIdNameDto;
import com.mecsbalint.backend.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagIdNameDto> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Long> addTags(@RequestBody List<TagToAdd> tagsToAdd) {
        return tagService.addTags(tagsToAdd);
    }
}
