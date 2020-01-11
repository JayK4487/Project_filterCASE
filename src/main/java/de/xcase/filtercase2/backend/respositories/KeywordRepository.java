package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findByUserKeyword(String user);
}
