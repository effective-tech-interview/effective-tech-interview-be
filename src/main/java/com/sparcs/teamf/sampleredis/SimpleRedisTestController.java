package com.sparcs.teamf.sampleredis;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SimpleRedisTestController {

    private final PersonRedisRepository personRedisRepository;

    @GetMapping("/simple/getSimpleData")
    public List<Person> getSimpleData() {
        return (List<Person>) personRedisRepository.findAll();
    }

    @GetMapping("/simple/saveSimpleData")
    public void removeSimpleData() {
        personRedisRepository.save(new Person("1", 1));
    }

    @GetMapping("/simple/removeSimpleData")
    public void saveSimpleData() {
        personRedisRepository.deleteAll();
    }
}
