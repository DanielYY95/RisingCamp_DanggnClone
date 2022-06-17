package com.example.demo.src.keyword;

import com.example.demo.src.keyword.model.DeleteKeywordSetReq;
import com.example.demo.src.keyword.model.GetKeywordAlarmsRes;
import com.example.demo.src.keyword.model.GetKeywordSetRes;
import com.example.demo.src.keyword.model.PostKeywordSetReq;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@Mapper
public interface KeywordDao {

    @Select("select count(*) cnt, memberStatus from Member where memberId = #{memberId}")
    public HashMap<String, String> checkMemberExists(Integer memberId);


    @Select("select count(*) cnt from Town where townId = #{townId}")
    public int checkTownExists(Integer townId);


    @Select("select ks.keywordSetId keywordSetId, ks.keywordId keywordId, k.name name \n" +
            "from Keywordset ks\n" +
            "join Keyword k\n" +
            "    on ks.keywordId = k.keywordId\n" +
            "where ks.memberId = #{memberId} and ks.status=1 order by updatedAt desc \n")
    public List<GetKeywordSetRes> getKeywordSet(Integer memberId);


    @Select("select keywordAlarmId, k.postId postId, ifnull(pi.path, '') imgPath,\n" +
            "       keyword, name, title,\n" +
            "          case \n" +
            "                when TIMESTAMPDIFF(SECOND, i.createdAt,CURRENT_TIMESTAMP) < 60 \n" +
            "                    then concat(TIMESTAMPDIFF(SECOND , i.createdAt,CURRENT_TIMESTAMP), '초 전') \n" +
            "                when TIMESTAMPDIFF(MINUTE , i.createdAt,CURRENT_TIMESTAMP) < 60 \n" +
            "                    then concat(TIMESTAMPDIFF(MINUTE , i.createdAt,CURRENT_TIMESTAMP), '분 전') \n" +
            "                when TIMESTAMPDIFF(HOUR, i.createdAt,CURRENT_TIMESTAMP) < 24 \n" +
            "                    then concat(TIMESTAMPDIFF(HOUR, i.createdAt,CURRENT_TIMESTAMP), '시간 전') \n" +
            "                when TIMESTAMPDIFF(HOUR, i.createdAt,CURRENT_TIMESTAMP) < 48 \n" +
            "                    then '하루 전' \n" +
            "                when TIMESTAMPDIFF(DAY, i.createdAt,CURRENT_TIMESTAMP) < 30 \n" +
            "                    then concat(TIMESTAMPDIFF(DAY, i.createdAt,CURRENT_TIMESTAMP), '일 전') \n" +
            "                when TIMESTAMPDIFF(MONTH , i.createdAt,CURRENT_TIMESTAMP) < 12 \n" +
            "                    then concat(TIMESTAMPDIFF(MONTH , i.createdAt,CURRENT_TIMESTAMP), '달 전') \n" +
            "                else concat(TIMESTAMPDIFF(YEAR, i.createdAt,CURRENT_TIMESTAMP), '년 전') \n" +
            "          end createdAt\n" +
            "from (select * from Keywordalarm where memberId = #{memberId}) k\n" +
            "join Itempost i\n" +
            "    on k.postId = i.itemPostId\n" +
            "left outer join (select * from Postimg where\n" +
            "     postTable = 'Itempost' and `order` = 1\n" +
            "   ) pi\n" +
            "on i.itemPostId = pi.postId\n" +
            "join Town t\n" +
            "on i.townId = t.townId\n" +
            "order by i.createdAt DESC;\n" +
            "\n")
    public List<GetKeywordAlarmsRes> getKeywordAlarms(Integer memberId);


    @Update("update Keywordset Set status = 2 where keywordSetId = #{keywordSetId} and memberId = #{memberId}")
    public int deleteKeywordSet(DeleteKeywordSetReq req);


    @Update("update Keywordset Set status = 1 where keywordSetId = #{keywordSetId}")
    public int restoreKeywordSet(Integer keywordSetId);

    @Insert("insert into Keyword(memberId, name) values(#{memberId}, #{name})")
    public int insertKeyword(PostKeywordSetReq req);

    @Insert("insert into Keywordset(keywordId, memberId, town1Id, town2Id) values(#{keywordId}, #{req.memberId}, #{req.town1Id}, #{req.town2Id})")
    public int insertKeywordSet(@Param("keywordId") Integer keywordId, @Param("req") PostKeywordSetReq req);

    @Select("select keywordId from Keyword where name = #{name}")
    public Integer getKeywordId(String name);

    @Select("select keywordSetId from Keywordset where memberId = #{memberId} and keywordId = #{keywordId}")
    public Integer getKeywordSetId(@Param("memberId") Integer memberId, @Param("keywordId") Integer keywordId);


}
