package ru.itis.kpfu.mockdataserver.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.kpfu.mockdataserver.Constants;
import ru.itis.kpfu.mockdataserver.entity.GeneratedItem;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;
import ru.itis.kpfu.mockdataserver.entity.dao.InternalField;
import ru.itis.kpfu.mockdataserver.entity.dao.PrimitiveField;
import ru.itis.kpfu.mockdataserver.repository.ClassModelRepository;
import ru.itis.kpfu.mockdataserver.repository.EndpointRepository;
import ru.itis.kpfu.mockdataserver.repository.InternalFieldRepository;
import ru.itis.kpfu.mockdataserver.repository.PrimitiveFieldRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private DataGenerationService dataGenerationService;

    public String getResponse(String additionalPath, String userPath) {
        Endpoint endpoint = endpointRepository.findByAdditionalPathAndUserPath(additionalPath, userPath);
        List<ClassModel> classModels = endpoint.getClassModels();
        List<ClassModel> rootFilteredList =
            classModels.stream().filter(ClassModel::getRoot).collect(Collectors.toList());
        if (rootFilteredList.size() == 1) {
            ClassModel rootModel = rootFilteredList.get(0);
            if (!endpoint.isList()) {
                HashMap<String, Object> response = proceedInternalModels(
                        rootModel,
                        new HashMap<>(),
                        classModels,
                        endpoint.getLocale(),
                        endpoint.getIsRepresentative()
                );
                GsonBuilder builder = new GsonBuilder().setPrettyPrinting().serializeNulls();
                Gson gson = builder.create();
                return gson.toJson(response);
            } else {
                ArrayList<HashMap<String, Object>> responseList = new ArrayList<>();
                for (int i = 0; i < endpoint.getElementsCount(); i++) {
                    responseList.add(proceedInternalModels(
                            rootModel,
                            new HashMap<>(),
                            classModels,
                            endpoint.getLocale(),
                            endpoint.getIsRepresentative()
                    ));
                }
                GsonBuilder builder = new GsonBuilder().setPrettyPrinting().serializeNulls();
                Gson gson = builder.create();
                return gson.toJson(responseList);
            }
        } else {
            return "Внутренняя ошибка сервера";
        }
    }

    private HashMap<String, Object> proceedInternalModels(
        ClassModel model,
        HashMap<String, Object> currentMap,
        List<ClassModel> classModels,
        String locale,
        boolean isRepresentative
    ) {
        if (model.getHasPrimitive()) {
            List<PrimitiveField> primitiveFields = model.getPrimitiveFields();
            primitiveFields.forEach(field -> {
                proceedPrimitiveValueToResponse(field, currentMap, locale, isRepresentative);
            });
        }
        if (model.getHasInternal()) {
            List<InternalField> internalFields = model.getInternalFields();
            internalFields.forEach(internalField -> {
                List<ClassModel> additionalFilteredList = classModels
                    .stream()
                    .filter(m -> m.getName().equals(internalField.getTypeName()))
                    .collect(Collectors.toList());
                if (additionalFilteredList.size() == 1) {
                    ClassModel internalModel = additionalFilteredList.get(0);
                    currentMap.put(
                        internalField.getName(),
                        proceedInternalModels(internalModel, new HashMap<>(), classModels, locale, isRepresentative)
                    );
                }
            });
        }
        return currentMap;
    }

    private void proceedPrimitiveValueToResponse(
        PrimitiveField field,
        HashMap<String, Object> responseMap,
        String locale,
        boolean isRepresentative
    ) {
        if (!field.getIsStatic()) {
            GeneratedItem newValue =
                dataGenerationService.generateValue(field.getTypeName(), field.getName(), locale, isRepresentative);
            field.setStaticValue(newValue.getGeneratedValue());
        }
        addPrimitiveToResponseMap(field, responseMap);
    }

    public void addPrimitiveToResponseMap(PrimitiveField field, HashMap<String, Object> responseMap) {
        if (!field.getIsList()) {
            switch (field.getTypeId()) {
                case Constants.NUMBER_TYPE_BOOLEAN:
                    responseMap.put(field.getName(), Boolean.parseBoolean(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_INT:
                case Constants.NUMBER_TYPE_NUMBER:
                    responseMap.put(field.getName(), Integer.parseInt(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_LONG:
                    responseMap.put(field.getName(), Long.parseLong(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_DOUBLE:
                    responseMap.put(field.getName(), Double.parseDouble(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_FLOAT:
                    responseMap.put(field.getName(), Float.parseFloat(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_SHORT:
                    responseMap.put(field.getName(), Short.parseShort(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_BYTE:
                    responseMap.put(field.getName(), Byte.parseByte(field.getStaticValue()));
                    break;
                case Constants.NUMBER_TYPE_CHAR:
                    responseMap.put(field.getName(), field.getStaticValue().charAt(0));
                    break;
                case Constants.NUMBER_TYPE_STRING:
                    responseMap.put(field.getName(), field.getStaticValue());
                    break;
                default:
                    responseMap.put(field.getName(), null);
            }
        } else {
            String[] splitValue = field.getStaticValue().split(";");
            switch (field.getTypeId()) {
                case Constants.NUMBER_TYPE_BOOLEAN:
                    List<Boolean> resultBoolean =
                        Arrays.stream(splitValue).map(Boolean::parseBoolean).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultBoolean);
                    break;
                case Constants.NUMBER_TYPE_INT:
                case Constants.NUMBER_TYPE_NUMBER:
                    List<Integer> resultInt =
                        Arrays.stream(splitValue).map(Integer::parseInt).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultInt);
                    break;
                case Constants.NUMBER_TYPE_LONG:
                    List<Long> resultLong =
                        Arrays.stream(splitValue).map(Long::parseLong).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultLong);
                    break;
                case Constants.NUMBER_TYPE_DOUBLE:
                    List<Double> resultDouble =
                        Arrays.stream(splitValue).map(Double::parseDouble).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultDouble);
                    break;
                case Constants.NUMBER_TYPE_FLOAT:
                    List<Float> resultFloat =
                        Arrays.stream(splitValue).map(Float::parseFloat).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultFloat);
                    break;
                case Constants.NUMBER_TYPE_SHORT:
                    List<Short> resultShort =
                        Arrays.stream(splitValue).map(Short::parseShort).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultShort);
                    break;
                case Constants.NUMBER_TYPE_BYTE:
                    List<Byte> resultByte =
                        Arrays.stream(splitValue).map(Byte::parseByte).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultByte);
                    break;
                case Constants.NUMBER_TYPE_CHAR:
                    List<Character> resultCharacter =
                        Arrays.stream(splitValue).map(item -> item.charAt(0)).collect(Collectors.toList());
                    responseMap.put(field.getName(), resultCharacter);
                    break;
                case Constants.NUMBER_TYPE_STRING:
                    responseMap.put(field.getName(), splitValue);
                    break;
                default:
                    responseMap.put(field.getName(), null);
            }
        }
    }

}
