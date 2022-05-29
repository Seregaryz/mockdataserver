package ru.itis.kpfu.mockdataserver.controllers;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import ru.itis.kpfu.mockdataserver.service.PluginDataService;

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

    @Autowired
    private DataGenerationService dataGenerationService;

    @Autowired
    private PluginDataService pluginDataService;

    @PostMapping("/add_new_endpoint")
    public ResponseEntity<String> sendPluginData(@RequestBody PluginResponse pluginResponse) {
        try {
            HashMap<String, LinkedTreeMap<String, Object>> jsonModelMap;
            HashMap<String, FieldType> modelMap;
            try {
                jsonModelMap = (HashMap) new Gson().fromJson(pluginResponse.getRootModel(), HashMap.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Некорректные данные модели");
            }
            modelMap = pluginDataService.parseJsonModelMap(jsonModelMap);

            List<LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String, Object>>>> jsonAdditionalModels;
            List<HashMap<String, HashMap<String, FieldType>>> additionalModelList;
            try {
                jsonAdditionalModels = (List) new Gson().fromJson(pluginResponse.getAdditionalModels(), List.class);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Некорректные данные вложенных моделей");
            }
            additionalModelList = pluginDataService.parseJsonAdditionalModelMap(jsonAdditionalModels);

            // save endpoint
            Endpoint endpoint = new Endpoint();
            endpoint.setUserPath(pluginResponse.getEndpoint());
            endpoint.setAdditionalPath(pluginResponse.getUserId());
            endpoint.setLocale(pluginResponse.getLocale());
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
                        pluginResponse.getLocale()
                    );
                    PrimitiveField primitiveField = new PrimitiveField();
                    primitiveField.setName(fieldKey);
                    primitiveField.setClassModel(savedRootClassModel);
                    primitiveField.setTypeId(generatedItem.getTypeId());
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
                            pluginResponse.getLocale()
                        );
                        PrimitiveField primitiveField = new PrimitiveField();
                        primitiveField.setName(fieldKey);
                        primitiveField.setClassModel(savedAdditionalModel);
                        primitiveField.setTypeId(generatedItem.getTypeId());
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

            StringBuilder responseBuilder = new StringBuilder();
            if (pluginResponse.getLocale().equals("Russian")) {
                responseBuilder.append("Endpoint успешно сохранен. Url для получения данных: ");
            } else responseBuilder.append("The endpoint was saved successfully. Url to get data: ");
            responseBuilder.append("http://localhost:8080/generator/");
            responseBuilder.append(pluginResponse.getUserId());
            if (!pluginResponse.getEndpoint().startsWith("/")) responseBuilder.append("/");
            responseBuilder.append(pluginResponse.getEndpoint());
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body((new Gson().toJson(responseBuilder.toString())));
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
