package com.example.demo.src.member;


import com.example.demo.src.member.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberDao {

    @Select("select count(*) from Member")
    public int getMemberCnt();

    @Select("select memberId, nickname, basicTownId, memberStatus  " +
            "from Member where memberId = #{memberId}")
    public PostLoginRes login(Integer memberId);


    @Select("select memberId, pwd " +
            "from Member where memberId = #{memberId}")
    public Member getPwd(PostLoginReq req);



    @Insert("insert into Member(phone, pwd, nickname, town1Id " +
            ", basicTownId, profile) " +
            " values(#{phone}, #{pwd}, #{nickname}, #{town1Id}, " +
            " #{basicTownId}, #{profile})")
    @Options(useGeneratedKeys = true, keyProperty = "memberId")
    public int createUser(PostMemberReq postMemberReq);


    @Select("select nickname, memberId, s.name t1name, s.town1AuthCnt town1AuthCnt, \n" +
            "       t2.name t2name, s.town2AuthCnt town2AuthCnt, DATE_FORMAT(s.createdAt, '%Y년 %m월 %d일') createdAt, \n" +
            "       bcnt.count bcount, icnt.count icount, temperature temperature, responseRate responseRate, e.count ecnt, ec.count eccnt \n" +
            "from (select count(*) count from Badge where memberId = #{memberId}) bcnt,\n" +
            "    (select count(*) count from Itempost where writerId = #{memberId}) icnt,\n" +
            "    (select count(*) count from Estimate where sellerId=#{memberId} or buyerId =#{memberId}) e,\n" +
            "    (select count(*) count from Estimate where (sellerId=#{memberId} or buyerId =#{memberId}) and content>1) ec,\n" +
            "     (select *\n" +
            "      from Member m\n" +
            "               join Town t\n" +
            "                    on m.town1Id = t.townId) s\n" +
            "    join Town t2\n" +
            "on s.town2Id = t2.townId\n" +
            "where memberId = #{memberId}")
    public GetProfileRes getProfile(Integer memberId);

    // 닉네임 중복 여부
    @Select("select count(*) from Member where nickname = #{nickname} and " +
            "town1Id = #{town1Id} or town2Id = #{town1Id}")
    public int checkValidNickname(PostMemberReq req);

    // 핸드폰 중복 여부
    @Select("select count(*) from Member where phone = #{phone}")
    public int checkValidPhone(PostMemberReq req);


    // 유효한 동네인지 여부
    @Select("select count(*) from Town where townId = #{id}")
    public int checkValidTown(Integer id);


    @Select("select profile, nickname, memberId from Member where memberId = #{memberId}")
    public GetProfileForUpdateRes getProfileForUpdate(Integer memberId);


    @Update("update Member set profile = #{profile}, nickname = #{nickname} where memberId = #{memberId}")
    public int updateProfile( PatchProfileReq req);

    @Update("update Member set email = #{email} where memberId = #{memberId}")
    public int updateEmail(PatchManageInformationReq req);

    @Update("update Member set phone = #{phone} where memberId = #{memberId}")
    public int updatePhone(PatchManageInformationReq req);

    @Select("select memberId, email, phone from Member where memberId = #{memberId}")
    public GetManageInformationRes getManageInformation(Integer memberId);


    @Select("select sellerId, buyerId, roomNo from Chatroom " +
            "where postTable='Itempost' and postId = #{itemPostId} and sellerId = #{memberId}")
    public PostSoldItem_selectBuyer getBuyerList(PostSoldItemReq req);
}
