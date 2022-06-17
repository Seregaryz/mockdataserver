package ru.itis.kpfu.mockdataserver.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;
import ru.itis.kpfu.mockdataserver.entity.dao.InternalField;
import ru.itis.kpfu.mockdataserver.entity.dao.PrimitiveField;
import ru.itis.kpfu.mockdataserver.entity.server.FieldType;
import ru.itis.kpfu.mockdataserver.entity.server.PluginResponse;
import ru.itis.kpfu.mockdataserver.repository.ClassModelRepository;
import ru.itis.kpfu.mockdataserver.repository.EndpointRepository;
import ru.itis.kpfu.mockdataserver.repository.InternalFieldRepository;
import ru.itis.kpfu.mockdataserver.repository.PrimitiveFieldRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PluginDataService {

    public final String STATUS_SUCCESS = "success";

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private ClassModelRepository classModelRepository;

    @Autowired
    private PrimitiveFieldRepository primitiveFieldRepository;

    @Autowired
    private InternalFieldRepository internalFieldRepository;

    @Autowired
    private DataGenerationService dataGenerationService;

    public String savePluginData(PluginResponse pluginResponse) {
        HashMap<String, LinkedTreeMap<String, Object>> jsonModelMap;
        HashMap<String, FieldType> modelMap;
        try {
            jsonModelMap = (HashMap) new Gson().fromJson(pluginResponse.getRootModel(), HashMap.class);
        } catch (Exception e) {
            return "Некорректные данные модели";
        }
        modelMap = parseJsonModelMap(jsonModelMap);

        List<LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String, Object>>>> jsonAdditionalModels;
        List<HashMap<String, HashMap<String, FieldType>>> additionalModelList;
        try {
            jsonAdditionalModels = (List) new Gson().fromJson(pluginResponse.getAdditionalModels(), List.class);
        } catch (Exception e) {
            return "Некорректные данные вложенных моделей";
        }
        additionalModelList = parseJsonAdditionalModelMap(jsonAdditionalModels);

        // save endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setUserPath(pluginResponse.getEndpoint());
        endpoint.setAdditionalPath(pluginResponse.getUserId());
        endpoint.setLocale(pluginResponse.getLocale());
        endpoint.setList(pluginResponse.getIsList());
        endpoint.setElementsCount(pluginResponse.getElementsCount());
        endpoint.setIsRepresentative(pluginResponse.getIsRepresentative());
        Endpoint savedEndpointValue = endpointRepository.save(endpoint);

        // save class models
        ClassModel rootClassModel = new ClassModel();
        rootClassModel.setRoot(true);
        rootClassModel.setEndpoint(savedEndpointValue);
        rootClassModel.setName(pluginResponse.getNameOfRootModel());
        rootClassModel.setHasInternal(!additionalModelList.isEmpty());
        rootClassModel.setHasPrimitive(hasPrimitives(modelMap));
        ClassModel savedRootClassModel = classModelRepository.save(rootClassModel);

        // save root primitive fields
        modelMap.keySet().forEach(fieldKey -> {
            if (modelMap.get(fieldKey).getPrimitive()) {
                GeneratedItem generatedItem = dataGenerationService.generateValue(
                        modelMap.get(fieldKey).getType(),
                        fieldKey,
                        pluginResponse.getLocale(),
                        pluginResponse.getIsRepresentative()
                );
                PrimitiveField primitiveField = new PrimitiveField();
                primitiveField.setName(fieldKey);
                primitiveField.setIsList(generatedItem.isList());
                primitiveField.setClassModel(savedRootClassModel);
                primitiveField.setTypeId(generatedItem.getTypeId());
                primitiveField.setIsStatic(pluginResponse.getIsStatic());
                primitiveField.setStaticValue(generatedItem.getGeneratedValue());
                primitiveField.setTypeName(modelMap.get(fieldKey).getType());
                primitiveFieldRepository.save(primitiveField);
            } else {
                InternalField internalField = new InternalField();
                internalField.setName(fieldKey);
                internalField.setClassModel(savedRootClassModel);
                internalField.setTypeName(modelMap.get(fieldKey).getType());
                internalFieldRepository.save(internalField);
            }
        });

        // save additional models and root internal fields
        additionalModelList.forEach(map -> map.keySet().forEach(modelKey -> {

            //saving additional model
            ClassModel additionalClassModel = new ClassModel();
            additionalClassModel.setRoot(false);
            additionalClassModel.setEndpoint(savedEndpointValue);
            additionalClassModel.setName(modelKey);
            additionalClassModel.setHasPrimitive(hasPrimitives(map.get(modelKey)));
            int primitivesCount = primitivesCount(map.get(modelKey));
            boolean hasInternal = map.get(modelKey).size() > primitivesCount;
            additionalClassModel.setHasInternal(hasInternal);
            ClassModel savedAdditionalModel = classModelRepository.save(additionalClassModel);

            HashMap<String, FieldType> finalAdditionalModelMap = map.get(modelKey);
            map.get(modelKey).keySet().forEach(fieldKey -> {
                if (finalAdditionalModelMap.get(fieldKey).getPrimitive()) {
                    GeneratedItem generatedItem = dataGenerationService.generateValue(
                            finalAdditionalModelMap.get(fieldKey).getType(),
                            fieldKey,
                            pluginResponse.getLocale(),
                            pluginResponse.getIsRepresentative()
                    );
                    PrimitiveField primitiveField = new PrimitiveField();
                    primitiveField.setName(fieldKey);
                    primitiveField.setIsList(generatedItem.isList());
                    primitiveField.setClassModel(savedAdditionalModel);
                    primitiveField.setTypeId(generatedItem.getTypeId());
                    primitiveField.setIsStatic(pluginResponse.getIsStatic());
                    primitiveField.setStaticValue(generatedItem.getGeneratedValue());
                    primitiveField.setTypeName(finalAdditionalModelMap.get(fieldKey).getType());
                    primitiveFieldRepository.save(primitiveField);
                } else {
                    InternalField internalField = new InternalField();
                    internalField.setName(fieldKey);
                    internalField.setClassModel(savedAdditionalModel);
                    internalField.setTypeName(finalAdditionalModelMap.get(fieldKey).getType());
                    internalFieldRepository.save(internalField);
                }
            });
        }));
        return STATUS_SUCCESS;
    }

    public HashMap<String, FieldType> parseJsonModelMap(HashMap<String, LinkedTreeMap<String, Object>> jsonModelMap) {
        HashMap<String, FieldType> modelMap = new HashMap<>();
        jsonModelMap.keySet().forEach(fieldKey -> {
            modelMap.put(fieldKey, new FieldType(
                (String) jsonModelMap.get(fieldKey).get("type"),
                (Boolean) jsonModelMap.get(fieldKey).get("isPrimitive")
            ));
        });
        return modelMap;
    }

    public List<HashMap<String, HashMap<String, FieldType>>> parseJsonAdditionalModelMap(
        List<LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String, Object>>>> jsonAdditionalModels
    ) {
        List<HashMap<String, HashMap<String, FieldType>>> additionalModelList = new ArrayList<>();
        jsonAdditionalModels.forEach(classMap -> {
            HashMap<String, HashMap<String, FieldType>> resultClassMap = new HashMap<>();
            classMap.keySet().forEach(classKey -> {
                HashMap<String, FieldType> resultMap = new HashMap<>();
                classMap.get(classKey).keySet().forEach(fieldKey -> {
                    resultMap.put(fieldKey, new FieldType(
                        (String) classMap.get(classKey).get(fieldKey).get("type"),
                        (Boolean) classMap.get(classKey).get(fieldKey).get("isPrimitive")
                    ));
                });
                resultClassMap.put(classKey, resultMap);
            });
            additionalModelList.add(resultClassMap);
        });
        return additionalModelList;
    }

    public boolean hasPrimitives(HashMap<String, FieldType> fieldsMap) {
        return fieldsMap.values().stream().anyMatch(FieldType::getPrimitive);
    }

    public int primitivesCount(HashMap<String, FieldType> fieldsMap) {
        return (int) fieldsMap.values().stream().filter(FieldType::getPrimitive).count();
    }
}
