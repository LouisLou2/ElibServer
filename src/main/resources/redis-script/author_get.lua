local hashName = KEYS[1] -- hash容器名字
local zsetName = KEYS[2] -- zset容器名字
local key = KEYS[3] -- id

local keyAuthor = KEYS[4] -- value的author字段名字
local keyBooks = KEYS[5] -- value的books字段名字

-- 检查zset中是否存在
local exist = redis.call('ZSCORE', zsetName, key)

-- 如果不存在，返回空
if not exist then
return nil
end

local author = redis.call('HGET', hashName, key..keyAuthor)

local books = redis.call('HGET', hashName, key..keyBooks)

-- 增加zset中的分数
redis.call('ZINCRBY', zsetName, 1, key)

return {author, books}