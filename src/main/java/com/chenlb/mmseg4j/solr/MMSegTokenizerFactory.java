package com.chenlb.mmseg4j.solr;

import com.chenlb.mmseg4j.*;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MMSegTokenizerFactory extends TokenizerFactory implements ResourceLoaderAware {

	private static final Logger logger = LoggerFactory.getLogger(MMSegTokenizerFactory.class);
	/* 线程内共享 */
	private ThreadLocal<MMSegTokenizer> tokenizerLocal = new ThreadLocal<MMSegTokenizer>();
	// protected dic for test
	protected Dictionary dic = null;

	public MMSegTokenizerFactory(Map<String, String> args) {
		super(args);
	}

	private Seg newSeg(Map<String, String> args) {
		Seg seg = null;
		logger.info("create new Seg ...");
		//default max-word
		String mode = args.get("mode");
		if("simple".equals(mode)) {
			logger.info("use simple mode");
			seg = new SimpleSeg(dic);
		} else if("complex".equals(mode)) {
			logger.info("use complex mode");
			seg = new ComplexSeg(dic);
		} else {
			logger.info("use max-word mode");
			seg = new MaxWordSeg(dic);
		}
		return seg;
	}

	@Override
	public Tokenizer create(AttributeFactory factory) {
		MMSegTokenizer tokenizer = tokenizerLocal.get();
		if(tokenizer == null) {
			tokenizer = newTokenizer();
		}

		return tokenizer;
	}

	private MMSegTokenizer newTokenizer() {
		MMSegTokenizer tokenizer = new MMSegTokenizer(newSeg(getOriginalArgs()));
		tokenizerLocal.set(tokenizer);
		return tokenizer;
	}

	@Override
	public void inform(ResourceLoader loader) {
		String dicPath = getOriginalArgs().get("dicPath");

		dic = Utils.getDict(dicPath, loader);

		logger.info("dic load... in={}", dic.getDicPath().toURI());
	}

}
