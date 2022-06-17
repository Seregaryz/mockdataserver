package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;

import java.util.List;

public interface EndpointRepository extends CrudRepository<Endpoint, Long> {

    Endpoint findByAdditionalPathAndUserPath(String additionalPath, String userPath);

    List<Endpoint> findAllByAdditionalPath(String additionalPath);
}
