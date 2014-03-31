package com.chenlb.mmseg4j.solr;

import java.io.File;

import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.solr.core.SolrResourceLoader;

import com.chenlb.mmseg4j.Dictionary;

public class Utils {

	public static Dictionary getDict(String dicPath, ResourceLoader loader) {
		Dictionary dic = null;
		if(dicPath != null) {
			File f = new File(dicPath);
			if(!f.isAbsolute() && loader instanceof SolrResourceLoader) {	//相对目录
				SolrResourceLoader srl = (SolrResourceLoader) loader;
				dicPath = srl.getInstanceDir()+dicPath;
				f = new File(dicPath);
			}

			dic = Dictionary.getInstance(f);
		} else {
			dic = Dictionary.getInstance();
		}
		return dic;
	}
}
