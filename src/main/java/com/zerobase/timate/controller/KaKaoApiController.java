package com.zerobase.timate.controller;

import com.zerobase.timate.service.KakaoApiService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class KaKaoApiController {

    private final KakaoApiService kakaoApiService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchPlaces(@RequestParam String keyword) {
        return kakaoApiService.searchPlaceByKeyword(keyword);
    }

}
