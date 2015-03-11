## mmseg4j-solr

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

版本

* mmseg4j-solr-2.0.0.jar 要求 lucene/solr >= 4.3.0。在 lucene/solr [4.3.0, 4.7.1] 测试过兼容可用。
* mmseg4j-solr-2.1.0.jar 要求 lucene/solr 4.8.x
* mmseg4j-solr-2.2.0.jar 要求 lucene/solr [4.9, 4.10.x]
* mmseg4j-solr-2.3.0.jar 要求 lucene/solr [5.0, ]

## download

[mmseg4j-solr](http://pan.baidu.com/s/1dD7qMFf)

## wiki

[wiki](https://github.com/chenlb/mmseg4j-solr/wiki)