package com.leo.elib.service.specific.impl;

import com.leo.elib.mapper.BookInfoMapper;
import com.leo.elib.service.specific.inter.RecoBookProvider;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecoBookProviderImpl implements RecoBookProvider {

  @Resource
  BookInfoMapper bookInfoMapper;


  static private List<String> fakeRecoList = List.of(
      "000100039X",
      "000649885X",
      "000675399X",
      "000710653X",
      "000711933X",
      "000712774X",
      "000720230X",
      "000721975X",
      "000723368X",
      "000735049X",
      "000741949X",
      "000748805X",
      "006000942X",
      "006001234X",
      "006053124X",
      "006054094X",
      "006056251X",
      "006056668X",
      "006057299X",
      "006074457X",
      "006074815X",
      "006075995X",
      "006078721X",
      "006079156X",
      "006082543X",
      "006083577X",
      "006083756X",
      "006091307X",
      "006093140X",
      "006093316X",
      "006095955X",
      "006097625X",
      "006102001X",
      "006112429X",
      "006112527X",
      "006114097X",
      "006117758X",
      "006121468X",
      "006121471X",
      "006124189X",
      "006124242X",
      "006128887X",
      "006135323X",
      "006137587X",
      "006147312X",
      "006156608X",
      "006170654X",
      "006172081X",
      "006174266X",
      "006177894X",
      "006178320X",
      "006194436X",
      "006195795X",
      "006197806X",
      "006202468X",
      "006218850X",
      "006221635X",
      "006222543X",
      "006223742X",
      "006228567X",
      "006232005X",
      "006232022X",
      "006232540X",
      "006233638X",
      "006245773X",
      "006440174X",
      "006440630X",
      "006441034X",
      "006443253X",
      "006621131X",
      "009947137X",
      "009949857X",
      "009959241X",
      "014006690X",
      "014013168X",
      "014017821X",
      "014018998X",
      "014023313X",
      "014023828X",
      "014028009X",
      "014029628X",
      "014034893X",
      "014038572X",
      "014043478X",
      "014043769X",
      "014044176X",
      "014044789X",
      "014044906X",
      "014044923X",
      "014100018X",
      "014101587X",
      "014118616X",
      "014132063X",
      "014132502X",
      "014144116X",
      "014200068X",
      "014200202X",
      "014240120X",
      "014240165X",
      "014241493X"
  );
  // TODO: 还没有实现推荐功能
  @Override
  public List<String> getRecoBookIsbns(int userId, int offset, int num) {
    assert offset >= 0 && num > 0;
//    if (offset >= fakeRecoList.size() || offset + num > fakeRecoList.size()) {
//      return List.of();
//    }
//    return fakeRecoList.subList(offset, offset + num);
    return bookInfoMapper.dev_getIsbn(offset, num);
  }
}
