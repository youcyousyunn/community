package com.ycs.community.sysbo.lucene;

import com.ycs.community.cmmbo.domain.po.QuestionPo;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexUtil {
    private static Logger logger = LoggerFactory.getLogger(IndexUtil.class);

	/**
     * 将对象数据保存到索引库
     * @param questionPo
     */
    public static void save(QuestionPo questionPo) {
        IndexWriter indexWriter = null;
        try {
            // 1. Object --> Document
            Document doc = TranslationUtil.trans2Document(questionPo);
            // 2. indexWriter.addDocument(document)
            indexWriter = LuceneUtil.getIndexWriter();
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            logger.error("将对象数据保存到索引库失败:\n {}", e.getMessage());
        } finally {
            LuceneUtil.closeIndexWriter(indexWriter);
        }
    }

    /**
     * 删除索引
     * @param id
     */
    public static void delete(Integer id) {
        IndexWriter indexWriter = null;
        try {
            indexWriter = LuceneUtil.getIndexWriter();
            indexWriter.deleteDocuments(new Term("id", id.toString()));
        } catch (IOException e) {
            logger.error("删除索引失败: {}", e.getMessage());
        } finally {
            LuceneUtil.closeIndexWriter(indexWriter);
        }
    }

    /**
     * 更新索引
     * 更新操作代价很高，一般采取先删除对应的索引，然后再创建这个索引的方式
     * @param questionPo
     */
    public static void update(QuestionPo questionPo) {
        IndexWriter indexWriter = null;
        try {
            Term term = new Term("id", questionPo.getId().toString());
            indexWriter = LuceneUtil.getIndexWriter();
            // Document doc = new Document();
            // doc.add(new TextField("title", questionPo.getTitle(), Store.YES));
            // doc.add(new TextField("content", questionPo.getContent(),
            // Store.YES));
            // indexWriter.updateDocument(new Term("title", "content"), doc);

            // 优化版本的实现就是：先删除愿索引，然后再创建该索引
            indexWriter.deleteDocuments(term);
            Document doc = TranslationUtil.trans2Document(questionPo);
            indexWriter.addDocument(doc);
        } catch (IOException e) {
            logger.error("更新索引失败: {}", e.getMessage());
        } finally {
            LuceneUtil.closeIndexWriter(indexWriter);
        }
    }

    /**
     * 从索引库中查询内容
     * @param queryString
     * @return
     */
    public List<QuestionPo> search(String queryString) {
        try {
            // 1, queryString --> Query
            String[] queryFields = new String[] { "description" };
            Analyzer analyzer = new StandardAnalyzer();
            analyzer.setVersion(Version.LUCENE_6_0_0.LUCENE_6_1_0);
            QueryParser queryParser = new MultiFieldQueryParser(queryFields, analyzer);
            Query query = queryParser.parse(queryString);

            // 2, 查询并得到topDocs
            IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
            TopDocs topDocs = indexSearcher.search(query, 100);

            // 3, 处理并返回结果
            int totalHits = topDocs.totalHits;
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<QuestionPo> result = new ArrayList<>();
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                Document doc = indexSearcher.doc(scoreDoc.doc);
                QuestionPo questionPo = TranslationUtil.trans2Object(doc);
                result.add(questionPo);
            }
            LuceneUtil.closeIndexSearcher(indexSearcher);
            return result.size() > 0 ? result : null;
        } catch (Exception e) {
            logger.error("从索引库中查询内容失败: {}", e.getMessage());
        }
        return null;
    }

}
