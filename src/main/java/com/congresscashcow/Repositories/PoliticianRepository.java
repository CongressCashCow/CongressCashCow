package com.congresscashcow.Repositories;




import com.congresscashcow.models.Politician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoliticianRepository extends JpaRepository<Politician, Long> {
    Politician findByName(String name);
}
