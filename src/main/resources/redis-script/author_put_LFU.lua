local hashName = KEYS[1] -- hash容器名字
local zsetName = KEYS[2] -- zset容器名字
local key = KEYS[3] -- id
local keyAuthor = KEYS[4] -- value的author字段名字
local keyBooks = KEYS[5] -- value的books字段名字

local maxCapacity = ARGV[1] -- 最大容量
local authorVal = ARGV[2] -- author的值
local booksVal = ARGV[3] -- books的值

-- 如果容器已经满了
if redis.call('ZCARD', zsetName) >= tonumber(maxCapacity) then
    -- 找到最不常用的元素
    local minScoreId = redis.call('ZRANGE', zsetName, 0, 0)[1]
    -- 删除最不常用的元素
    redis.call('ZREM', zsetName, minScoreId)
    -- 删除hash中对应的元素
    redis.call('HDEL', hashName, minScoreId..keyAuthor)
    redis.call('HDEL', hashName, minScoreId..keyBooks)
end

-- 将新元素存入hash
redis.call('HSET', hashName, key..keyAuthor, authorVal)
redis.call('HSET', hashName, key..keyBooks, booksVal)

-- 将新元素存入zset
redis.call('ZADD', zsetName, 0, key)

return 1