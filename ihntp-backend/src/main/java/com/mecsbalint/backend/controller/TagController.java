package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.controller.dto.TagForAdd;
import com.mecsbalint.backend.controller.dto.TagIdNameDto;
import com.mecsbalint.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/all")
    public List<TagIdNameDto> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping("/add")
    public List<Long> addTags(@RequestBody List<TagForAdd> tagsToAdd) {
        return tagService.addTags(tagsToAdd);
    }
}
