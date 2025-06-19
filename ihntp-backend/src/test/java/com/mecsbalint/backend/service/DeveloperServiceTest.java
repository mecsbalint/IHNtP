package com.mecsbalint.backend.service;

import com.mecsbalint.backend.controller.dto.DeveloperIdNameDto;
import com.mecsbalint.backend.controller.dto.DeveloperToAdd;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.repository.DeveloperRepository;
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
class DeveloperServiceTest {

    @Mock
    private DeveloperRepository developerRepositoryMock;

    private DeveloperService developerService;

    @BeforeEach
    public void setup() {
        developerService = new DeveloperService(developerRepositoryMock);
    }

    @Test
    public void getAllTags_threeTags_returnListOfTagIdNameDto() {
        when(developerRepositoryMock.findAll()).thenReturn(getListOfDevelopers());

        List<DeveloperIdNameDto> expectedTags = getListOfDevelopers().stream().map(DeveloperIdNameDto::new).sorted(Comparator.comparing(DeveloperIdNameDto::name)).toList();
        List<DeveloperIdNameDto> actualTags = developerService.getAllDevelopers();

        assertEquals(expectedTags.get(0).name(), actualTags.get(0).name());
        assertEquals(expectedTags.get(1).name(), actualTags.get(1).name());
        assertEquals(expectedTags.get(2).name(), actualTags.get(2).name());
    }

    @Test
    public void getAllTags_noTags_returnEmptyList() {
        when(developerRepositoryMock.findAll()).thenReturn(List.of());

        int expectedListSize = 0;
        int actualListSize = developerService.getAllDevelopers().size();

        assertEquals(expectedListSize, actualListSize);
    }

    @Test
    public void addTags_threeTagDto_saveAllCalledAndIdsReturned() {
        when(developerRepositoryMock.saveAll(any())).thenReturn(getListOfDevelopers());

        List<Long> expectedResult = List.of(1L, 2L, 3L);
        List<Long> actualResult = developerService.addDevelopers(getListOfDeveloperToAdds());

        verify(developerRepositoryMock, times(1)).saveAll(any());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void addTags_atLeastOneElementIsAlreadyExist_throwsElementIsAlreadyInDatabaseException() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Constraint violation", null, "SomeConstraint");
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("", constraintViolationException);
        when(developerRepositoryMock.saveAll(any())).thenThrow(dataIntegrityViolationException);

        assertThrows(ElementIsAlreadyInDatabaseException.class, () -> developerService.addDevelopers(getListOfDeveloperToAdds()));
    }

    @Test
    public void addTags_elementsNotExistButExceptionThrown_throwsException() {
        when(developerRepositoryMock.saveAll(any())).thenThrow(new DataIntegrityViolationException("", null));

        assertThrows(DataIntegrityViolationException.class, () -> developerService.addDevelopers(getListOfDeveloperToAdds()));
    }

    private List<Developer> getListOfDevelopers() {
        Developer developer1 = new Developer();
        developer1.setId(1);
        developer1.setName("developer one");

        Developer developer2 = new Developer();
        developer2.setId(2);
        developer2.setName("tag two");

        Developer developer3 = new Developer();
        developer3.setId(3);
        developer3.setName("tag three");

        return new ArrayList<>(List.of(developer1, developer2, developer3));
    }

    private List<DeveloperToAdd> getListOfDeveloperToAdds() {
        return getListOfDevelopers().stream().map(DeveloperToAdd::new).toList();
    }
}