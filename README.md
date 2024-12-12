



## 运行前配置Windows

### 需要安装的服务

- MySQL
- Redis
- ElasticSearch

### MySQL数据库配置

数据库的dump文件位于`src/main/resources/dump/mysql/_localhost--dump.sql`. 请先创建数据库，然后导入dump文件。(这个步骤略)

### Redis配置

Redis配置位于`src/main/resources/dump/redis/dump.rdb`
Redis数据导入
Redis提供了RESTORE命令来导入数据。在Windows环境下，我们需要先停止Redis服务器.
以下是官方文档的方法：

Restore an RDB file
If you have an RDB file dump.rdb that contains the data you want you can use this file to create a new database

1. Copy the dump.rdb file into the Redis working directory
   If you do not know what it is folder you can run the command `CONFIG get dir` where your Redis instance is up and running
- redis的windows版本中，redis.windows.conf可以找到dir配置:
  
  ```bash
    # The working directory.
    #
    # The DB will be written inside this directory, with the filename specified
    # above using the 'dbfilename' configuration directive.
    # 
    # The Append Only File will also be created inside this directory.
    # 
    # Note that you must specify a directory here, not a file name.
    dir ./
  ```
2. Start the Redis service with the redis-server
3. The file dump.rdb is automatically imported.
4. Connect to the database using redis-cli or any other client, to check that data have been imported. (for example SCAN)

### ElasticSearch配置

#### 记录用户和密码

es启动会默认建立一个用户，用户名是elastic，密码是随机的，在安装过程中会在控制台输出，记录一下

#### 使用http

安装后默认使用localhost:9200
服务启动会默认使用https，为了数据导入方便，将其先改为http
在`~\elasticsearch\config\elasticsearch.yml`中添加
修改以下：

```yml
xpack.security.enabled: false #!!!关闭安全认证
# Enable encryption for HTTP API client connections, such as Kibana, Logstash, and Agents
xpack.security.http.ssl:
  enabled: false #!!!关闭https
```

运行`"~\elasticsearch\bin\elasticsearch.bat"`配置生效

#### 使用elasticsearch-dupm导入数据

##### 需要node.js环境

##### 安装elasticsearch-dump直接见[官方文档](https://github.com/elasticsearch-dump/elasticsearch-dump)

##### 已经dump好的文件在项目文件夹的`src/main/resources/dump/es`中

##### 我这里的示例：

```bash
> node C:/Users/LSK/node_modules/elasticdump/bin/multielasticdump --direction=load --match='^.*$' --output=http://127.0.0.1:9200 --input=D:/SourceCode/elib/src/main/resources/dump/es
# C:/Users/LSK/node_modules/elasticdump/bin/multielasticdump 是我的elasticsearch-dump安装后的可执行文件
```

### application.yml 更改

- `spring.datasource`是MySQL的配置,改为你的数据库配置

- `spring.redis`是Redis的配置,改为你的Redis配置，注意`spring.redis.database`是数据库的索引,redis默认有16个数据库，索引从0开始，填你导入的数据库索引

- `spring.mail`是邮箱配置，可以自己开启自己邮箱的smtp服务，填入自己的邮箱配置。

- es的配置
  
  ```yml
  es:
    host: 127.0.0.1 # 111.230.63.174
    port: 9200
    https:
      enabled: false # 这里你前面如果一直保持http没有在导入数据之后改成https，这里就false,否则true
      insecure: true # 一定要设置为true
    username: elastic
    password: # 你的密码
  ```

## 关于运行App的环境配置

### 安装Flutter

注意不要看Flutter英文网站的安装文档

请参考[安装和环境配置 | Flutter 中文文档 - Flutter 中文开发者网站 - Flutter](https://docs.flutter.cn/get-started/install)

网站会让你选择，你的开发环境以及倾向于开发什么系统的软件（请选择Android）

之后请按照文档进行。

仅仅安装Flutter是不能将软件运行的，每个平台的软件都需要各自平台的SDK，例如Android SDK，以上的文档也包括这部分的配置。

配置完请看，是一些配置镜像的工作 :[在中国网络环境下使用 Flutter | Flutter 中文文档 - Flutter 中文开发者网站 - Flutter](https://docs.flutter.cn/community/china/)

#### Tips

- 你不需要安装Dart SDK, Flutter会内嵌一个

- 请安装flutter 3.24, 这也是上面文档默认的（最新版）

- 注意flutter和内部的dart都需要注册环境变量，dart在flutter内部的路径是`D:\Tools\dev_toolset\flutter\bin\cache\dart-sdk\bin`, 注册到Path

### 使用Andriod Studio运行项目

*vscode*也可以，但是我没有在上面用过，下面只介绍AS的

Android Studio需要安装Flutter插件(Dart插件也会连带着安装)

#### 导入

之后使用Andriod Studio打开项目，会提示specify flutter安装路径。

#### 下载依赖

打开项目的pubspec.yaml, 右上角有pub get按钮，点击即可, 或者在项目根目录运行

`flutter pub get`

#### 生成一些预生成文件

在项目根目录运行`dart run build_runner build`, 会在/generated文件下生成很多后缀是`.g.dart` 文件，这是必须的



#### 更改记录的ip

`/lib/config/network_config.dart`改写其中内容为你的后端地址

```dart
class Configs{
  static const String HOST = ${自己的后端地址}
   // 忽略这个websocket,不用管
  // static const String WEBSOCKET_URL = 'ws://192.168.24.144:5000';
  static const int CONNECT_TIMEOUT = 4000;//ms
}
```

#### Andriod构建工具设置

如果你是从头写一个flutter,andriod文件夹中的`android/gradle/wrapper/gradle-wrapper.properties`会有一个gradle的下载地址，也就是第一次运行项目会新下一个gradle.
你也可以使用自己本地本就有的。
将`android/gradle/wrapper/gradle-wrapper.properties`中：

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/di
# 可以将原来的url改成本地的Url(file协议)，这样就不会下载新的了
#distributionUrl=https/://mirrors.cloud.tencent.com/gradle/gradle-8.3-all.zip
distributionUrl=file\:/D:/Tools/BuildTools/gradle/archives/gradle-8.11-bin.zip


```

#### 运行起来这个项目

##### 使用虚拟设备

你可以选择虚拟设备调试。如果内存够可以使用虚拟设备。
记忆步骤andriod studio官方和网上都有很详细教程，这里不说了

##### 使用自己的手机

无线调试和USB 调试需要用自己的手机，需要**开启开发者选项**（手机设置的隐秘角落）
不用手机品牌开启的方法不同，请自己找，进入之后，无线调试就允许无线调试。
USB调试就允许USB调试，安装应用等。具体不同手机品牌内部选项不同



#### 可能会遇到错误

大部分错误因为网络，命令行运行`flutter doctor`可以检测是否自己的环境不对
项目根目录运行`flutter run -v` 可以看到运行项目所有的内部工作，可以帮助定位。

## 配置静态资源服务器

**注意后端代码更新了，mysql, redis,es的dump文件已经改了，请重新clone并导入数据库新数据**

后端文件夹有一个`static_resource`文件夹，里面是一些封面之类的静态文件，请使用nginx作为服务器。
我的配置参考：


主要就是server.listen和server.location.root要更改

```properties
server {
        listen 192.168.127.252:9022; # 注意ip改成你的局域网主机ip和你需要的端口

        server_name resource_host;

        location / {
            root D:/projects/ElibServer/static_resource; #这里写入static_resource在你电脑上的绝对路径
            autoindex on;
            
            # 缓存静态文件（图片）30天
            expires 30d;  # 设置缓存过期时间为30天
            add_header Cache-Control "public, max-age=2592000";  # 设置缓存头，30天的秒数
            
            # 你也可以使用 Cache-Control 来控制其他行为
            # add_header Cache-Control "public, max-age=2592000, immutable";  # 不可变的静态文件
        }
    }
```

运行nginx访问尝试是否成功


##### 后端springboot记录这个地址

`D:\projects\ElibServer\src\main\java\com\leo\elib\config\ServiceNetConfig.java`更改

```java
static private final String ResourceServerAddress = "http://ip:port/";
```
