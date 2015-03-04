package com.chenlb.mmseg4j.analysis;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class MMSegTokenizer extends Tokenizer {

	private MMSeg mmSeg;

	private CharTermAttribute termAtt;
	private OffsetAttribute offsetAtt;
	private TypeAttribute typeAtt;

	private final Seg seg;

	public MMSegTokenizer(Seg seg) {
		super();
		this.seg = seg;

		termAtt = addAttribute(CharTermAttribute.class);
		offsetAtt = addAttribute(OffsetAttribute.class);
		typeAtt = addAttribute(TypeAttribute.class);
	}

	public void reset() throws IOException {
		super.reset();
		//lucene 4.0
		//org.apache.lucene.analysis.Tokenizer.setReader(Reader)
		//setReader 自动被调用, input 自动被设置。
		if(mmSeg == null) {
			mmSeg = new MMSeg(input, seg);
		} else {
			mmSeg.reset(input);
		}
	}

/*//lucene 2.9 以下
 	public Token next(Token reusableToken) throws IOException {
		Token token = null;
		Word word = mmSeg.next();
		if(word != null) {
			//lucene 2.3
			reusableToken.clear();
			reusableToken.setTermBuffer(word.getSen(), word.getWordOffset(), word.getLength());
			reusableToken.setStartOffset(word.getStartOffset());
			reusableToken.setEndOffset(word.getEndOffset());
			reusableToken.setType(word.getType());

			token = reusableToken;

			//lucene 2.4
			//token = reusableToken.reinit(word.getSen(), word.getWordOffset(), word.getLength(), word.getStartOffset(), word.getEndOffset(), word.getType());
		}

		return token;
	}*/

	//lucene 2.9/3.0
	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		Word word = mmSeg.next();
		if(word != null) {
			//lucene 3.0
			//termAtt.setTermBuffer(word.getSen(), word.getWordOffset(), word.getLength());
			//lucene 3.1
			termAtt.copyBuffer(word.getSen(), word.getWordOffset(), word.getLength());
			offsetAtt.setOffset(word.getStartOffset(), word.getEndOffset());
			typeAtt.setType(word.getType());
			return true;
		} else {
			return false;
		}
	}
}
