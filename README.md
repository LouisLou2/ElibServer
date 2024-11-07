# 后端运行前配置Windows

## 需要安装的服务
- MySQL
- Redis
- ElasticSearch

## MySQL数据库配置
数据库的dump文件位于`src/main/resources/dump/mysql/_localhost--dump.sql`. 请先创建数据库，然后导入dump文件。(这个步骤略)

## Redis配置
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

## ElasticSearch配置
### 记录用户和密码
es启动会默认建立一个用户，用户名是elastic，密码是随机的，在安装过程中会在控制台输出，记录一下
### 使用http
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

### 使用elasticsearch-dupm导入数据
#### 需要node.js环境
#### 安装elasticsearch-dump直接见[官方文档](https://github.com/elasticsearch-dump/elasticsearch-dump)
#### 已经dump好的文件在项目文件夹的`src/main/resources/dump/es`中
#### 我这里的示例：
```bash
> node C:/Users/LSK/node_modules/elasticdump/bin/multielasticdump --direction=load --match='^.*$' --output=http://127.0.0.1:9200 --input=D:/SourceCode/elib/src/main/resources/dump/es
# C:/Users/LSK/node_modules/elasticdump/bin/multielasticdump 是我的elasticsearch-dump安装后的可执行文件
```

## application.yml 更改
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
