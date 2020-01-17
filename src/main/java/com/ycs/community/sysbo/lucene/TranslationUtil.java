package com.ycs.community.sysbo.lucene;

import com.ycs.community.cmmbo.domain.po.QuestionPo;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

public class TranslationUtil {

	/**
     * 将对象转换成Lucene的Document
     * @param questionPo
     * @return
     */
    public static Document trans2Document(QuestionPo questionPo) {
        Document doc = new Document();
        doc.add(new StringField("id", questionPo.getId().toString(), Store.YES));
        doc.add(new TextField("description", questionPo.getDescription(), Store.YES));
        return doc != null ? doc : null;
    }

    /**
     * 将Document转换回Article
     * @param document
     * @return
     */
    public static QuestionPo trans2Object(Document document) {
        QuestionPo questionPo = new QuestionPo();
        questionPo.setId(Long.parseLong(document.get("id")));
        questionPo.setDescription(document.get("description"));
        return questionPo != null ? questionPo : null;
    }

}
