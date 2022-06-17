package ru.itis.kpfu.mockdataserver.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.kpfu.mockdataserver.entity.dao.HistoryEndpoint;
import ru.itis.kpfu.mockdataserver.service.RequestHistoryService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class RequestHistoryController {

    @Autowired
    RequestHistoryService requestHistoryService;

    @GetMapping("/endpoint_list")
    public ResponseEntity<String> sendEndpointList(@RequestParam("user_id") String userId) {
        try {
//            List<EndpointItem> endpointItems = adminService.getEndpointList(userId);
            List<HistoryEndpoint> endpointItems = requestHistoryService.getHistoryEndpointList(userId);
            Gson responseGson = new Gson();
            String response = responseGson.toJson(endpointItems);
                return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }
}

