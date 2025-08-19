package com.project.dasihaebom.domain.user.corp.converter;

import com.project.dasihaebom.domain.location.entity.Coordinates;
import com.project.dasihaebom.domain.user.Role;
import com.project.dasihaebom.domain.user.corp.dto.request.CorpReqDto;
import com.project.dasihaebom.domain.user.corp.dto.response.CorpResDto;
import com.project.dasihaebom.domain.user.corp.entity.Corp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorpConverter {

    public static Corp toCorp(CorpReqDto.CorpCreateReqDto corpCreateReqDto, List<Double> corpCoordinatesAsList) {


        Coordinates coordinates = new Coordinates(corpCoordinatesAsList.get(1), corpCoordinatesAsList.get(0));

        return Corp.builder()
                .loginId(corpCreateReqDto.loginId())
                .ceoName(corpCreateReqDto.ceoName())
                .phoneNumber(corpCreateReqDto.phoneNumber())
                .corpNumber(corpCreateReqDto.corpNumber())
                .corpName(corpCreateReqDto.corpName())
                .corpAddress(corpCreateReqDto.corpAddress())
                .role(Role.CORP)
                .coordinates(coordinates)
                .build();
    }

    public static CorpResDto.CorpNumberValidResDto toCorpNumberValidResDto(CorpReqDto.CorpNumberValidReqDto corpNumberValidReqDto, boolean isValid) {
        return CorpResDto.CorpNumberValidResDto.builder()
                .corpNumber(corpNumberValidReqDto.corpNumber())
                .isValid(isValid)
//                .corpInfo(corpInfo)
                .build();
    }

    public static CorpResDto.CorpProfileResDto toCorpProfileResDto(Corp corp) {
        return CorpResDto.CorpProfileResDto.builder()
                .role(corp.getRole())
                .ceoName(corp.getCeoName())
                .phoneNumber(corp.getPhoneNumber())
                .corpNumber(corp.getCorpNumber())
                .corpName(corp.getCorpName())
                .corpAddress(corp.getCorpAddress())
                .build();
    }
}
