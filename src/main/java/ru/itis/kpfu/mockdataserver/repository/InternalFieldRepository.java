package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.InternalField;

public interface InternalFieldRepository extends CrudRepository<InternalField, Long> {

}
