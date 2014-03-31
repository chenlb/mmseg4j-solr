package com.chenlb.mmseg4j.solr;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource.AttributeFactory;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

public class MMSegTokenizerFactory extends TokenizerFactory implements ResourceLoaderAware {

	static final Logger log = Logger.getLogger(MMSegTokenizerFactory.class.getName());
	/* 线程内共享 */
	private ThreadLocal<MMSegTokenizer> tokenizerLocal = new ThreadLocal<MMSegTokenizer>();
	private Dictionary dic = null;

	public MMSegTokenizerFactory(Map<String, String> args) {
		super(args);
	}

	private Seg newSeg(Map<String, String> args) {
		Seg seg = null;
		log.info("create new Seg ...");
		//default max-word
		String mode = args.get("mode");
		if("simple".equals(mode)) {
			log.info("use simple mode");
			seg = new SimpleSeg(dic);
		} else if("complex".equals(mode)) {
			log.info("use complex mode");
			seg = new ComplexSeg(dic);
		} else {
			log.info("use max-word mode");
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
				log.info("MMSegTokenizer.reset i/o error by:"+e.getMessage());
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

		log.info("dic load... in="+dic.getDicPath().toURI());
	}

}
