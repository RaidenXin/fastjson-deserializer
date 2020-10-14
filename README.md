# fastjson-deserializer
fastjson-deserializer
这是一个FastJson自定义反序列化ValueMutator(值修改器)  
可以在反序列化的过程中，对模型的值进行修改。  
注：对FastJson反序列化的速度有一定的影响。本人是使用于json配置国际化，在反序列化的过程中将占位符替换成相应的中英文文案。然后缓存起来使用。  
使用方式有两种,一种是直接通过 @JsonDeserializer 注解的valueMutators属性注入这种方式有个缺陷,你的修改器必须有无参构造函数的 
 
@JsonDeserializer(valueMutators = {CoustomizeValueMutator.class})  
@Getter  
@Setter  
public class User {

    private String a;
    private String id;
    private String student;
    private List<String> contents;
    private String url;

    @Override
    public String toString() {
        return "User{" +
                "a='" + a + '\'' +
                ", id='" + id + '\'' +
                ", student='" + student + '\'' +
                ", contents='" + contents.toString() + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}  


第二种方式是通过直接创建的方式,使用反序列化工具，这种方式支持有参构造函数。  

        CoustomizeValueMutator zhCoustomizeValueMutator = new CoustomizeValueMutator(zh);  
        User user = CustomizeJSON.parseObject(userStr, User.class, zhCoustomizeValueMutator);
        