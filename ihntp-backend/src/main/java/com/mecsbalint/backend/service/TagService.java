package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.PublisherIdNameDto;
import com.mecsbalint.backend.controller.dto.TagIdNameDto;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagIdNameDto> getAllTags() {
        List<Tag> tagEntities = tagRepository.findAll();

        return tagEntities.stream()
                .map(TagIdNameDto::new)
                .sorted(Comparator.comparing(TagIdNameDto::name))
                .toList();
    }
}
