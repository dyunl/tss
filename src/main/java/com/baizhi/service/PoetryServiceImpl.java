package com.baizhi.service;

import com.baizhi.dao.PoetryDAO;
import com.baizhi.entity.Poetry;

import com.baizhi.view.MessageCount;
import jdk.nashorn.internal.runtime.Version;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class PoetryServiceImpl implements PoertyService {
    @Resource
    private PoetryDAO poetryDAO;

    @Override
    public List<Poetry> queryAll() {
        return poetryDAO.findAll();
    }
    @Override
    public MessageCount query(String message, Integer page, Integer size) throws Exception {
        //System.out.println(message+"--------"+page+"---------"+size);
//多域
        MessageCount messageCount = new MessageCount();

        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\shige1\\01"));
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
        Query query = null;

        query = queryParser.parse("author:"+message+"or title:"+message+"or content:"+message);
//        query = queryParser.parse(message);
//        query = queryParser.parse("title:"+message);
       // String[] fields = { "author","content","title"};
      //  Query query = new MultiFieldQueryParser(Version,fields, new IKAnalyzer()).parse(message);


//分页
        int nowPage = page;
        int pageSize= size;
        TopDocs topDocs = null;

        if (nowPage == 1 || nowPage < 1){
            topDocs = indexSearcher.search(query, pageSize);
            // 总记录数
        } else if( nowPage > 1){
            // 假如说: 不是第一页 必须先获取上一页的最后一条记录的ScoreDoc
            topDocs = indexSearcher.search(query, (nowPage-1) * pageSize);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            ScoreDoc sd = scoreDocs[scoreDocs.length-1];
            // 参数一: 当前页的上一页的最后的文档的ScoreDoc对象
            topDocs = indexSearcher.searchAfter(sd,query,pageSize);
            System.out.println();

        }

        int count = topDocs.totalHits;
        int counts = count%size==0?count/size:count/size+1;
        messageCount.setCount(counts);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //高亮
        Scorer scorer = new QueryScorer(query);
        Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>","</span>");
        Highlighter highlighter = new Highlighter(formatter,scorer);
        HashMap<String, String> map = new HashMap<>();
        for (ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;

            Document document = indexReader.document(docID);

            String highlighterBestFragment = highlighter.getBestFragment(new IKAnalyzer(), "content", document.get("content"));
            if (highlighterBestFragment==null){highlighterBestFragment=document.get("content");}

            String highlighterBestFragment2 = highlighter.getBestFragment(new IKAnalyzer(), "title", document.get("title"));
            if (highlighterBestFragment2==null){highlighterBestFragment2=document.get("title");}

            String autor = highlighter.getBestFragment(new IKAnalyzer(), "author", document.get("author"));
            if (autor==null){autor=document.get("author");}
            map.put(highlighterBestFragment,highlighterBestFragment2+"----作者----"+autor);
        }

        indexReader.close();
        messageCount.setMap(map);
        return messageCount;
    }
}
