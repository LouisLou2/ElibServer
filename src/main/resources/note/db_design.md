### 数据库Category的相关设计
CategoryId是以1000为单位递增的，SubCategoryId是以1为单位递增的。例如1001表示“历史”(1000)的第一个子分类“世界近代”(1001)
所以一级分类和二级分类是不会重复的，其实没必要再增加一个level字段。并且仅从千位数字即可知道它属于哪一个一级分类，
但是subcategory_map表里还是加了category字段, 表示它从属于的一级分类，主要是为了数据的完整性。比如防止增加一个没有一级分类的二级分类。

但是在其他操作中，可以大胆利用这一特性，提升效率