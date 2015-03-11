package com.chenlb.mmseg4j.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;

/**
 * lucene 3.0 从 TokenStream 得到 Token 比较麻烦。
 *
 * @author chenlb 2010-10-7下午10:07:10
 */
public class TokenUtils {

	/**
	 * @param input
	 * @param reusableToken is null well new one auto.
	 * @return null - if not next token or input is null.
	 * @throws IOException
	 */
	public static PackedTokenAttributeImpl nextToken(TokenStream input, PackedTokenAttributeImpl reusableToken) throws IOException {
		if(input == null) {
			return null;
		}
		if(!input.incrementToken()) {
			return null;
		}

		CharTermAttribute termAtt = input.getAttribute(CharTermAttribute.class);
		OffsetAttribute offsetAtt = input.getAttribute(OffsetAttribute.class);
		TypeAttribute typeAtt = input.getAttribute(TypeAttribute.class);

		if(reusableToken == null) {
			reusableToken = new PackedTokenAttributeImpl();
		}

		reusableToken.clear();
		if(termAtt != null) {
			//lucene 3.0
			//reusableToken.setTermBuffer(termAtt.termBuffer(), 0, termAtt.termLength());
			//lucene 3.1
			reusableToken.copyBuffer(termAtt.buffer(), 0, termAtt.length());

		}
		if(offsetAtt != null) {
			//lucene 3.1
			//reusableToken.setStartOffset(offsetAtt.startOffset());
			//reusableToken.setEndOffset(offsetAtt.endOffset());
			//lucene 4.0
			reusableToken.setOffset(offsetAtt.startOffset(), offsetAtt.endOffset());
		}

		if(typeAtt != null) {
			reusableToken.setType(typeAtt.type());
		}

		return reusableToken;
	}

	public static PackedTokenAttributeImpl subToken(PackedTokenAttributeImpl oriToken, int termBufferOffset, int termBufferLength) {
		//CharTermAttributeImpl termImpl = new CharTermAttributeImpl();
		//termImpl.copyBuffer(oriToken.buffer(), termBufferOffset, termBufferLength);

		//new Token(oriToken.buffer(), termBufferOffset, termBufferLength,
		//		oriToken.startOffset()+termBufferOffset, oriToken.startOffset()+termBufferOffset+termBufferLength);
		//Token token = new Token(termImpl, oriToken.startOffset()+termBufferOffset, oriToken.startOffset()+termBufferOffset+termBufferLength);
		PackedTokenAttributeImpl token = new PackedTokenAttributeImpl();
		token.copyBuffer(oriToken.buffer(), termBufferOffset, termBufferLength);
		token.setOffset(oriToken.startOffset()+termBufferOffset, oriToken.startOffset()+termBufferOffset+termBufferLength);
		token.setType(oriToken.type());
		return token;
	}
}
