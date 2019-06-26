package com.jhyx.halfroom.commons;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;

public class JSONUtil {
	public static final JSONObject EMPTY_JSONOBJECT = new JSONObject();
	public static final JSONArray EMPTY_JSONARRAY = new JSONArray();



	@SuppressWarnings("unchecked")
	public static <T> List<T> parseList(String content, Class<T> clazz) {
		if (StringUtils.isBlank(content)) {
			return Collections.emptyList();
		}
		List<T> results = null;
		try {
			results = JSON.parseArray(content, clazz);

		} catch (Exception e) {}
		return (results == null ? Collections.EMPTY_LIST : results);
	}

	public static <T> T parse(String content, Class<T> clazz) {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		T result = null;
		try {
			result = JSON.parseObject(content, clazz);
		} catch (Exception e) {}
		return result;
	}

	public static JSONObject parseObject(String content) {
		if (StringUtils.isBlank(content)) {
			return EMPTY_JSONOBJECT;
		}
		JSONObject result = null;
		try {
			result = JSON.parseObject(content);
		} catch (Exception e) {}
		return result == null ? EMPTY_JSONOBJECT : result;
	}
	
	/**
	 * 解析JsonArray
	 * 
	 * @param content
	 * @return
	 */
	public static JSONArray parseArray(String content) {
		if (StringUtils.isBlank(content)) {
			return EMPTY_JSONARRAY;
		}
		JSONArray result = null;
		try {
			result = JSON.parseArray(content);
		} catch (Exception e) {}
		return result == null ? EMPTY_JSONARRAY : result;
	}

	/**
	 * 将对象解析成JSON
	 */
	public static String toJSONString(Object object) {
		if (object == null) {
			return EMPTY_JSONOBJECT.toJSONString();
		}
		String result = null;
		try {
			result = JSON.toJSONString(object);
		} catch (Exception e) {}
		return result == null ? EMPTY_JSONOBJECT.toJSONString() : result;
	}

	/**根据object 返回json
	 * @param object
	 * @return
	 */
	public static JSONObject toJSON(Object object) {
		
		if (object == null) {
			return null;
		}
		JSONObject result = null;
		try {
			result = (JSONObject)JSON.toJSON(object);
		} catch (Exception e) {}
		return result;
	}
	
	
	/**创建一个空jsonobject
	 * @return
	 */
	public static JSONObject createNew(){
		return new JSONObject();
	}
}
