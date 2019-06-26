package com.jhyx.halfroom.commons;

import java.util.Random;
import java.util.UUID;

public class UniqueKeyGeneratorUtil {
	public static String generateToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String random(int n) {
		String random = "";
		Random _random = new Random();
		for (int i = 0; i < n; i++) {
			random += _random.nextInt(10);
		}
		return random;
	}

	/**
	 * 生成[min,max]区间内的随机数
	 */
	public static String random(int n, int min, int max) {
		String random = "";
		Random _random = new Random();
		for (int i = 0; i < n; i++) {
			random += (_random.nextInt(max) % (max - min + 1) + min);
		}
		return random;
	}
}
