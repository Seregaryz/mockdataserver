package ru.itis.kpfu.mockdataserver.repository;

import org.springframework.data.repository.CrudRepository;
import ru.itis.kpfu.mockdataserver.entity.dao.HistoryEndpoint;

import java.util.List;

public interface HistoryEndpointRepository extends CrudRepository<HistoryEndpoint, Long> {

    List<HistoryEndpoint> findAllByUserId(String userId);
}
