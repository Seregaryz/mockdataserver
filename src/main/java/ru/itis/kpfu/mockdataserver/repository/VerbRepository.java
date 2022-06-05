package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.representative.Verb;

public interface VerbRepository extends CrudRepository<Verb, Long> {


}
