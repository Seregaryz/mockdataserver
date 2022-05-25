package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;

import java.util.List;

public interface ClassModelRepository extends CrudRepository<ClassModel, Long> {

    List<ClassModel> findAllByEndpointId(Long endpointId);
}
