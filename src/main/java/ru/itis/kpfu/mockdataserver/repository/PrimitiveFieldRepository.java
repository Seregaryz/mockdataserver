package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.PrimitiveField;

public interface PrimitiveFieldRepository extends CrudRepository<PrimitiveField, Long> {
}
