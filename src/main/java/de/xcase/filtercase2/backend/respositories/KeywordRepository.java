package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByUserKeyword(String user);
}
