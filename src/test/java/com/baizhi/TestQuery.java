package com.baizhi;

import com.baizhi.entity.Poetry;
import com.baizhi.service.PoertyService;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestQuery {
    @Autowired
    private PoertyService poertyService;
    @Test
    public void contextLoads() throws Exception{

        Map<String, String> query = poertyService.query("月", 1, 100);

        Set<String> keys = query.keySet();

        keys.forEach(k-> System.out.println(k+"---"+query.get(k)));

    }
}