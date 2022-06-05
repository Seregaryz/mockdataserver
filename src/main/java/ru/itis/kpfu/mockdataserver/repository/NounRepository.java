package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.representative.Noun;

public interface NounRepository extends CrudRepository<Noun, Long> {


}
