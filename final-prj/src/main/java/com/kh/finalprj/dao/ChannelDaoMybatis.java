package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ChannelDto;

@Repository
public class ChannelDaoMybatis implements ChannelDao{
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.channel.sequence");
	}

	@Override
	public void insert(ChannelDto channelDto) {
		sqlSession.insert("mapper.channel.create", channelDto);
	}

	@Override
	public List<ChannelDto> list(int projectNo) {
		return sqlSession.selectList("mapper.channel.list", projectNo);
	}

	@Override
	public ChannelDto selectOne(int projectNo, int channelNo) {
		ChannelDto channelDto = ChannelDto.builder()
	            .projectNo(projectNo)
	            .chatChannelNo(channelNo)
            .build();
		return sqlSession.selectOne("mapper.channel.find", channelDto);
	}

	@Override
	public void delete(int projectNo, int channelNo) {
		ChannelDto channelDto = ChannelDto.builder()
	            .projectNo(projectNo)
	            .chatChannelNo(channelNo)
            .build();
		sqlSession.delete("mapper.channel.delete", channelDto);
	}

	@Override
	public void update(ChannelDto channelDto) {
		sqlSession.update("mapper.channel.update", channelDto);
	}

}
