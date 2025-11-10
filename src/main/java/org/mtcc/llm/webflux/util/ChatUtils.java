package org.mtcc.llm.webflux.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUtils {
	public static String extractJsonString(String content) {
		int startIndex = content.indexOf('{');
		int endIndex = content.lastIndexOf('}');

		if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
			return content.substring(startIndex, endIndex + 1);
		}
		return "";
	}
}
