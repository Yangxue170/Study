### Dubbo调用log记录展示
> 主要是针对于Apache的dubbo进行学习

1、定义一个MyDubboLogFilter类 ，implements Filter（org.apache.dubbo.rpc.Filter）
   重写invoke方法，将想关注的日志进行记录，格式限制
    `//执行具体的方法
    result =  invoker.invoke(invocation);`
2、自定义类上面添加注解
    @Activate(group = { CommonConstants.CONSUMER,CommonConstants.PROVIDER})
    group值表示在 provider端/consumer端 进行使用

3、自定义的类怎么找到呢？ META-INF下的dubbo目录下，自定义org.apache.dubbo.rpc.Filter（自定义org.apache.dubbo.rpc文件名，Fliter为后缀）
    resources/META-INF/dubbo/org.apache.dubbo.rpc.Filter
    `指定自定义类
    dubboLoggerFilter=com.learn.log.dubbo.DubboLoggerFilter
    `
测试：
Provider进行服务发布：spring-dubbo-provider.xml
Consumer进行服务使用：spring-dubbo-consumer.xml

观察日志输出的格式，或者debug到自定义的filter中查看是否按照自定义输出即可。









































