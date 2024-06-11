package com.gdsc.petwalk.global.exception;

import com.gdsc.petwalk.global.exception.custom.MemberException;
import com.gdsc.petwalk.global.exception.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExControllerAdvice {

    // 컨트롤러 단에서 발생하는 예외 처리
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ExceptionDto> DuplicateEmailException(MemberException e){
        log.info("Email 중복 오류 예외 핸들러 발생");
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(e.getErrorCode().getMessage())
                .errorCode(e.getErrorCode().getHttpStatus().toString())
                .build();

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(exceptionDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> NotValidEmailException(MethodArgumentNotValidException e){
        log.info("회원가입 시 형식 오류 예외 핸들러 발생");
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .errorCode(e.getStatusCode().toString())
                .build();

        return ResponseEntity.status(e.getStatusCode()).body(exceptionDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> UsernameNotFoundException(UsernameNotFoundException e){
        log.info("DB에서 유저 조회 시 예외 핸들러 발생");
        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message(e.getMessage())
                .errorCode(HttpStatus.BAD_REQUEST.toString())
                .build();

        return ResponseEntity.badRequest().body(exceptionDto);
    }

}
