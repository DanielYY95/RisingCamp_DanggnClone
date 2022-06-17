package com.example.demo.src.itemPost;


import com.example.demo.src.itemPost.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ItemPostDao {

    // 게시물 조회
        // 1. 해당 게시물의 사진들 2. 게시물의 정보 3. 판매자가 판매하고 있는 상품들 4개  4. 조회 수 증가

    @Select("select `order` imgOrder, path  from Postimg\n" +
            "where postTable = 'Itempost'\n" +
            "and postId = #{itemPostId}")
    public List<GetItemPostRes_imgs> getItemPostImgs(Integer itemPostId);

    @Select("select count(*) cnt, memberStatus from Member where memberId = #{memberId}")
    public HashMap<String, String> checkMemberExists(Integer memberId);


    @Select("select count(*) cnt, postStatus from Itempost where itemPostId = #{itemPostId}")
    public HashMap<String, String> checkPostExists(Integer itemPostId);

    // 유효한 동네인지 여부
    @Select("select count(*) from Town where townId = #{id}")
    public int checkValidTown(Integer id);

    // 유효한 항목인지 여부
    @Select("select count(*) from Category where categoryId = #{id}")
    public int checkValidCategory(Integer id);



    @Select("select i.itemPostId itemPostId,\n" +
            "       i.writerId writerId, profile, nickname, temperature,\n" +
            "       i.title title, i.content content, t.district district, t.name town, " +
            "       i.price price, i.offerStatus offerStatus, \n" +
            "       i.sellStatus sellStatus, i.openStatus openStatus, i.postStatus postStatus, " +
            "       case when i.createdAt = i.pullTime then ''\n" +
            "           else '끌올'\n" +
            "        end isPull,\n" +
            "     case\n" +
            "        when TIMESTAMPDIFF(SECOND, i.pullTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , i.pullTime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , i.pullTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , i.pullTime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pullTime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, i.pullTime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pullTime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, i.pullTime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, i.pullTime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , i.pullTime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , i.pullTime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, i.pullTime,CURRENT_TIMESTAMP), '년 전')\n" +
            "    end pullTime,\n" +
            "         C2.name category,\n" +
            "       ifnull(a.aCnt,'0') chatCnt, ifnull(b.bCnt,'0') interestCnt, i.viewCnt viewCnt, ifnull(mc.cnt, '0') hasInterest \n" +
            "from Itempost i inner join Town t\n" +
            "    on i.townId = t.townId\n" +
            "join Member\n" +
            "    on i.writerId = Member.memberId\n" +
            "join Category C2\n" +
            "    on i.categoryId = C2.categoryId\n" +
            "left outer join (select c.postId, count(c.roomNo) aCnt from Chatroom c where c.postTable = 'Itempost' group by c.postId ) a\n" +
            "    on i.itemPostId =  a.postId\n" +
            "left outer join (select it.postId, count(it.interestId) bCnt from Interest it where it.interestType = '중고거래' group by it.postId ) b\n" +
            "    on i.itemPostId =  b.postId\n" +
            "left outer join (select postId, count(*) cnt from Interest where postId = #{itemPostId} and interestType = '중고거래' and memberId = #{memberId}) mc\n" +
            "    on i.itemPostId = mc.postId " +
            " where itemPostId = #{itemPostId}")
    public GetItemPostRes getItemPostInformation(GetItemPostReq req); // 필요한 데이터가 itemPostId, memberid


    @Select("select i.writerId writerId, title, price, p.path path\n" +
            "from Itempost i\n" +
            "left outer join Postimg p\n" +
            "on i.itemPostId = p.postId\n" +
            "where i.writerId = #{writerId}\n" +
            "and i.itemPostId <> #{itemPostId}\n" +
            "order by viewCnt\n" +
            "LIMIT 4")
    public List<GetItemPostRes_otherItems> getWriterOtherItems(@Param("writerId") Integer writerId, @Param("itemPostId") Integer itemPostId); // 판매자의 다른 상품들 4개 // writerId, itemPostId

    @Update("update Itempost set viewCnt = viewCnt + 1 where itemPostId = #{itemPostId}")
    public int insertViewCnt(Integer itemPostId); // 조회 수 증가



    @Insert("insert into Itempost(writerId, townId, categoryId, offerStatus, title, content, price) values(#{memberId}, #{townId}, #{categoryId}, #{offerStatus}, #{title}, #{content}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "itemPostId")
    public int insertItemPost(PostItemPostReq req);

    @Insert("insert into Postimg(postId, writerId, postTable, `order`, path) values(#{postId}, #{writerId}, 'Itempost', #{img.imgOrder}, #{img.path})")
    public int insertItemPostImgs(@Param("postId") Integer postId, @Param("writerId") Integer writerId, @Param("img") PostItemPostReq_imgs img); // forEach는 어노테이션 options에서는 지원 x


    @Select("select i.itemPostId itemPostId, i.title title, t.name town, i.price price,\n" +
            "       case when i.createdAt = i.pullTime then ''\n" +
            "           else '끌올'\n" +
            "        end isPull\n" +
            "        ,    case\n" +
            "        when TIMESTAMPDIFF(SECOND, i.pulltime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , i.pulltime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , i.pulltime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , i.pulltime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, i.pulltime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, i.pulltime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , i.pulltime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , i.pulltime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, i.pulltime,CURRENT_TIMESTAMP), '년 전')\n" +
            "    end pullTime,\n" +
            "       p.path path, ifnull(a.aCnt,'0') chatCnt, ifnull(b.bCnt,'0') interestCnt\n" +
            "from Itempost i inner join Town t\n" +
            "    on i.townId = t.townId\n" +
            "left outer join (select * from Postimg where `order` = 1) p\n" +
            "    on i.itemPostId = p.postId\n" +
            "    and p.postTable = 'Itempost'\n" +
            "left outer join (select c.postId, count(c.roomNo) aCnt from Chatroom c where c.postTable = 'Itempost' group by c.postId ) a\n" +
            "on i.itemPostId =  a.postId\n" +
            "left outer join (select it.postId, count(it.interestId) bCnt from Interest it where it.interestType = '중고거래' group by it.postId ) b\n" +
            "on i.itemPostId =  b.postId\n" +
            "where title like concat('%',#{keyword},'%') order by pullTime ")
    public List<ItemPostList> getItemPostTitleSearch(String keyword);


    @Select("select i.itemPostId itemPostId, i.title title, t.name town, i.price price,\n" +
            "       case when i.createdAt = i.pullTime then ''\n" +
            "           else '끌올'\n" +
            "        end isPull\n" +
            "        ,    case\n" +
            "        when TIMESTAMPDIFF(SECOND, i.pulltime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , i.pulltime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , i.pulltime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , i.pulltime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.pulltime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, i.pulltime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, i.pulltime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , i.pulltime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , i.pulltime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, i.pulltime,CURRENT_TIMESTAMP), '년 전')\n" +
            "    end pullTime,\n" +
            "       p.path path, ifnull(a.aCnt,'0') chatCnt, ifnull(b.bCnt,'0') interestCnt\n" +
            "from Itempost i inner join Town t\n" +
            "    on i.townId = t.townId\n" +
            "left outer join (select * from Postimg where `order` = 1) p\n" +
            "    on i.itemPostId = p.postId\n" +
            "    and p.postTable = 'Itempost'\n" +
            "left outer join (select c.postId, count(c.roomNo) aCnt from Chatroom c where c.postTable = 'Itempost' group by c.postId ) a\n" +
            "on i.itemPostId =  a.postId\n" +
            "left outer join (select it.postId, count(it.interestId) bCnt from Interest it where it.interestType = '중고거래' group by it.postId ) b\n" +
            "on i.itemPostId =  b.postId\n" +
            "where title not like concat('%',#{keyword},'%') and content like concat('%',#{keyword},'%') order by pullTime ")
    public List<ItemPostList> getItemPostContentSearch(String keyword);



    @Update("update Itempost set pullTime = now() where itemPostId = #{itemPostId} and writerId = #{memberId}")
    public int modifyPullPost(PatchItemPostReq req);

    @Update("update Itempost set postStatus = 'hidden' where itemPostId = #{itemPostId} and writerId = #{memberId}")
    public int modifyToHidden(PatchItemPostReq req);

    @Update("update Itempost set postStatus = 'active' where itemPostId = #{itemPostId} and writerId = #{memberId}")
    public int modifyToActive(PatchItemPostReq req);

    @Update("update Itempost set postStatus = 'deleted' where itemPostId = #{itemPostId} and writerId = #{memberId}")
    public int modifyToDeleted(PatchItemPostReq req);


    @Select("select postStatus from Itempost where itemPostId = #{itemPostId} and writerId = #{memberId}")
    public String checkItemPostStatus(PatchItemPostReq req);


    @Select("select i.itemPostId itemPostId,\n" +
            "       i.writerId writerId, \n" +
            "       i.title title, i.content content,  " +
            "       i.price price, i.offerStatus offerStatus, \n" +
            "         C2.name category \n" +
            "from Itempost i \n" +
            "join Category C2\n" +
            "    on i.categoryId = C2.categoryId\n" +
            " where itemPostId = #{itemPostId} and writerId = #{writerId}")
    public GetItemPostReadyUpdateRes getItemPostReadyUpdate(GetItemPostReadyUpdateReq req); // 필요한 데이터가 itemPostId, memberid


    @Update("update Itempost set title = #{title}, content = #{content}, price= #{price},\n" +
            " offerStatus = #{offerStatus}, categoryId = #{categoryId}\n" +
            " where itemPostId = #{itemPostId} and writerId = #{writerId}")
    public int updateItempost(PutItemPostReq req);


    @Delete("delete from Postimg where postId = #{itemPostId} and postTable = 'Itempost' and writerId = #{writerId}")
    public int deleteImgs(PutItemPostReq req);



    @Select("select i.roomNo roomNo, i.buyerId buyerId, m.nickname nickname, m.profile profile, t.name town,    case\n" +
            "        when TIMESTAMPDIFF(SECOND, i.lastChatTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , i.lastChatTime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , i.lastChatTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , i.lastChatTime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.lastChatTime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, i.lastChatTime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, i.lastChatTime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, i.lastChatTime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, i.lastChatTime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , i.lastChatTime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , i.lastChatTime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, i.lastChatTime,CURRENT_TIMESTAMP), '년 전')\n" +
            "    end lastChatTime from\n" +
            "(select roomNo, buyerId, sellerId, lastChatTime, postId, postTable\n" +
            "from Chatroom \n" +
            "where postId = #{postId} and postTable = 'Itempost' and sellerId = #{memberId}) i\n" +
            "join Member m\n" +
            "on i.buyerId = m.memberId\n" +
            "join Town t\n" +
            "on m.basicTownId = t.townId")
    public List<GetItemPostSellRes> getBuyerIdList(GetItemPostSellReq req);

    @Update("update Itempost set sellStatus = '판매완료' where itemPostId = #{postId}")
    public int updateItemPostSold(PatchItemPostSellReq req);



}
