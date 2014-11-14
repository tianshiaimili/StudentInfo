package com.aixuexiao.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

/**
 * 这个是放在fontawesome-webfont.svg下的 ，先暂时提取出来
 * <font-face units-per-em="1792" ascent="1536" descent="-256"></font-face>
 * 
 * @author zero
 *
 */
public class BaseDao {

	@Resource(name="readSqlSession")
	public SqlSessionTemplate readSqlSession;
	
	@Resource(name="writerSqlSession")
	public SqlSessionTemplate writerSqlSession;
	
}
