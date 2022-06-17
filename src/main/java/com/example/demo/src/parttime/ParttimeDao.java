package com.example.demo.src.parttime;


import com.example.demo.src.itemPost.model.GetItemPostRes_imgs;
import com.example.demo.src.member.model.PostMemberReq;
import com.example.demo.src.parttime.model.GetParttimePostReq;
import com.example.demo.src.parttime.model.GetParttimePostRes;
import com.example.demo.src.parttime.model.GetParttimePostRes_imgs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParttimeDao {



    @Select("select `order` imgOrder, path  from Postimg\n" +
            "where postTable = 'Parttime'\n" +
            "and postId = #{parttimeId}")
    public List<GetParttimePostRes_imgs> getParttimeImgs(Integer parttimeId);



    @Select("select pt.partTimeId parttimeId, title, companyName, pt.ownerId ownerId, case when pt.createdAt = pt.pullTime then ''\n" +
            "            else '끌올 '\n" +
            "            end pullCnt,  case\n" +
            "        when TIMESTAMPDIFF(SECOND, pt.pullTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , pt.pullTime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , pt.pullTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , pt.pullTime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, pt.pullTime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, pt.pullTime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, pt.pullTime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, pt.pullTime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, pt.pullTime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , pt.pullTime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , pt.pullTime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, pt.pullTime,CURRENT_TIMESTAMP), '년 전')\n" +
            "    end createdAt, wageMethod, wage, day, time,\n" +
            "            content, location, viewCnt, ifnull(a.aCnt,'0') applyCnt,\n" +
            "    ifnull(b.bCnt,'0') interestCnt, phone, if(icnt.cnt<>1, 'false', 'true') isInterest,\n" +
            "            if(apcnt.cnt<>1, 'false', 'true') isApplied\n" +
            "    from Parttime pt\n" +
            "    left outer join (select pa.partTimeId, count(pa.partTimeApplyId) aCnt from Parttimeapply pa group by pa.partTimeId ) a\n" +
            "    on pt.partTimeId =  a.partTimeId\n" +
            "    left outer join (select it.postId, count(it.interestId) bCnt from Interest it where it.interestType = '당근알바' group by it.postId ) b\n" +
            "    on pt.partTimeId =  b.postId\n" +
            "    left outer join (select i.postId, count(*) cnt from Interest i\n" +
            "    where i.memberId = #{memberId} and i.interestType = '당근알바' and i.postId = #{parttimeId}) icnt\n" +
            "    on pt.partTimeId = icnt.postId\n" +
            "    left outer join (select pta.partTimeId, count(*) cnt from Parttimeapply pta\n" +
            "    where pta.applicantId = #{memberId} and pta.partTimeId = #{parttimeId}) apcnt\n" +
            "    on pt.partTimeId = apcnt.partTimeId\n" +
            "    where pt.partTimeId = #{parttimeId}")
    public GetParttimePostRes getParttimePost(GetParttimePostReq req);

    // 회원고유번호 유효한지
    @Select("select count(*) from Member where memberId = #{memberId}")
    public int checkValidMember(GetParttimePostReq req);
    
    
    // 알바 게시물 고유번호 유효한지
    @Select("select count(*) from Parttime where partTimeId = #{parttimeId}")
    public int checkValidParttimePost(GetParttimePostReq req);







}
