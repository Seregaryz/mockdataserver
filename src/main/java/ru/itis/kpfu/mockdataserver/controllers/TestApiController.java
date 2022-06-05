package ru.itis.kpfu.mockdataserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.kpfu.mockdataserver.entity.dao.representative.*;
import ru.itis.kpfu.mockdataserver.repository.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@RestController
@RequestMapping("/begin")
public class TestApiController {

    @Autowired
    AdverbRepository adverbRepository;

    @Autowired
    AdjectiveRepository adjectiveRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    NounRepository nounRepository;

    @Autowired
    AdjectiveRussianRepository adjectiveRussianRepository;

    @Autowired
    VerbRussianRepository verbRussianRepository;

    @Autowired
    NounRussianRepository nounRussianRepository;

    @Autowired
    AdverbRussianRepository adverbRussianRepository;

    @GetMapping("/{pathVariable}")
    public ResponseEntity<String> greeting(@PathVariable("pathVariable") String pathVariable) {
        try {
            if (pathVariable.equals("adverb")) {
                String fileName = "adverbs.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    Adverb adverb = new Adverb();
                    adverb.setValue(st);
                    adverbRepository.save(adverb);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("adjective")) {
                String fileName = "adjectives.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    Adjective adjective = new Adjective();
                    adjective.setValue(st);
                    adjectiveRepository.save(adjective);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("verb")) {
                String fileName = "verbs.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    Verb verb = new Verb();
                    verb.setValue(st);
                    verbRepository.save(verb);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("noun")) {
                String fileName = "nouns.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    Noun noun = new Noun();
                    noun.setValue(st);
                    nounRepository.save(noun);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("russian_adjectives")) {
                String fileName = "прилагательные.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    AdjectiveRussian adjectiveRussian = new AdjectiveRussian();
                    adjectiveRussian.setValue(st);
                    adjectiveRussianRepository.save(adjectiveRussian);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("russian_verbs")) {
                String fileName = "глаголы.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    VerbRussian verbRussian = new VerbRussian();
                    verbRussian.setValue(st);
                    verbRussianRepository.save(verbRussian);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("russian_nouns")) {
                String fileName = "существительные.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    if (!st.endsWith("а") && !st.endsWith("ь") && !st.endsWith("я") && !st.endsWith("ю") &&
                        !st.endsWith("у") && !st.endsWith("о") && !st.endsWith("е") && !st.endsWith("и") &&
                        !st.endsWith("ы") && !st.endsWith("и") && !st.endsWith("э") && !st.endsWith("з") &&
                        !st.endsWith("щ") && !st.endsWith("х") && !st.endsWith("й")) {
                        NounRussian nounRussian = new NounRussian();
                        nounRussian.setValue(st);
                        nounRussianRepository.save(nounRussian);
                    }
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("russian_adverbs")) {
                String fileName = "наречия.txt";
                File file = new File(fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    AdverbRussian adverbRussian = new AdverbRussian();
                    adverbRussian.setValue(st);
                    adverbRussianRepository.save(adverbRussian);
                }
                return ResponseEntity.ok("It's okay");
            } else if (pathVariable.equals("test")) {
                    return ResponseEntity.ok("test response");
                } else return ResponseEntity.ok("unknown request");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Неправильный запрос");
        }
    }

}
