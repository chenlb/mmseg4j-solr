package com.chenlb.mmseg4j.solr;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.solr.analysis.TokenizerChain;
import org.apache.solr.schema.FieldType;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.Dictionary.FileLoading;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.SimpleSeg;
import com.chenlb.mmseg4j.analysis.AnalyzerTest;

public class MMSegTokenizerFactoryTest extends AbstractSolrTestCase {

	private static final Logger logger = LoggerFactory.getLogger(MMSegTokenizerFactoryTest.class);

	@BeforeClass
	public static void beforeClass() throws Exception {
		initCore("solrconfig.xml", "schema.xml", getFile("solr/mmseg4j_core").getParent(), "mmseg4j_core");
	}

	private Dictionary getDictionaryByFieldType(String fieldTypeName) {
		FieldType ft = h.getCore().getLatestSchema().getFieldTypeByName(fieldTypeName);
		Analyzer a = ft.getAnalyzer();
		Assert.assertEquals(a.getClass(), TokenizerChain.class);
		
		TokenizerChain tc = (TokenizerChain) a;
		TokenizerFactory tf = tc.getTokenizerFactory();
		Assert.assertEquals(tf.getClass(), MMSegTokenizerFactory.class);
		
		MMSegTokenizerFactory mtf = (MMSegTokenizerFactory) tf;
		
		Assert.assertNotNull(mtf.dic);
		return mtf.dic;
	}

	private void assertTokenizerFactory(final String fieldName, final Seg seg) throws IOException {
		logger.info("assert TokenizerFactory field type={}", fieldName);
		FileInputStream fis = new FileInputStream("src/test/resources/text-sentence.txt");
		try {
			Dictionary.load(fis, new FileLoading() {

				@Override
				public void row(String line, int n) {
					List<String> mwords = AnalyzerTest.toMMsegWords(line, seg);

					assertU(adoc("id", String.valueOf(n), fieldName, line));
					assertU(commit());

					logger.debug("words = {}", mwords);
					for (String word : mwords) {
						assertQ(req("q", "id:" + String.valueOf(n) + " AND " + fieldName + ":" + word),
								"//*[@numFound='1']",
								"//result/doc[1]/int[@name='id'][.='" + String.valueOf(n) + "']");
					}
				}
			});
		} finally {
			fis.close();
		}
	}

	@Test
	public void test_mmseg4j() throws IOException {
		assertTokenizerFactory("textSimple", new SimpleSeg(getDictionaryByFieldType("textSimple")));
		assertTokenizerFactory("textComplex", new ComplexSeg(getDictionaryByFieldType("textComplex")));
		assertTokenizerFactory("textMaxWord", new MaxWordSeg(getDictionaryByFieldType("textMaxWord")));
	}

}
