mmseg4j-solr
============

mmseg4j for lucene or solr


```xml
<fieldtype name="textComplex" class="solr.TextField" positionIncrementGap="100">
	<analyzer>
		<tokenizer class="com.chenlb.mmseg4j.solr.MMSegTokenizerFactory" mode="complex" dicPath="dic"/>
	</analyzer>
</fieldtype>
<fieldtype name="textMaxWord" class="solr.TextField" positionIncrementGap="100">
	<analyzer>
		<tokenizer class="com.chenlb.mmseg4j.solr.MMSegTokenizerFactory" mode="max-word" />
	</analyzer>
</fieldtype>
<fieldtype name="textSimple" class="solr.TextField" positionIncrementGap="100">
	<analyzer>
		<tokenizer class="com.chenlb.mmseg4j.solr.MMSegTokenizerFactory" mode="simple" dicPath="n:/custom/path/to/my_dic" />
	</analyzer>
</fieldtype>
```
tokenizer 的参数：
 * dicPath 参数 － 设置自定义的扩展词库，支持相对路径(相对于 solr_home).
 * mode 参数 － 分词模式。