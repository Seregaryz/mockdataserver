package ru.itis.kpfu.mockdataserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.kpfu.mockdataserver.service.RequestHistoryService;
import ru.itis.kpfu.mockdataserver.service.MockApiService;

@RestController
@RequestMapping("/generator")
public class MockApiController {

    @Autowired
    private MockApiService mockApiService;

    @Autowired
    private RequestHistoryService requestHistoryService;

    @GetMapping("/{pathVariable}/{userPathVariable}")
    public ResponseEntity<String> getGeneratedValue(
            @PathVariable("pathVariable") String pathVariable,
            @PathVariable("userPathVariable") String userPathVariable
    ) {
        try {
            String response = mockApiService.getResponse(pathVariable, userPathVariable);
            if (response.startsWith("{") || response.startsWith("[")) {
                requestHistoryService.saveEndpointToHistory(response, pathVariable, userPathVariable);
                return ResponseEntity.ok().body(response);
            } else return ResponseEntity.badRequest().body("Неправильные данные");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }

}
