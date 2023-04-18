package com.milkmoney.Repositories;

import com.milkmoney.models.Politician;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoliticianRepository extends JpaRepository<Politician, Long> {
    Politician findByName(String name);
    List<Politician> findByNameContaining(String name);
}
