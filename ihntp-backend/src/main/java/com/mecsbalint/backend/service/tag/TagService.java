package com.mecsbalint.backend.service.tag;

import com.mecsbalint.backend.controller.dto.tag.TagToAdd;
import com.mecsbalint.backend.controller.dto.tag.TagIdNameDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.TagRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    public List<Long> addTags(List<TagToAdd> tagForAdds) {
        List<Tag> tagsToAdd = tagForAdds.stream()
                .map(tagForAdd -> {
                    Tag tag = new Tag();
                    tag.setName(tagForAdd.name());
                    return tag;
                })
                .toList();

        try {
            return tagRepository.saveAll(tagsToAdd).stream().map(Tag::getId).toList();
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                String elementsStr = tagsToAdd.stream().map(Tag::getName).collect(Collectors.joining(", "));
                throw new ElementIsAlreadyInDatabaseException(elementsStr, "Tag");
            } else {
                throw exception;
            }
        }
    }
}
