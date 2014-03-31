package com.chenlb.mmseg4j.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;

public class UseLucene {

	Directory dir;

	@Before
	public void before() {
		dir = new RAMDirectory();
	}

	private Document createDoc(int id) {
		Document doc = new Document();
		FieldType ft = new FieldType();
		ft.setIndexed(true);
		doc.add(new Field("id", "" + id, ft));

		FieldType ft2 = new FieldType();
		ft2.setIndexed(true);
		ft2.setTokenized(true);
		ft2.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		doc.add(new Field("name", "echo ensh id " + id, ft2));
		return doc;
	}

	@Test
	public void test() throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(
				Version.LUCENE_CURRENT));
		IndexWriter iw = new IndexWriter(dir, iwc);
		iw.addDocument(createDoc(1));
		iw.addDocument(createDoc(2));
		iw.commit();

		iw.close();
	}

}
