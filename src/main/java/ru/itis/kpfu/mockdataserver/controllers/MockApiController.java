package ru.itis.kpfu.mockdataserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.kpfu.mockdataserver.entity.dao.ClassModel;
import ru.itis.kpfu.mockdataserver.entity.dao.Endpoint;
import ru.itis.kpfu.mockdataserver.entity.dao.InternalField;
import ru.itis.kpfu.mockdataserver.entity.dao.PrimitiveField;
import ru.itis.kpfu.mockdataserver.service.MockApiService;

import java.util.List;

@RestController
@RequestMapping("/generator")
public class MockApiController {

    @Autowired
    private MockApiService mockApiService;

    @GetMapping("/{pathVariable}/{userPathVariable}")
    public ResponseEntity<String> getGeneratedValue(
            @PathVariable("pathVariable") String pathVariable,
            @PathVariable("userPathVariable") String userPathVariable
    ) {
        try {
            Endpoint endpoint = mockApiService.getEndpoint(pathVariable, userPathVariable);
            List<ClassModel> classModels = endpoint.getClassModels();
            List<PrimitiveField> primitiveFields = classModels.get(0).getPrimitiveFields();
            List<InternalField> internalFields = classModels.get(0).getInternalFields();

            if (pathVariable.equals("dina")) {
                return ResponseEntity.ok("Kiss kiss");
            } else if (pathVariable.equals("home")) {
                return ResponseEntity.ok("Go home");
            } else return ResponseEntity.ok("unknown request");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }

}
