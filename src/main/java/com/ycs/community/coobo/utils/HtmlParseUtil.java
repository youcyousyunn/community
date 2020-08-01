package com.ycs.community.coobo.utils;

import com.ycs.community.coobo.domain.po.JDDocumentPo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HtmlParseUtil {
    /**
     * 解析网页
     * @throws IOException
     * @return
     */
    public static List<JDDocumentPo> parseHtml(String uri) throws IOException {
        URL url = new URL(uri);
        Document document = Jsoup.parse(url, 60000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        List<JDDocumentPo> result = new ArrayList<>();
        for (Element item : elements) {
            String img = item.getElementsByTag("img").eq(0).attr("src");
            String price = item.getElementsByClass("p-price").eq(0).text();
            String name = item.getElementsByClass("p-name").eq(0).text();

            JDDocumentPo jdDocumentPo = new JDDocumentPo();
            jdDocumentPo.setImg(img);
            jdDocumentPo.setPrice(price);
            jdDocumentPo.setName(name);
            result.add(jdDocumentPo);
        }

        return result;
    }
}
