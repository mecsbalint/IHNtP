package com.mecsbalint.backend.service.developer;

import com.mecsbalint.backend.controller.dto.DeveloperToAdd;
import com.mecsbalint.backend.controller.dto.DeveloperIdNameDto;
import com.mecsbalint.backend.exception.ElementIsAlreadyInDatabaseException;
import com.mecsbalint.backend.model.Developer;
import com.mecsbalint.backend.repository.DeveloperRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeveloperService {
    private final DeveloperRepository developerRepository;

    @Autowired
    public DeveloperService(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    public List<DeveloperIdNameDto> getAllDevelopers() {
        List<Developer> developerEntities = developerRepository.findAll();

        return developerEntities.stream()
                .map(DeveloperIdNameDto::new)
                .sorted(Comparator.comparing(DeveloperIdNameDto::name))
                .toList();
    }

    public List<Long> addDevelopers(List<DeveloperToAdd> developerForAdds) {
        List<Developer> developersToAdd = developerForAdds.stream()
                .map(developerForAdd -> {
                    Developer developer = new Developer();
                    developer.setName(developerForAdd.name());
                    return developer;
                })
                .toList();

        try {
            return developerRepository.saveAll(developersToAdd).stream().map(Developer::getId).toList();
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                String elementsStr = developersToAdd.stream().map(Developer::getName).collect(Collectors.joining(", "));
                throw new ElementIsAlreadyInDatabaseException(elementsStr, "Developer");
            } else {
                throw exception;
            }
        }
    }
}
