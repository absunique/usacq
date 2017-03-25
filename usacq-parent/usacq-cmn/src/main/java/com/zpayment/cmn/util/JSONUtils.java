/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */
package com.zpayment.cmn.util;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
public class JSONUtils {

//	private static JsonConfig jsonConfig = new JsonConfig();
	private static Gson glbGson;
	static {
//		jsonConfig.setExcludes(new String[] { "jsonValue" });
//		String[] formats = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
//		net.sf.json.util.JSONUtils.getMorpherRegistry().registerMorpher(
//				new TimestampMorpher(formats));
//		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,
//				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		glbGson = new GsonBuilder()
				.registerTypeAdapter(Timestamp.class,
						new TimestampTypeAdapter())
				.setExclusionStrategies(new ExclusionStrategy() {

					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						return f.getName().equals("jsonValue");
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				}).create();

	}

	public static String toJson(Object obj) {
		return glbGson.toJson(obj);
	}

	public static String toJson(Object obj, final String[] ignoreFields) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Timestamp.class,
						new TimestampTypeAdapter())
				.setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes f) {
						String fldName = f.getName();
						for (String ignoreFiled : ignoreFields) {
							if (ignoreFiled.equals(fldName)) {
								return true;
							}
						}
						return false;
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						// TODO Auto-generated method stub
						return false;
					}
				}).create();
		return gson.toJson(obj);
	}

	public static <T> T jsonToObj(String jsonData, Class<T> clazz) {
		return glbGson.fromJson(jsonData, clazz);
	}

	@SuppressWarnings({ })
	public static <T> T jsonToObj(String jsonData, Class<T> clazz,
			Map<String, Class<?>> classMap) {
		return glbGson.fromJson(jsonData, clazz);

		// JSONObject jsonObject1 = JSONObject.fromObject(jsonData, jsonConfig);
		// T result = (T) JSONObject.toBean(jsonObject1, clazz, classMap);
		// return result;
	}

}

class TimestampTypeAdapter implements JsonSerializer<Timestamp>,
		JsonDeserializer<Timestamp> {
	private final DateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public JsonElement serialize(Timestamp src, Type arg1,
			JsonSerializationContext arg2) {
		String dateFormatAsString = format.format(new Date(src.getTime()));
		return new JsonPrimitive(dateFormatAsString);
	}

	public Timestamp deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		if (!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be a string value");
		}

		try {
			Date date = format.parse(json.getAsString());
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

}