package com.chenlb.mmseg4j.analysis;

import java.io.Reader;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * 
 * @author chenlb 2014年3月30日 下午7:19:57
 */
public class CutLetterDigitFilterTest {

	@Test
	public void testCutLeeterDigitFilter() {
		String myTxt = "mb991ch cq40-519tx mmseg4j ";
		List<String> words = AnalyzerTest.toWords(myTxt, new MMSegAnalyzer("") {

			@Override
			protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
				Tokenizer t = new MMSegTokenizer(newSeg(), reader);
				return new TokenStreamComponents(t, new CutLetterDigitFilter(t));
			}

		});

		Assert.assertArrayEquals("CutLeeterDigitFilter fail", words.toArray(new String[words.size()]),
				"mb 991 ch cq 40 519 tx mmseg 4 j".split(" "));
	}
}
