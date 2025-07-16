package com.mecsbalint.backend.service.publisher;

import com.mecsbalint.backend.controller.dto.publisher.PublisherToAdd;
import com.mecsbalint.backend.controller.dto.publisher.PublisherIdNameDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Publisher;
import com.mecsbalint.backend.repository.PublisherRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public List<PublisherIdNameDto> getAllPublishers() {
        List<Publisher> publisherEntities = publisherRepository.findAll();

        return publisherEntities.stream()
                .map(PublisherIdNameDto::new)
                .sorted(Comparator.comparing(PublisherIdNameDto::name))
                .toList();
    }

    public List<Long> addPublishers(List<PublisherToAdd> publisherForAdds) {
        List<Publisher> publishersToAdd = publisherForAdds.stream()
                .map(publisherForAdd -> {
                    Publisher publisher = new Publisher();
                    publisher.setName(publisherForAdd.name());
                    return publisher;
                })
                .toList();

        try {
            return publisherRepository.saveAll(publishersToAdd).stream().map(Publisher::getId).toList();
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                String elementsStr = publishersToAdd.stream().map(Publisher::getName).collect(Collectors.joining(", "));
                throw new ElementIsAlreadyInDatabaseException(elementsStr, "Publisher");
            } else {
                throw exception;
            }
        }
    }
}
