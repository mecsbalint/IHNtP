package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.TagForAdd;
import com.mecsbalint.backend.controller.dto.TagIdNameDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Long> addTags(List<TagForAdd> tagForAdds) {
        List<Tag> tagsToAdd = tagForAdds.stream()
                .map(tagForAdd -> {
                    Tag tag = new Tag();
                    tag.setName(tagForAdd.name());
                    return tag;
                })
                .toList();

        try {
            return tagRepository.saveAll(tagsToAdd).stream().map(Tag::getId).toList();
        } catch (RuntimeException ignored) {
            String elementsStr = tagsToAdd.stream().map(Tag::getName).collect(Collectors.joining(", "));
            throw new ElementIsAlreadyInDatabaseException(elementsStr, "Tag");
        }
    }
}
