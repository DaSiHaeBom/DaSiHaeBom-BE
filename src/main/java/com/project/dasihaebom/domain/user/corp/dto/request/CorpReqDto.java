package com.project.dasihaebom.domain.user.corp.dto.request;

public class CorpReqDto {

    public record CorpCreateReqDto(
            String loginId,
            String password,
            String ceoName,
            String phoneNumber,
            String corpNumber,
            String corpName,
            String corpAddress
    ) {
    }

    public record CorpUpdateReqDto(
            String ceoName,
            String phoneNumber,
            String corpNumber,
            String corpName,
            String corpAddress
    ) {
    }
}
