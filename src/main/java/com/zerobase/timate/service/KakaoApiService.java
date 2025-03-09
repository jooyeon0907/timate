package com.zerobase.timate.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KakaoApiService {


	@Value("${KAKAO_API_KEY}")
	private String kakaoApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

	public List<Map<String, Object>> searchPlaceByKeyword(String query) {
		URI url = UriComponentsBuilder.fromUriString(
				"https://dapi.kakao.com/v2/local/search/keyword.json")
			.queryParam("query", query)
			.queryParam("page", 5)
			.encode(StandardCharsets.UTF_8)
			.build().toUri();

		var headers = new org.springframework.http.HttpHeaders();
		headers.set("Authorization", "KakaoAK " + kakaoApiKey);
		var entity = new org.springframework.http.HttpEntity<>(headers);

		var response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity,
			Map.class);
		List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody()
			.get("documents");

		// 필요한 필드만 반환
		return documents.stream()
			.map(document -> {
				Map<String, Object> placeInfo = new HashMap<>();
				placeInfo.put("address", document.get("address_name"));
				placeInfo.put("place_name", document.get("place_name"));
				placeInfo.put("longitude", document.get("x"));
				placeInfo.put("latitude", document.get("y"));
				return placeInfo;
			})
			.collect(Collectors.toList());
	}


}
