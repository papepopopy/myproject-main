package com.spring.myproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.myproject.entity.Board;
import io.swagger.v3.oas.annotations.callbacks.Callback;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
  private Long rno;

  // spring-boot-starter-validation: 서버 유효성 검사 라이브러리
  @NotNull
  private Long bno;       // 댓글의 부모: 클라이언트로 부터 넘어온 게시글 번호
  private Board board;    // replyDTO와 reply Entity 맵핑을 위한 객체

  @NotEmpty
  private String replyText;
  @NotEmpty
  private String replyer;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime regDate;

  @JsonFormat
  private LocalDateTime modDate;

}