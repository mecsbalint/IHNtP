package com.mecsbalint.backend.service.publisher;

import com.mecsbalint.backend.controller.dto.PublisherIdNameDto;
import com.mecsbalint.backend.controller.dto.PublisherToAdd;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.repository.PublisherRepository;
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
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepositoryMock;

    private PublisherService publisherService;

    @BeforeEach
    public void setup() {
        publisherService = new PublisherService(publisherRepositoryMock);
    }

    @Test
    public void getAllTags_threeTags_returnListOfTagIdNameDto() {
        when(publisherRepositoryMock.findAll()).thenReturn(getListOfPublishers());

        List<PublisherIdNameDto> expectedTags = getListOfPublishers().stream().map(PublisherIdNameDto::new).sorted(Comparator.comparing(PublisherIdNameDto::name)).toList();
        List<PublisherIdNameDto> actualTags = publisherService.getAllPublishers();

        assertEquals(expectedTags.get(0).name(), actualTags.get(0).name());
        assertEquals(expectedTags.get(1).name(), actualTags.get(1).name());
        assertEquals(expectedTags.get(2).name(), actualTags.get(2).name());
    }

    @Test
    public void getAllTags_noTags_returnEmptyList() {
        when(publisherRepositoryMock.findAll()).thenReturn(List.of());

        int expectedListSize = 0;
        int actualListSize = publisherService.getAllPublishers().size();

        assertEquals(expectedListSize, actualListSize);
    }

    @Test
    public void addTags_threeTagDto_saveAllCalledAndIdsReturned() {
        when(publisherRepositoryMock.saveAll(any())).thenReturn(getListOfPublishers());

        List<Long> expectedResult = List.of(1L, 2L, 3L);
        List<Long> actualResult = publisherService.addPublishers(getListOfPublisherToAdds());

        verify(publisherRepositoryMock, times(1)).saveAll(any());
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void addTags_atLeastOneElementIsAlreadyExist_throwsElementIsAlreadyInDatabaseException() {
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Constraint violation", null, "SomeConstraint");
        DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("", constraintViolationException);
        when(publisherRepositoryMock.saveAll(any())).thenThrow(dataIntegrityViolationException);

        assertThrows(ElementIsAlreadyInDatabaseException.class, () -> publisherService.addPublishers(getListOfPublisherToAdds()));
    }

    @Test
    public void addTags_elementsNotExistButExceptionThrown_throwsException() {
        when(publisherRepositoryMock.saveAll(any())).thenThrow(new DataIntegrityViolationException("", null));

        assertThrows(DataIntegrityViolationException.class, () -> publisherService.addPublishers(getListOfPublisherToAdds()));
    }

    private List<Publisher> getListOfPublishers() {
        Publisher publisher1 = new Publisher();
        publisher1.setId(1);
        publisher1.setName("publisher one");

        Publisher publisher2 = new Publisher();
        publisher2.setId(2);
        publisher2.setName("tag two");

        Publisher publisher3 = new Publisher();
        publisher3.setId(3);
        publisher3.setName("tag three");

        return new ArrayList<>(List.of(publisher1, publisher2, publisher3));
    }

    private List<PublisherToAdd> getListOfPublisherToAdds() {
        return getListOfPublishers().stream().map(PublisherToAdd::new).toList();
    }
}