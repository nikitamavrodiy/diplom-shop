package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdsCommentMapper;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.service.AdsService;

import javax.validation.Valid;

@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления", description = "AdsController")
public class AdsController {

    private static final Logger logger = LoggerFactory.getLogger(AdsController.class);
    private final AdsService adsService;

    private final AdsCommentMapper adsCommentMapper;
    private final AdsMapper adsMapper;

    @Operation(summary = "getAllAds", description = "getAllAds")
    @GetMapping
    public ResponseWrapper<AdsDto> getAllAds() {
        logger.info("Current method is - getAllAds");
        return ResponseWrapper.of(adsMapper.toDto(adsService.getAllAds()));
    }

    @Operation(summary = "getAdsMe", description = "getAdsMe")
    @GetMapping("/me")
    public ResponseWrapper<AdsDto> getAdsMe() {
        return ResponseWrapper.of(adsMapper.toDto(adsService.getAdsMe()));
    }

    @Operation(summary = "getFullAd", description = "getFullAd")
    @GetMapping("/{adId}")
    public ResponseEntity<FullAdsDto> getFullAd(@PathVariable("adId") Long adId) {
        return ResponseEntity.ok(adsMapper.toFullAdsDto(adsService.getAdsById(adId)));
    }

    @Operation(summary = "addAds", description = "addAds")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdsDto> addAds(@Parameter(description = "Данные нового объявления")
                                             @RequestPart("image") MultipartFile imageFile,
                                         @Valid @RequestPart("properties") CreateAdsDto createAdsDto) {
        logger.info("Current method is - addAds");
        return ResponseEntity.ok(adsMapper.toDto(adsService.addAds(createAdsDto, imageFile)));
    }

    @Operation(summary = "getAdsComment", description = "getAdsComment")
    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(@PathVariable("ad_pk") long adPk,
                                                     @PathVariable("id") long id) {
        return ResponseEntity.ok(adsCommentMapper.toDto(adsService.getAdsComment(adPk, id)));
    }

    @Operation(summary = "deleteAdsComment", description = "deleteAdsComment")
    @DeleteMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<HttpStatus> deleteAdsComment(@PathVariable("ad_pk") long adPk,
                                                       @PathVariable("id") long id) {
        adsService.deleteAdsComment(adPk, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "updateComments", description = "updateComments")
    @PatchMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<AdsCommentDto> updateComments(@PathVariable("ad_pk") int adPk,
                                                     @PathVariable("id") int id,
                                                     @RequestBody AdsCommentDto adsCommentDto) {
        return ResponseEntity.ok(adsCommentMapper.toDto(adsService.updateComments(
                adPk, id, adsCommentMapper.toEntity(adsCommentDto))));

    }

    @Operation(summary = "updateAds", description = "updateAds")
    @PatchMapping("/{adId}")
    public ResponseEntity<AdsDto> updateAds(@PathVariable("adId") Long adId,
                                            @RequestBody CreateAdsDto createAdsDto) {
        return ResponseEntity.ok(adsMapper.toDto(adsService.updateAds(adId, createAdsDto)));
    }

    @Operation(summary = "removeAds", description = "removeAds")
    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> removeAds(@PathVariable("adId") Long adId) {
            adsService.removeAdsById(adId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "getComments", description = "getComments")
    @GetMapping("/{ad_pk}/comments")
    public ResponseWrapper<AdsCommentDto> getComments(@PathVariable("ad_pk") long adPk) {
        return ResponseWrapper.of(adsCommentMapper.toDto(adsService.getComments(adPk)));
    }

    @Operation(summary = "addAdsComments", description = "addAdsComments")
    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<AdsCommentDto> addAdsComments(@PathVariable("ad_pk") long adPk,
                                                        @RequestBody @Valid AdsCommentDto adsCommentDto) {
        return ResponseEntity.ok(adsCommentMapper.toDto(adsService.addAdsComments(adPk, adsCommentDto)));
    }
}
