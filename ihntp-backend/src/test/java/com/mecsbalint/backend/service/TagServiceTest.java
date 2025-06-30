package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.TagIdNameDto;
import com.mecsbalint.backend.controller.dto.TagToAdd;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Tag;
import com.mecsbalint.backend.repository.TagRepository;
import com.mecsbalint.backend.service.tag.TagService;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepositoryMock;

    private TagService tagService;

    @BeforeEach
    public void setup() {
        tagService = new TagService(tagRepositoryMock);
    }

    @Test
    public void getAllTags_threeTags_returnListOfTagIdNameDto() {
        when(tagRepositoryMock.findAll()).thenReturn(getListOfTags());

        List<TagIdNameDto> expectedTags = getListOfTags().stream().map(TagIdNameDto::new).sorted(Comparator.comparing(TagIdNameDto::name)).toList();
        List<TagIdNameDto> actualTags = tagService.getAllTags();

        assertEquals(expectedTags.get(0).name(), actualTags.get(0).name());
        assertEquals(expectedTags.get(1).name(), actualTags.get(1).name());
        assertEquals(expectedTags.get(2).name(), actualTags.get(2).name());
    }

    @Test
    public void getAllTags_noTags_returnEmptyList() {
        when(tagRepositoryMock.findAll()).thenReturn(List.of());

        int expectedListSize = 0;
        int actualListSize = tagService.getAllTags().size();

        assertEquals(expectedListSize, actualListSize);
    }

    @Test
    public void addTags_threeTagDto_saveAllCalledAndIdsReturned() {
        when(tagRepositoryMock.saveAll(any())).thenReturn(getListOfTags());

        List<Long> expectedResult = List.of(1L, 2L, 3L);
        List<Long> actualResult = tagService.addTags(getListOfTagToAdds());

        verify(tagRepositoryMock, times(1)).saveAll(any());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void addTags_atLeastOneElementIsAlreadyExist_throwsElementIsAlreadyInDatabaseException() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Constraint violation", null, "SomeConstraint");
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("", constraintViolationException);
        when(tagRepositoryMock.saveAll(any())).thenThrow(dataIntegrityViolationException);

        assertThrows(ElementIsAlreadyInDatabaseException.class, () -> tagService.addTags(getListOfTagToAdds()));
    }

    @Test
    public void addTags_elementsNotExistButExceptionThrown_throwsException() {
        when(tagRepositoryMock.saveAll(any())).thenThrow(new DataIntegrityViolationException("", null));

        assertThrows(DataIntegrityViolationException.class, () -> tagService.addTags(getListOfTagToAdds()));
    }

    private List<Tag> getListOfTags() {
        Tag tag1 = new Tag();
        tag1.setId(1);
        tag1.setName("tag one");

        Tag tag2 = new Tag();
        tag2.setId(2);
        tag2.setName("tag two");

        Tag tag3 = new Tag();
        tag3.setId(3);
        tag3.setName("tag three");

        return new ArrayList<>(List.of(tag1, tag2, tag3));
    }

    private List<TagToAdd> getListOfTagToAdds() {
        return getListOfTags().stream().map(TagToAdd::new).toList();
    }
}