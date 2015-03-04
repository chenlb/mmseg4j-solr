package com.chenlb.mmseg4j.analysis;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class UseLucene {

	Directory dir;

	@Before
	public void before() {
		dir = new RAMDirectory();
	}

	private Document createDoc(int id) {
		Document doc = new Document();
		FieldType ft = new FieldType();
		ft.setTokenized(true);
		ft.setStored(true);
		ft.setIndexOptions(IndexOptions.DOCS);
		doc.add(new Field("id", "" + id, ft));

		FieldType ft2 = new FieldType();
		ft2.setTokenized(true);
		ft.setStored(true);
		ft2.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		doc.add(new Field("name", "echo ensh id " + id, ft2));
		return doc;
	}

	@Test
	public void test() throws IOException {
		IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());
		IndexWriter iw = new IndexWriter(dir, iwc);
		iw.addDocument(createDoc(1));
		iw.addDocument(createDoc(2));
		iw.commit();

		iw.close();
	}

}
