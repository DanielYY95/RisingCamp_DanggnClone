package com.example.demo.src.chat;


import com.example.demo.src.chat.model.GetChatRoomRes;
import com.example.demo.src.chat.model.GetChatRoomRes_Chat;
import com.example.demo.src.chat.model.GetChatRoomsRes;
import com.example.demo.src.chat.model.PostChatReq;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ChatDao {

    @Select("select count(*) from Member where memberId = #{memberId}")
    public int isValidMember(Integer memberId);

    @Select("select count(*) from Itempost where itemPostId = #{itemPostId}")
    public int isValidItemPost(Integer itemPostId);

    @Select("select count(*) from Chatroom where roomNo = #{roomNo}")
    public int isValidChatRoom(Integer roomNo);


    @Select("select b.bProfile bProfile, b.nickname bNickname, c.sProfile sProfile,\n" +
            "       c.nickname sNickname, b.lastChatMsg lastChatMsg, b.lastChatMsgType lastChatMsgType, " +
            "  case\n" +
            "        when TIMESTAMPDIFF(SECOND, b.lastChatTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(SECOND , b.lastChatTime,CURRENT_TIMESTAMP), '초 전')\n" +
            "        when TIMESTAMPDIFF(MINUTE , b.lastChatTime,CURRENT_TIMESTAMP) < 60\n" +
            "            then concat(TIMESTAMPDIFF(MINUTE , b.lastChatTime,CURRENT_TIMESTAMP), '분 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, b.lastChatTime,CURRENT_TIMESTAMP) < 24\n" +
            "            then concat(TIMESTAMPDIFF(HOUR, b.lastChatTime,CURRENT_TIMESTAMP), '시간 전')\n" +
            "        when TIMESTAMPDIFF(HOUR, b.lastChatTime,CURRENT_TIMESTAMP) < 48\n" +
            "            then '하루 전'\n" +
            "        when TIMESTAMPDIFF(DAY, b.lastChatTime,CURRENT_TIMESTAMP) < 30\n" +
            "            then concat(TIMESTAMPDIFF(DAY, b.lastChatTime,CURRENT_TIMESTAMP), '일 전')\n" +
            "        when TIMESTAMPDIFF(MONTH , b.lastChatTime,CURRENT_TIMESTAMP) < 12\n" +
            "            then concat(TIMESTAMPDIFF(MONTH , b.lastChatTime,CURRENT_TIMESTAMP), '달 전')\n" +
            "        else concat(TIMESTAMPDIFF(YEAR, b.lastChatTime,CURRENT_TIMESTAMP), '년 전')\n" +
            "  end lastChatTime,\n" +
            "        buyerCity bCity , buyerDistrict bDistrict, buyerName bTown,\n" +
            "        sellerCity sCity, sellerDistrict sDistrict, sellerName sTown,\n" +
            "        p.path path, ifnull(rc.cnt, '0') readCnt \n" +
            "from (select *, profile bProfile from Chatroom join Member on Chatroom.buyerId = Member.memberId) b\n" +
            "    join (select *, profile sProfile from Chatroom join Member on Chatroom.sellerId = Member.memberId) c\n" +
            "    on b.roomNo = c.roomNo\n" +
            "    join\n" +
            "    (select t1.city buyerCity, t1.district buyerDistrict, t1.name buyerName,\n" +
            "       t2.city sellerCity, t2.district sellerDistrict, t2.name sellerName,roomNo\n" +
            "    from\n" +
            "        (select Member.town1Id buyerTown, c.town1Id sellerTown, c.roomNo from Chatroom join Member on Chatroom.buyerId = Member.memberId\n" +
            "        join (select *, profile sProfile from Chatroom join Member on Chatroom.sellerId = Member.memberId) c\n" +
            "        on Chatroom.roomNo = c.roomNo) a\n" +
            "        join Town t1\n" +
            "        on t1.townId = a.buyerTown\n" +
            "        join Town t2\n" +
            "        on t2.townId = a.sellerTown) town\n" +
            "    on b.roomNo = town.roomNo\n" +
            "    left outer join (select * from Postimg where `order` = 1) p\n" +
            "    on b.postId = p.postId \n" +
            "    left outer join (select roomNo, count(*) cnt from Chat where `read` = 'F' and talkerId <> #{memberId} group by roomNo) rc " +
            "    on b.roomNo = rc.roomNo   " +
            "    where b.buyerId = #{memberId} or b.sellerId = #{memberId}" +
            "   and case when p.`order` is not null\n" +
            "        then  b.postTable = p.postTable\n" +
            "        else 1=1\n" +
            "            end")
    public List<GetChatRoomsRes> getChatRooms(Integer memberId);



    @Select("select  buyerCity  bCity, buyerDistrict bDistrict, buyerName bTown, b.bProfile  bProfile, b.nickname bNickname, b.temperature bTemperature, b.phone bPhone,\n" +
            "        sellerCity sCity, sellerDistrict  sDistrict, sellerName  sTown, c.sProfile  sProfile, c.nickname sNickname, c.temperature sTemperature, c.phone sphone,\n" +
            "        p.path path, title, price, if(sellStatus<>3, '판매중', '거래완료') sellStatus, b.postTable postTable, b.postId postId, b.buyerId buyerId, b.sellerId sellerId \n" +
            "from (select *, profile bProfile from Chatroom join Member on Chatroom.buyerId = Member.memberId where postTable = 'Itempost') b\n" +
            "    join (select *, profile sProfile from Chatroom join Member on Chatroom.sellerId = Member.memberId) c\n" +
            "        on b.roomNo = c.roomNo\n" +
            "    join\n" +
            "        (select t1.city buyerCity, t1.district buyerDistrict, t1.name buyerName,\n" +
            "            t2.city sellerCity, t2.district sellerDistrict, t2.name sellerName,roomNo\n" +
            "    from\n" +
            "        (select Member.town1Id buyerTown, c.town1Id sellerTown, c.roomNo from Chatroom join Member on Chatroom.buyerId = Member.memberId\n" +
            "        join (select *, profile sProfile from Chatroom join Member on Chatroom.sellerId = Member.memberId where roomNo = #{roomno}) c\n" +
            "            on Chatroom.roomNo = c.roomNo) a\n" +
            "        join Town t1\n" +
            "            on t1.townId = a.buyerTown\n" +
            "        join Town t2\n" +
            "            on t2.townId = a.sellerTown) town\n" +
            "        on b.roomNo = town.roomNo\n" +
            "    left outer join (select * from Postimg where postTable = 'Itempost') p\n" +
            "        on b.postId = p.postId\n" +
            "    join Itempost i\n" +
            "    on b.postId = i.itemPostId\n" +
            "    where b.buyerId = #{memberId} or b.sellerId = #{memberId}\n" +
            "        and case when p.path <> null\n" +
            "            then p.`order` = 1\n" +
            "            else 1=1\n" +
            "        end ")
    public GetChatRoomRes getChatRoomStatus(@Param("roomno") Integer roomno, @Param("memberId") Integer memberId);





    @Select("select chatId, talkerId, content, DATE_FORMAT(createdAt, '%Y년 %m월 %d일 %H:%i') createdAt,\n" +
            "\t\t\tif(`read`<>'F', '읽음', '1') isRead\n" +
            "from Chat\n" +
            "where roomNo = #{roomno} \n" +
            "order by createdAt")
    public List<GetChatRoomRes_Chat> getChatRoomChats(Integer roomno);


    @Update("update Chat set `read` = 'T' where roomNo = #{roomno} and talkerId <> #{memberId}")
    public int updateIsRead(@Param("roomno") Integer roomno, @Param("memberId") Integer memberId);



    @Select("select  b.city  bCity, b.District bDistrict, b.Name bTown, b.Profile  bProfile, b.nickname bNickname, b.temperature bTemperature, b.phone bPhone,\n" +
            "        s.City sCity, s.District  sDistrict, s.Name  sTown, s.Profile  sProfile, s.nickname sNickname, s.temperature sTemperature, s.phone sphone,\n" +
            "                   p.path path, title, price, if(sellStatus<>3, '판매중', '거래완료') sellStatus, p.postTable postTable, p.postId postId, b.memberId buyerId, s.memberId sellerId\n" +
            "from Itempost i\n" +
            "join(select * from Member join Town T on Member.town1Id = T.townId) s\n" +
            "    on i.writerId =s.memberId\n" +
            "join (select * from Member join Town T on Member.town1Id = T.townId where memberId = #{memberId}) b\n" +
            "    on 1=1\n" +
            "left outer join (select postId, postTable, path from Postimg where postTable = 'Itempost') p\n" +
            "    on i.itemPostId = p.postId\n" +
            "where i.itemPostId = #{itemPostId} \n")
    public GetChatRoomRes getChatRoomReady(@Param("itemPostId") Integer itemPostId, @Param("memberId") Integer memberId);


    @Insert("insert into Chatroom(buyerId, sellerId, lastChatMsg, lastChatMsgType, lastChatTIme, postId, postTable) " +
            "values(#{buyerId}, #{sellerId}, #{content}, 'msg', now(), #{postId}, #{postTable}) ")
    @Options(useGeneratedKeys = true,  keyColumn = "roomNo", keyProperty = "roomNo")
    public int createChatroom(PostChatReq req);


    @Insert("insert into Chat(buyerId, sellerId, content, roomNo, postId, postTable, talkerId) " +
            " values(#{buyerId}, #{sellerId}, #{content}, #{roomNo}, #{postId}, #{postTable}, #{talkerId})")
    public int createChat(PostChatReq req);


    @Update("update Chatroom Set lastChatMsg = #{content}, lastChatMsgType= 'msg', lastChatTime = now() where roomNo = #{roomNo}")
    public int updateChatRoom(PostChatReq req);


}
