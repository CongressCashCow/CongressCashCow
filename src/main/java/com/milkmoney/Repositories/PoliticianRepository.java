package com.milkmoney.Repositories;

import com.milkmoney.models.Politician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticianRepository extends JpaRepository<Politician, Long> {
    Politician findByName(String name);
}
