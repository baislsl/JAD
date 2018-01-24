# Simple CSV editor

## 依赖和引用

- Porter Stemmer算法实现使用gov.sandia.foundry.porter-stemmer包
- json读写 com.googlecode.json-simple
- 问答数据来源dev-v1.1.json [The Stanford Question Answering Dataset](https://rajpurkar.github.io/SQuAD-explorer/dataset/dev-v1.1.json)
- 基本的stop word词来源见[https://www.ranks.nl/stopwords](https://www.ranks.nl/stopwords)


## 简单算法实现介绍

### 读入数据集

1. 读入每个question的问题和答案
1. 建立使用porter stemmer算法简化后的单词和question的map
1. 提取关键词， 首先在基本关键词的基础上，将读入文章中出现次数频繁的单词加入关键词中

### 分析输入

1. porter stemmer算法简化
1. stopword过滤
1. 筛选出现输入出现次数最多的文章

