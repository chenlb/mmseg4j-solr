package com.chenlb.mmseg4j.solr;

import com.chenlb.mmseg4j.Dictionary;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.solr.core.SolrResourceLoader;

import java.io.File;

public class Utils {

    public static Dictionary getDict(String dicPath, ResourceLoader loader) {
        Dictionary dic;
        if (dicPath != null) {
            File f = new File(dicPath);
            if (!f.isAbsolute() && loader instanceof SolrResourceLoader) {    //相对目录
                SolrResourceLoader srl = (SolrResourceLoader) loader;
                dicPath = srl.getInstancePath() + dicPath;
                f = new File(dicPath);
            }

            dic = Dictionary.getInstance(f);
        } else {
            dic = Dictionary.getInstance();
        }
        return dic;
    }
}
