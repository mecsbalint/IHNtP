package com.mecsbalint.backend.repository;

import com.mecsbalint.backend.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
