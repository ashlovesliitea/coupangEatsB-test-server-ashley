package com.example.demo.src.test;

import com.example.demo.annotation.NoAuth;
import com.example.demo.src.test.model.GetTestRes;
import org.aspectj.weaver.ast.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TestController(TestDao testDao) {
        this.testDao = testDao;
    }

    private TestDao testDao;


    /**
     * 로그 테스트 API
     * [GET] /test/log
     * @return String
     */
    @ResponseBody
    @GetMapping("/log")
    public String getAll() {
        System.out.println("테스트");
//        trace, debug 레벨은 Console X, 파일 로깅 X
//        logger.trace("TRACE Level 테스트");
//        logger.debug("DEBUG Level 테스트");

//        info 레벨은 Console 로깅 O, 파일 로깅 X
        logger.info("INFO Level 테스트");
//        warn 레벨은 Console 로깅 O, 파일 로깅 O
        logger.warn("Warn Level 테스트");
//        error 레벨은 Console 로깅 O, 파일 로깅 O (app.log 뿐만 아니라 error.log 에도 로깅 됨)
//        app.log 와 error.log 는 날짜가 바뀌면 자동으로 *.gz 으로 압축 백업됨
        logger.error("ERROR Level 테스트");

        return "Success Test";
    }
    @NoAuth
    @ResponseBody
    @GetMapping("/DB")
    public void testDB(){
        testDao.DBTest();
    }

    @NoAuth
    @ResponseBody
    @GetMapping("/dtotest")
    public void testDto(@RequestBody GetTestRes getTestRes){
        System.out.println("getTestRes.get = " + getTestRes.getTestString());
    }
}

