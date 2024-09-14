## 简介

安卓网络请求框架封装

采用rxjava2+retrofit2的模式，链式调用并返回Observable对象

## 使用方法

### 初始化

```plain
//初始化
 SmoothHttp.getInstance().init("http://apis.juhe.cn/")//定义请求域名或根目录
                        .globalConfig()//获取默认请求配置
                        .addGlobalHeader("1","2")//添加默认请求头
                        .addGlobalParam("2","3")//添加默认请求参数
                        .defaultValue(new NullDataValue().defString("").defInt(0));//设置返回类型为空时的默认值
```
### GET请求

```plain
//get请求
SmoothHttp.get("mobile/get")
           .addHeader("8","8")//添加请求头
           .addParams(m)//添加请求参数
           .request(PhoneDTO.class);//发送请求，传递解析返回值的类型,返回Observerable<?>
           // .requestObj(PhoneDTO.class);发送请求，返回Observerable<PhoneDTO>。
           // .requestList(PhoneDTO.class);发送请求，返回Observerable<List<PhoneDTO>>
```

### POST请求

```plain
//post请求
SmoothHttp.post("mobile/post")
               .addParams(m)//添加请求参数
               .isQuery(true)//是否Query方式
               .type(AddressDTO.class)//传递解析参数类
               .request(AddressDTO.class);//发送请求
```
### 上传文件

```plain
//上传文件
SmoothHttp.upload("http://192.168.129.155:84/master/test/con/uploadFile")
        .urlParams("userid","222")
        .file("file",file)
        .request(callBack);
```
### 下载文件

```plain
// 下载文件
SmoothHttp.download("http://download.llmmwl.com/dXB-4.1.2-release.apk")
            .addParams(m)
            .tag(tag)
            .request(callBack);
```

## 错误码

### 公共错误码

```plain
302 页面重定向
303 重复提交
401 认证异常
403 操作未授权
404 资源不存在
408 服务器执行超时
500 服务器内部错误
503 服务器不可用
```
### 自定义错误码

```plain
1000 解析错误
1001 证书验证失败
1002 连接失败
1003 连接超时
1004 主机地址未知
1005 服务器返回数据错误
1006 未知错误
```
## 类说明

### SmoothHttp(框架调用类)

```plain
init                    ：定义请求根目录
getBaseUrl              ：获取请求根目录
globalConfig            ：获取请求配置
get                     ：get请求
post                    ：post请求
download                ：下载文件请求
upload                  ：上传文件请求
```

### HttpGlobalConfig(请求配置类 配置全局请求参数)

```plain
sslFactory              ：添加ssl证书
hostVerifier            ：添加主机域名验证
defaultValue            ：对空类型设置默认值
hosts                   ：添加安全认证的域名或根路径
addGlobalHeader         ：添加全局请求头
globalHeader            ：更换请求头
addGlobalParam          ：添加全局请求参数
globalParams            ：更换请求参数
okHttpClient            ：创建okHttpClient
retryDelayMillis        : 添加请求失败重试间隔时间
retryCount              ：添加请求失败重试次数
addInterceptor          ：添加拦截器
addCallAdapterFactory   ：添加Call适配器工厂
converterFactory        ：添加转换工厂,默认为  GsonConverterFactory
readTimeout             ：添加读取超时时间 默认 10 秒
writeTimeout            ：添加写入超时时间 默认 10 秒
connectTimeout          ：设置连接超时时间 默认 10 秒
timeUnit                ：设置超时时间单位 默认秒
trustAllHost            ：配置是否允许所有 host
debug                   ：开启debug模式
```


### BaseRequest(请求基类 可配置每个请求的参数)

```plain
addHeaders  addHeader resetHeader   ：添加单个、多个请求头
removeHeader  cleanHeader           ：移除单个、多个请求头
addParams  addParam resetParam      ：添加单个、多个请求参数
removeParam  cleanParams            ：移除单个、多个请求参数
interceptor                         ：添加拦截器
clearInterceptor                    ：删除拦截器
readTimeout                         ：设置读取超时时间
writeTimeout                        ：设置写入超时时间
connectTimeout                      ：设置连接超时时间
autoConvert                         ：开启关闭自动解析 关闭时返回String源数据
json                                ：添加模拟数据
convert                             ：添加自定义解析器
```

项目根目录：build.gradle 添加以下代码

```groovy
buildscript {
    repositories {
        google()
        jcenter()
        maven{
            url 'http://192.168.126.254:8081/repository/maven-releases/'
        }// 添加maven库地址
    }
    dependencies {
    classpath "com.android.tools.build:gradle:4.1.1"
    }
}
allprojects {
    repositories {
        google()
        jcenter()
        maven{
            url 'http://192.168.126.254:8081/repository/maven-releases/'
        }// 添加maven库地址
    }
}
```
然后项目 module 中启用插件，可以是`application`也可以是`library`
添加类库依赖：

```groovy
dependencies {
implementation 'com.sesxh.android-smoothhttp:smoothhttp:1.0.1'
}
```


