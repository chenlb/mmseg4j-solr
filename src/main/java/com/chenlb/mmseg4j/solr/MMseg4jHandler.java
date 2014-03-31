package com.chenlb.mmseg4j.solr;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.handler.RequestHandlerBase;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.util.plugin.SolrCoreAware;

import com.chenlb.mmseg4j.Dictionary;

/**
 * mmseg4j 的 solr handler，用于检测词库，查看状态等。
 * 
 * @author chenlb 2009-10-12 上午10:53:38
 */
public class MMseg4jHandler extends RequestHandlerBase implements SolrCoreAware {
	
	//private File solrHome = null;
	private SolrResourceLoader loader = null;
	
	public String getDescription() {
		
		return "";
	}

	public String getSource() {
		
		return "$URL: http://mmseg4j.googlecode.com/svn/trunk/src/com/chenlb/mmseg4j/solr/MMseg4jHandler.java $";
	}

	public String getSourceId() {
		
		return "$Revision: 63 $";
	}

	public String getVersion() {
		
		return "1.8";
	}


	public void handleRequestBody(SolrQueryRequest req, SolrQueryResponse rsp) throws Exception {
		rsp.setHttpCaching(false);
		final SolrParams solrParams = req.getParams();

		String dicPath = solrParams.get("dicPath");
		Dictionary dict = Utils.getDict(dicPath, loader);

		NamedList<Object> result = new NamedList<Object>();
		result.add("dicPath", dict.getDicPath().toURI());

		boolean check = solrParams.getBool("check", false);	//仅仅用于检测词库是否有变化
		//用于尝试加载词库，有此参数, check 参数可以省略。
		boolean reload = solrParams.getBool("reload", false);	

		check |= reload;

		boolean changed = false;
		boolean reloaded = false;
		if(check) {
			changed = dict.wordsFileIsChange();
			result.add("changed", changed);
		}
		if(changed && reload) {
			reloaded = dict.reload();
			result.add("reloaded", reloaded);
		}
		rsp.add("result", result);
	}

	public void inform(SolrCore core) {
		loader = core.getResourceLoader();
		//solrHome = new File(loader.getInstanceDir());
	}

}
