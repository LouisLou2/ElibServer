package com.leo.elib.preset;

import com.leo.elib.entity.BookTag;
import com.leo.elib.service.inter.RCacheManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookTagSetter {
  @Value("${container.redis.book_tag_hash_name}")
  private String bookTagNameHashCont;
  @Value("${container.redis.book_tag_hash_whole}")
  private String bookTagWholeHashCont;
  @Resource
  private RCacheManager rCacheManager;
  private HashOperations<String, String, Object> opsForHash;
  private static List<BookTag> BookTags;
  static {
    BookTags = new ArrayList<>();
    BookTags.add(new BookTag((short) 1, "Mystery", "Books that involve solving a mystery or crime"));
    BookTags.add(new BookTag((short) 2, "Fantasy", "Stories with magical or supernatural elements"));
    BookTags.add(new BookTag((short) 3, "Historical Fiction", "Novels set in a specific historical time period"));
    BookTags.add(new BookTag((short) 4, "Romance", "Books that focus on romantic relationships"));
    BookTags.add(new BookTag((short) 5, "Science Fiction", "Books based on speculative scientific concepts"));
    BookTags.add(new BookTag((short) 6, "Biography", "Books that tell the life story of a person"));
    BookTags.add(new BookTag((short) 7, "Self-Help", "Books aimed at personal improvement"));
    BookTags.add(new BookTag((short) 8, "Adventure", "Books with exciting and risk-taking journeys"));
    BookTags.add(new BookTag((short) 9, "Thriller", "Fast-paced novels that are full of suspense"));
    BookTags.add(new BookTag((short) 10, "Psychology", "Books exploring mental health and human behavior"));
    BookTags.add(new BookTag((short) 11, "Philosophy", "Books on theoretical and fundamental questions of life"));
    BookTags.add(new BookTag((short) 12, "Young Adult", "Books targeted at young adult readers"));
    BookTags.add(new BookTag((short) 13, "Horror", "Books that aim to scare or thrill the reader"));
    BookTags.add(new BookTag((short) 14, "Cookbook", "Books with recipes and cooking techniques"));
    BookTags.add(new BookTag((short) 15, "Memoir", "Nonfiction books focused on personal life experiences"));
    BookTags.add(new BookTag((short) 16, "Graphic Novel", "Books in comic format but with complex storytelling"));
    BookTags.add(new BookTag((short) 17, "Travel", "Books that document or inspire travel and exploration"));
    BookTags.add(new BookTag((short) 18, "Dystopian", "Books set in an imagined, often oppressive future"));
    BookTags.add(new BookTag((short) 19, "Poetry", "Books containing collections of poems"));
    BookTags.add(new BookTag((short) 20, "Political", "Books discussing political theories or real-world politics"));
    BookTags.add(new BookTag((short) 21, "Spirituality", "Books discussing spirituality and personal beliefs"));
    BookTags.add(new BookTag((short) 22, "Humor", "Books intended to amuse and entertain"));
    BookTags.add(new BookTag((short) 23, "Essay Collection", "Books consisting of a series of essays"));
    BookTags.add(new BookTag((short) 24, "Anthology", "Books that collect works from various authors"));
    BookTags.add(new BookTag((short) 25, "Health", "Books focused on health and wellness topics"));
    BookTags.add(new BookTag((short) 26, "Motivation", "Books that aim to inspire and motivate readers"));
    BookTags.add(new BookTag((short) 27, "Nature", "Books exploring the natural world and ecosystems"));
    BookTags.add(new BookTag((short) 28, "Mythology", "Books based on myths and folklore"));
    BookTags.add(new BookTag((short) 29, "Short Stories", "Books containing multiple short stories"));
    BookTags.add(new BookTag((short) 30, "Satire", "Books that use humor to criticize or make a point"));
    BookTags.add(new BookTag((short) 31, "Art", "Books focused on art and visual culture"));
    BookTags.add(new BookTag((short) 32, "Science", "Books discussing scientific topics and discoveries"));
    BookTags.add(new BookTag((short) 33, "Parenting", "Books providing advice and guidance for parents"));
    BookTags.add(new BookTag((short) 34, "Education", "Books focused on educational theory or practice"));
    BookTags.add(new BookTag((short) 35, "Music", "Books that explore music, musicians, and genres"));
    BookTags.add(new BookTag((short) 36, "Personal Finance", "Books focused on managing money and finances"));
    BookTags.add(new BookTag((short) 37, "Technology", "Books exploring advancements in technology"));
    BookTags.add(new BookTag((short) 38, "Drama", "Books with an intense and emotional plotline"));
    BookTags.add(new BookTag((short) 39, "True Crime", "Books recounting actual criminal events"));
    BookTags.add(new BookTag((short) 40, "Environmental", "Books discussing environmental issues and solutions"));
    BookTags.add(new BookTag((short) 41, "Cultural Studies", "Books that explore cultural analysis and theory"));
    BookTags.add(new BookTag((short) 42, "Religion", "Books discussing religious beliefs and practices"));
    BookTags.add(new BookTag((short) 43, "Legal", "Books focused on law and legal matters"));
    BookTags.add(new BookTag((short) 44, "Food & Drink", "Books about cooking, food, and beverages"));
    BookTags.add(new BookTag((short) 45, "Fantasy Romance", "Books combining elements of fantasy and romance"));
  }
  @PostConstruct
  void init() {
    opsForHash = rCacheManager.getOpsForHash();
  }
  public void setBookTagsForMySql() {
    // mysql连接
    // 数据库连接配置
    String jdbcUrl = "jdbc:mysql://localhost:3306/lib_manage";
    String username = "leo";
    String password = "lou..200499";
    //SQL
    String sql = "INSERT INTO book_tag (tag_id, tag_name, tag_desc) VALUES (?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
         PreparedStatement ps = conn.prepareStatement(sql)) {
      for (int i = 0; i< BookTags.size();++i) {
        BookTag bookTag = BookTags.get(i);
        ps.setShort(1, bookTag.getTagId());
        ps.setString(2, bookTag.getTagName());
        ps.setString(3, bookTag.getTagDesc());
        ps.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  public void setBookTagsForRedis() {
    for (BookTag bookTag : BookTags) {
      opsForHash.put(bookTagNameHashCont, String.valueOf(bookTag.getTagId()), bookTag.getTagName());
      opsForHash.put(bookTagWholeHashCont, String.valueOf(bookTag.getTagId()), bookTag);
    }
  }
}
