package com.leo.elib;


import com.leo.elib.entity.TmpBookCover;
import com.leo.elib.mapper.BookInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class BookCoverPathSetter {

  @Resource
  private BookInfoMapper bookInfoMapper;

  @Test
  void setBookCoverPath(){

    String baseDir = "elib/book_cover/pics/";

    List<TmpBookCover> tmpBookCovers = bookInfoMapper.debug_getTmpBookCover(0,10000);

    // 将书籍isbn与对应path备份至txt
//    try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:/projects/ElibServer/static_resource/elib/book_cover/book_cover_path_exlinks.txt"))) {
//      for (TmpBookCover tmpBookCover : tmpBookCovers) {
//        writer.write(tmpBookCover.getIsbn());
//        writer.newLine();
//        writer.write(tmpBookCover.getCoverUrl());
//        writer.newLine();
//      }
//    } catch (IOException e) {
//      System.err.println("写入文件失败: " + e.getMessage());
//    }
    for (TmpBookCover tmpBookCover : tmpBookCovers) {
      String isbn = tmpBookCover.getIsbn();
      String extention = tmpBookCover.getCoverUrl().substring(tmpBookCover.getCoverUrl().lastIndexOf("."));
      String fold1 = isbn.substring(0,2);
      String fold2 = isbn.substring(2,4);
      String fold3 = isbn.substring(4,6);
      String finalPath = baseDir + fold1 + "/" + fold2 + "/" + fold3 + "/" + isbn + extention;
      String shortDesc = "这是一段书籍的简要描述，这是一段自拟的书籍描述，编号"+isbn;
      bookInfoMapper.debug_setTmpBookCoverAndShortDesc(isbn,finalPath,shortDesc);
    }
  }
}