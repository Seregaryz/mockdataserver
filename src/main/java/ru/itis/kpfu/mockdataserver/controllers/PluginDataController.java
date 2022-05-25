package ru.itis.kpfu.mockdataserver.controllers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;
import ru.itis.kpfu.mockdataserver.entity.dao.InternalField;
import ru.itis.kpfu.mockdataserver.entity.dao.PrimitiveField;
import ru.itis.kpfu.mockdataserver.entity.server.FieldType;
import ru.itis.kpfu.mockdataserver.entity.server.PluginResponse;
import ru.itis.kpfu.mockdataserver.repository.EndpointRepository;
import ru.itis.kpfu.mockdataserver.repository.ClassModelRepository;
import ru.itis.kpfu.mockdataserver.repository.InternalFieldRepository;
import ru.itis.kpfu.mockdataserver.repository.PrimitiveFieldRepository;
import ru.itis.kpfu.mockdataserver.service.DataGenerationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("")
public class PluginDataController {

    @Autowired
    private EndpointRepository endpointRepository;

    @Autowired
    private ClassModelRepository classModelRepository;

    @Autowired
    private PrimitiveFieldRepository primitiveFieldRepository;

    @Autowired
    private InternalFieldRepository internalFieldRepository;

    @PostMapping("/add_new_endpoint")
    public ResponseEntity<String> sendPluginData(@RequestBody PluginResponse pluginResponse) {
        try {
            DataGenerationService dataGenerationService = new DataGenerationService();

            HashMap<String, LinkedTreeMap<String, Object>> rawModelMap;
            HashMap<String, FieldType> modelMap = new HashMap<>();
            try {
                rawModelMap = (HashMap) new Gson().fromJson(pluginResponse.getRootModel(), HashMap.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Некорректные данные модели");
            }
            HashMap<String, LinkedTreeMap<String, Object>> finalRawModelMap = rawModelMap;
            rawModelMap.keySet().forEach(fieldKey -> {
                modelMap.put(fieldKey, new FieldType(
                        (String) finalRawModelMap.get(fieldKey).get("type"),
                        (Boolean) finalRawModelMap.get(fieldKey).get("isPrimitive")
                ));
            });

            List<LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String, Object>>>> rawAdditionalModels;
            List<HashMap<String, HashMap<String, FieldType>>> additionalModels = new ArrayList<>();
            try {
                rawAdditionalModels = (List) new Gson().fromJson(pluginResponse.getAdditionalModels(), List.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Некорректные данные вложенных моделей");
            }

            rawAdditionalModels.forEach(classMap -> {
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
                additionalModels.add(resultClassMap);
            });

            // save endpoint
            Endpoint endpoint = new Endpoint();
            endpoint.setUserPath(pluginResponse.getEndpoint());
            endpoint.setAdditionalPath(pluginResponse.getUserId());
            Endpoint savedEndpointValue = endpointRepository.save(endpoint);

            // save class models
            ClassModel rootClassModel = new ClassModel();
            rootClassModel.setRoot(true);
            rootClassModel.setEndpoint(savedEndpointValue);
            rootClassModel.setName(pluginResponse.getNameOfRootModel());
            rootClassModel.setHasInternal(!additionalModels.isEmpty());
            rootClassModel.setHasPrimitive(hasPrimitives(modelMap));
            ClassModel savedRootClassModel = classModelRepository.save(rootClassModel);

            // save root primitive fields
            modelMap.keySet().forEach(fieldKey -> {
                if (modelMap.get(fieldKey).getPrimitive()) {
                    GeneratedItem generatedItem = dataGenerationService.generateValue(
                            modelMap.get(fieldKey).getType(),
                            fieldKey
                    );
                    PrimitiveField primitiveField = new PrimitiveField();
                    primitiveField.setName(fieldKey);
                    primitiveField.setClassModel(savedRootClassModel);
                    primitiveField.setTypeId(dataGenerationService.getTypeId());
                    primitiveField.setIsStatic(pluginResponse.getIsStatic());
                    primitiveField.setStaticValue(generatedItem.getGeneratedValue());
                    primitiveFieldRepository.save(primitiveField);
                } else {
                    InternalField internalField = new InternalField();
                    internalField.setName(fieldKey);
                    internalField.setClassModel(savedRootClassModel);
                    internalField.setType_name(modelMap.get(fieldKey).getType());
                    internalFieldRepository.save(internalField);
                }
            });

            // save additional models and root internal fields
            additionalModels.forEach(map -> map.keySet().forEach(modelKey -> {

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
                                fieldKey
                        );
                        PrimitiveField primitiveField = new PrimitiveField();
                        primitiveField.setName(fieldKey);
                        primitiveField.setClassModel(savedAdditionalModel);
                        primitiveField.setTypeId(dataGenerationService.getTypeId());
                        primitiveField.setIsStatic(pluginResponse.getIsStatic());
                        primitiveField.setStaticValue(generatedItem.getGeneratedValue());
                        primitiveFieldRepository.save(primitiveField);
                    } else {
                        InternalField internalField = new InternalField();
                        internalField.setName(fieldKey);
                        internalField.setClassModel(savedAdditionalModel);
                        internalField.setType_name(finalAdditionalModelMap.get(fieldKey).getType());
                        internalFieldRepository.save(internalField);
                    }
                });
            }));

            //

            return ResponseEntity.ok("Endpoint успешно сохранен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }

    private boolean hasPrimitives(HashMap<String, FieldType> fieldsMap) {
        return fieldsMap.values().stream().anyMatch(FieldType::getPrimitive);
    }

    private int primitivesCount(HashMap<String, FieldType> fieldsMap) {
        return (int) fieldsMap.values().stream().filter(FieldType::getPrimitive).count();
    }
}
