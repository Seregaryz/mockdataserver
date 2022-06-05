package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.representative.Adjective;

public interface AdjectiveRepository extends CrudRepository<Adjective, Long> {


}
