package com.chenlb.mmseg4j.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

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
	public Tokenizer create(AttributeFactory factory, Reader input) {
		MMSegTokenizer tokenizer = tokenizerLocal.get();
		if(tokenizer == null) {
			tokenizer = newTokenizer(input);
		} else {
			try {
				tokenizer.setReader(input);
			} catch (IOException e) {
				tokenizer = newTokenizer(input);
				logger.info("MMSegTokenizer.setReader i/o error by: e.getMessage()", e);
			}
		}

		return tokenizer;
	}

	private MMSegTokenizer newTokenizer(Reader input) {
		MMSegTokenizer tokenizer = new MMSegTokenizer(newSeg(getOriginalArgs()), input);
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
