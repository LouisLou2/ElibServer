//package com.leo.elib;
//
//import com.leo.elib.entity.TmpBookCover;
//import com.leo.elib.mapper.BookInfoMapper;
//import jakarta.annotation.Resource;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@SpringBootTest
//public class BookCoverDownload {
//  @Resource
//  private BookInfoMapper bookInfoMapper;
//
//  private static void downloadImage(String imageUrl, String outputPath) throws IOException{
//    // 打开连接
//    URL url = new URL(imageUrl);
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//    connection.setRequestMethod("GET");
//
//    // 检查响应代码
//    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//      throw new IOException("HTTP request failed with response code " + connection.getResponseCode());
//    }
//
//    // 从输入流中读取数据并写入文件
//    try (InputStream inputStream = connection.getInputStream();
//         FileOutputStream outputStream = new FileOutputStream(outputPath)) {
//
//      byte[] buffer = new byte[4096];
//      int bytesRead;
//
//      while ((bytesRead = inputStream.read(buffer)) != -1) {
//        outputStream.write(buffer, 0, bytesRead);
//      }
//    } finally {
//      connection.disconnect();
//    }
//  }
//
//  @Test
//  void download() throws IOException {
//    final String resourcePath = "D:/resources/project_resource/elib_pic";
//    final String txtPath = resourcePath + "/book_cover.txt";
//    final String imgPath = resourcePath + "/book_cover";
//    final String imgNoCover= resourcePath + "/no_cover.png";
//
//    List<TmpBookCover> tmpBookCovers = bookInfoMapper.debug_getTmpBookCover(0, 10000);
//    int count = 0;
//    for (TmpBookCover tmpBookCover : tmpBookCovers) {
//      String isbn = tmpBookCover.getIsbn();
//      String coverUrl = tmpBookCover.getCoverUrl();
//      String fext = coverUrl.substring(coverUrl.lastIndexOf('.'));
//      assert fext.length() > 1;
//      String coverPath = imgPath + "/" + isbn + fext;
//      try {
//        downloadImage(coverUrl, coverPath);
//      } catch (IOException e) {
//        // 删除下载失败的文件
//        Path target = Paths.get(coverPath);
//        Files.deleteIfExists(target);
//        // 复制no_cover.png
//        Files.copy(Paths.get(imgNoCover), target);
//        System.err.println("下载封面失败, ISBN: " + isbn + ", URL: " + coverUrl);
//        System.err.println("已使用默认封面");
//      }
//      // 记录下载成功的封面
//      tmpBookCover.setCoverUrl(coverPath);
//      ++count;
//      if (count % 100 == 0) {
//        System.out.println("已下载" + count + "个封面");
//      }
//    }
//    System.out.println("下载完成");
//    System.out.println("共下载" + count + "个封面");
//    // 分行写入文件
//    // 打开文件
//    // 创建BufferedWriter，使用FileWriter打开文件
//    try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtPath))) {
//      for (TmpBookCover tmpBookCover : tmpBookCovers) {
//        writer.write(tmpBookCover.getIsbn());
//        writer.newLine();
//        writer.write(tmpBookCover.getCoverUrl());
//        writer.newLine();
//      }
//    } catch (IOException e) {
//      System.err.println("写入文件失败: " + e.getMessage());
//    }
//  }
//}
package com.leo.elib;

import com.leo.elib.entity.TmpBookCover;
import com.leo.elib.mapper.BookInfoMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class BookCoverDownload {

  @Resource
  private BookInfoMapper bookInfoMapper;

  // 设置固定的代理
  private static final String PROXY_HOST = "127.0.0.1";
  private static final int PROXY_PORT = 7897;

  public static void downloadImage(String imageUrl, String outputPath) throws IOException {
    // 创建代理
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));

    // 打开连接并使用代理
    URL url = new URL(imageUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy); // 使用代理连接
    connection.setRequestMethod("GET");

    // 检查响应代码
    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new IOException("HTTP request failed with response code " + connection.getResponseCode());
    }

    // 从输入流中读取数据并写入文件
    try (InputStream inputStream = connection.getInputStream();
         FileOutputStream outputStream = new FileOutputStream(outputPath)) {

      byte[] buffer = new byte[4096];
      int bytesRead;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } finally {
      connection.disconnect();
    }
  }

  // 下载封面图片的任务类
  private static class DownloadTask implements Callable<Void> {
    private final TmpBookCover tmpBookCover;
    private final String imgPath;
    private final String imgNoCover;

    public DownloadTask(TmpBookCover tmpBookCover, String imgPath, String imgNoCover) {
      this.tmpBookCover = tmpBookCover;
      this.imgPath = imgPath;
      this.imgNoCover = imgNoCover;
    }

    @Override
    public Void call() throws Exception {
      String isbn = tmpBookCover.getIsbn();
      String coverUrl = tmpBookCover.getCoverUrl();
      String fext = coverUrl.substring(coverUrl.lastIndexOf('.'));
      String coverPath = imgPath + "/" + isbn + fext;
      try {
        downloadImage(coverUrl, coverPath);
      } catch (IOException e) {
        // 下载失败，处理默认封面
        Path target = Paths.get(coverPath);
        Files.deleteIfExists(target);
        Files.copy(Paths.get(imgNoCover), target);
        System.err.println("下载封面失败, ISBN: " + isbn + ", URL: " + coverUrl);
        System.err.println("已使用默认封面");
      }
      // 更新封面路径
      tmpBookCover.setCoverUrl(coverPath);
      return null;
    }
  }

  @Test
  void download() throws IOException, InterruptedException, ExecutionException {
    final String resourcePath = "D:/resources/project_resource/elib_pic";
    final String txtPath = resourcePath + "/book_cover.txt";
    final String imgPath = resourcePath + "/book_cover";
    final String imgNoCover = resourcePath + "/no_cover.png";

    List<TmpBookCover> tmpBookCovers = bookInfoMapper.debug_getTmpBookCover(0, 10000);
    int count = 0;

    // 创建一个线程池，最大并发数为 30（可以根据实际情况调整）
    ExecutorService executorService = Executors.newFixedThreadPool(80);

    // 创建一个任务列表
    List<Callable<Void>> tasks = new ArrayList<>();
    for (TmpBookCover tmpBookCover : tmpBookCovers) {
      tasks.add(new DownloadTask(tmpBookCover, imgPath, imgNoCover));
    }

    // 提交所有下载任务
    List<Future<Void>> futures = executorService.invokeAll(tasks);

    // 等待所有任务完成
    for (Future<Void> future : futures) {
      future.get();  // 阻塞直到任务完成
    }

    // 记录下载成功的封面
    System.out.println("下载完成");
    System.out.println("共下载" + tmpBookCovers.size() + "个封面");

    // 分行写入文件
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtPath))) {
      for (TmpBookCover tmpBookCover : tmpBookCovers) {
        writer.write(tmpBookCover.getIsbn());
        writer.newLine();
        writer.write(tmpBookCover.getCoverUrl());
        writer.newLine();
      }
    } catch (IOException e) {
      System.err.println("写入文件失败: " + e.getMessage());
    } finally {
      // 关闭线程池
      executorService.shutdown();
    }
  }
}