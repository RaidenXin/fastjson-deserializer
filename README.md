# fastjson-deserializer
fastjson-deserializer
这是一个FastJson自定义反序列化ValueMutator(值修改器)  
可以在反序列化的过程中，对模型的值进行修改。  
注：对FastJson反序列化的速度有一定的影响。  
本人是使用于json配置国际化，在反序列化的过程中将占位符替换成相应的中英文文案。然后缓存起来使用。  