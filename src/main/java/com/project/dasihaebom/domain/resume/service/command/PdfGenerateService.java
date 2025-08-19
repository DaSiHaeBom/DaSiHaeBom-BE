package com.project.dasihaebom.domain.resume.service.command;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.project.dasihaebom.domain.resume.entity.Resume;
import com.project.dasihaebom.domain.resume.repository.ResumeRepository;
import com.project.dasihaebom.domain.user.worker.entity.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;


@Service
@RequiredArgsConstructor
public class PdfGenerateService {

    private final TemplateEngine templateEngine;
    private final ResumeRepository resumeRepository;

    @Transactional(readOnly = true)
    public byte[] generateResumePdf(Long resumeId) throws IOException {

        // 1. DB에서 이력서 데이터 조회
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이력서를 찾을 수 없습니다. ID: " + resumeId));
        Worker worker = resume.getWorker();

        // 2. 자기소개서 텍스트의 줄바꿈 문자(\n)를 <br/> HTML 태그로 미리 변환
        String introductionWithLineBreaks = "";
        if (resume.getIntroductionFullText() != null) {
            introductionWithLineBreaks = resume.getIntroductionFullText().replaceAll("\n", "<br/>");
        }

        // 3. Thymeleaf 템플릿에 전달할 데이터 모델 생성
        Context context = new Context();
        context.setVariable("resume", resume);
        context.setVariable("licenses", worker.getLicense());
        context.setVariable("introductionHtml", introductionWithLineBreaks);

        // 4. 템플릿 엔진을 사용해 HTML 생성
        String html = templateEngine.process("resume", context);

        // 5. 생성된 HTML을 PDF 바이트 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();

        try {
            File fontFile = new ClassPathResource("fonts/NanumGothic.ttf").getFile();
            builder.useFont(fontFile, "Malgun Gothic");

            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 중 오류가 발생했습니다.", e);
        }

        return outputStream.toByteArray();
    }
}
