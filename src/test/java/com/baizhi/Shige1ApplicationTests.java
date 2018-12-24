package com.baizhi;

import com.baizhi.entity.Poetry;
import com.baizhi.service.PoertyService;
import org.apache.jasper.tagplugins.jstl.core.If;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class Shige1ApplicationTests {
    @Autowired
    private PoertyService poertyService;
    @Test
    public void contextLoads() {//查询所有的诗
        List<Poetry> poetries = poertyService.queryAll();

        poetries.forEach(p-> System.out.println(p));
    }
    @Test
    public void testSave() throws Exception{//生成索引

        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\shige1\\01"));

        IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new IKAnalyzer()));


        List<Poetry>  list = poertyService.queryAll();

        Document document = null;
        int id = 0;


        for (Poetry p : list) {
            document = new Document();
            document.add(new IntField("id",id,Field.Store.YES));

            document.add(new TextField("title",p.getTitle(),Field.Store.YES));
            document.add(new TextField("content",p.getContent(),Field.Store.YES));
            document.add(new TextField("author",p.getPoet().getName(),Field.Store.YES));

            id ++;
            indexWriter.addDocument(document);
        }
        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void testQuery() throws Exception{
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\shige1\\01"));
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
        Query query = null;

        query = new TermQuery(new Term("content","李白"));

        BooleanQuery bq = new BooleanQuery.Builder().add(query,BooleanClause.Occur.SHOULD).build();

        TopDocs topDocs = indexSearcher.search(query,20);

        ScoreDoc[] scoreDoc = topDocs.scoreDocs;

        for (ScoreDoc doc : scoreDoc) {
            int docID = doc.doc;

            Document document = indexReader.document(docID);
            System.out.println(document.get("title")+"------------"+document.get("content"));

        }
        indexReader.close();

    }
    @Test
    public void testPage() throws  Exception{
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("D:\\shige1\\01"));
        IndexReader indexReader = DirectoryReader.open(fsDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("content",new IKAnalyzer());
        Query query = null;
        String yue = "月";

        query = queryParser.parse("title:"+yue);
        query = queryParser.parse(yue);

        int nowPage = 1;
        int pageSize= 10;
        TopDocs topDocs = null;

        if (nowPage == 1 || nowPage < 1){
            // 假如说: 查第一页 每页2条
            topDocs = indexSearcher.search(query, pageSize);
        } else if( nowPage > 1){
            // 假如说: 不是第一页 必须先获取上一页的最后一条记录的ScoreDoc
            topDocs = indexSearcher.search(query, (nowPage-1) * pageSize);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            ScoreDoc sd = scoreDocs[scoreDocs.length-1];
            // 参数一: 当前页的上一页的最后的文档的ScoreDoc对象
            topDocs = indexSearcher.searchAfter(sd,query,pageSize);
            System.out.println("1");
        }

        int count = topDocs.totalHits;
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        //高亮
        Scorer scorer = new QueryScorer(query);
        Formatter formatter = new SimpleHTMLFormatter("<span style=\"color:red\">","</span>");
        Highlighter highlighter = new Highlighter(formatter,scorer);

        for (ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;

            Document document = indexReader.document(docID);
            //System.out.println(document.get("title")+"----"+"作者"+document.get("author")+document.get("content"));
            String highlighterBestFragment = highlighter.getBestFragment(new IKAnalyzer(), "content", document.get("content"));
            if (highlighterBestFragment==null){highlighterBestFragment=document.get("content");};

            String highlighterBestFragment2 = highlighter.getBestFragment(new IKAnalyzer(), "title", document.get("title"));
            if (highlighterBestFragment2==null){highlighterBestFragment2=document.get("title");}

            System.out.println(highlighterBestFragment2+"-------------"+highlighterBestFragment);
        }



//        query = new TermQuery(new Term("content","月"));
//
        //String q = "李白";
        //QueryParser queryParser2 = new QueryParser("content",new IKAnalyzer());

       // BooleanQuery bq = new BooleanQuery.Builder().add(query,BooleanClause.Occur.SHOULD).build();
//
//        TopDocs topDocs = indexSearcher.search(query,20);
//
//        ScoreDoc[] scoreDoc = topDocs.scoreDocs;
//
//        for (ScoreDoc doc : scoreDoc) {
//            int docID = doc.doc;
//
//            Document document = indexReader.document(docID);
//            System.out.println(document.get("title")+"----"+document.get("content"));
//
//        }
        indexReader.close();




    }












    }


