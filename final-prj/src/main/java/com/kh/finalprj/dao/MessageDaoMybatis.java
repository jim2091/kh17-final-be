package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.vo.message.ChannelMessageRequestVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUpdateRequestVO;
import com.kh.finalprj.vo.message.MessageVO;

@Repository
public class MessageDaoMybatis implements MessageDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.message.sequence");
	}

	@Override
	public void add(MessageVO message) {
		sqlSession.insert("mapper.message.add", message);
	}

	@Override
	public List<MessageVO> selectList(int channelNo, ChannelMessageRequestVO request) {
		Map<String, Object> params = new HashMap<>();
		params.put("channelNo", channelNo);
		params.put("size", request.getSize());
		params.put("lastMessageNo", request.getLastMessageNo());
		return sqlSession.selectList("mapper.message.selectMessages", params);
	}

	@Override
	public int count(int channelNo, ChannelMessageRequestVO request) {
		Map<String, Object> params = new HashMap<>();
		params.put("channelNo", channelNo);
		params.put("lastMessageNo", request.getLastMessageNo());
		return sqlSession.selectOne("mapper.message.countMessages", params);
	}

	@Override
	public MessageTargetVO selectTarget(int chatMessageNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("chatMessageNo", chatMessageNo);
		return sqlSession.selectOne("mapper.message.selectTarget", params);
	}
	
	@Override
	public void delete(int chatMessageNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("chatMessageNo", chatMessageNo);
		sqlSession.update("mapper.message.delete", params);
	}

	@Override
	public void update(int chatMessageNo, String chatMessageContent) {
		Map<String, Object> params = new HashMap<>();
	    params.put("chatMessageNo", chatMessageNo);
	    params.put("chatMessageContent", chatMessageContent);
	    sqlSession.update("mapper.message.update", params);
	}

	@Override
	public int readChannelMessage(int channelNo, int projectMemberNo) {
		Map<String, Object> params = new HashMap<>();
	    params.put("channelNo", channelNo);
	    params.put("projectMemberNo", projectMemberNo);
		return sqlSession.insert("mapper.message.readChannelMessage", params);
	}

	

}
