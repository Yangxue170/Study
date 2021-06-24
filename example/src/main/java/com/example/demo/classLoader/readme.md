## 类的生命周期

加载、 验证、 准备、 解析、 初始化、 使用、 卸载

## 类加载过程

加载：查找并加载类的二进制数据（class文件）
    |_方法区：类的类信息 -堆：class文件对应的类信息 
验证：确保加载的类信息是正确的 
准备：为类的静态变量进行初始化，分配空间并分配初始值 
解析：将符号引用直接转化为直接引用 类似于int
    |_a=2; 将a替换为具体的地址 
初始化：JVM对类进行初始化，对静态代码变量赋予正确值
    |_静态代码块执行

### 类加载器

`       
ClassLoader classLoader = this.getClass().getClassLoader();
`
> 输出的classLoader 为 sun.misc.Launcher$AppClassLoader@18b4aac2

AppClassLoader

classLoader.getParent()
: sun.misc.Launcher$ExtClassLoader@34c45dca

classLoader.getParent().getParent()
：BootStrapClassLoader(c语言写的，打印不出来，返回null)

#### 类加载器 层级：
BootStrapClassLoader 
    |_加载(JDK/JRE/LIB java.)目录下
ExtClassLoader
    |_加载(JDK/JRE/LIB/EXT javax.)目录下
AppClassLoader
    |_自己定义的类，类路径下面
用户自定义类加载器

case:
> String.class.getClassLoader()  返回null，表示其是BootStrapClassLoader加载

### 双亲委派模型
> 先向上⬆️ 再向下⬇️ 
> 如果一个类加载器收到了类加载的请求，它首先不会自己去尝试加载这个类，而是委托给父加载器去完成，依次向上。
> 因此：所有的类加载器请求最终都应该被传递到顶层的启动类加载器(BootStrapClassLoader)中。
> 只有当父加载器在他的搜索范围内没有找到所需的类时，即无法完成加载，子加载器才会尝试自己去加载。

1、避免类的重复加载
2、防止核心API库被随意篡改



































