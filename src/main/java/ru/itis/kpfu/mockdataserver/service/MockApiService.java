package ru.itis.kpfu.mockdataserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;
import ru.itis.kpfu.mockdataserver.repository.ClassModelRepository;
import ru.itis.kpfu.mockdataserver.repository.EndpointRepository;
import ru.itis.kpfu.mockdataserver.repository.InternalFieldRepository;
import ru.itis.kpfu.mockdataserver.repository.PrimitiveFieldRepository;

import java.util.List;

@Service
public class MockApiService {

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private ClassModelRepository classModelRepository;

    @Autowired
    private PrimitiveFieldRepository primitiveFieldRepository;

    @Autowired
    private InternalFieldRepository internalFieldRepository;

    public Endpoint getEndpoint(String additionalPath, String userPath) {
        return endpointRepository.findByAdditionalPathAndUserPath(additionalPath, userPath);
    }

    public List<ClassModel> getClassModels(Long endpointId) {
        return classModelRepository.findAllByEndpointId(endpointId);
    }
}
