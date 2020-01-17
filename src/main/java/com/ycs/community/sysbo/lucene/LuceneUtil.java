package com.ycs.community.sysbo.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class LuceneUtil {
    private static Logger logger = LoggerFactory.getLogger(LuceneUtil.class);
	private static Directory dir;
	private static Analyzer analyzer;

    public static Directory getDir() {
        return dir;
    }

    public static Analyzer getAnalyzer() {
        return analyzer;
    }

    /**
     * 获得一个用于操作索引库的IndexWriter对象
     * @return
     */
    public static IndexWriter getIndexWriter() {
        try {
            Directory dir = FSDirectory.open(Paths.get("./lucene/indexDir/"));
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
            IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);
            return indexWriter != null ? indexWriter : null;
        } catch (IOException e) {
            logger.error("获取一个用于操作索引库的IndexWriter对象失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 获得一个索引库查询对象
     * @return
     */
    public static IndexSearcher getIndexSearcher() {
        try {
            DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(Paths.get("./indexDir/")));
            IndexReader indexReader = directoryReader;
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            return indexSearcher != null ? indexSearcher : null;
        } catch (IOException e) {
            logger.error("获得一个索引库查询对象失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 释放IndexWriter资源
     * @param indexWriter
     */
    public static void closeIndexWriter(IndexWriter indexWriter) {
        try {
            if (indexWriter != null) {
                indexWriter.close();
                indexWriter = null;
            }
        } catch (IOException e) {
            logger.error("释放IndexWriter资源失败: {}", e.getMessage());
        }
    }

    /**
     * 释放IndexSearcher资源
     * @param indexSearcher
     */
    public static void closeIndexSearcher(IndexSearcher indexSearcher) {
        try {
            if (indexSearcher != null) {
                indexSearcher = null;
            }
        } catch (Exception e) {
            logger.error("释放IndexSearcher资源失败: {}", e.getMessage());
        }
    }
}
