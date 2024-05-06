package com.gdsc.petwalk.domain.board.service;

import com.gdsc.petwalk.domain.board.entity.Board;
import com.gdsc.petwalk.domain.board.repository.BoardRepository;
import com.gdsc.petwalk.domain.member.entity.Member;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Board createBoard(String title, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        Board board = Board.builder()
            .title(title)
            .content(content)
            .writer(member)
            .writingTime(LocalDateTime.now())
            .build();
        return boardRepository.save(board);
    }

    public Optional<Board> getBoard(Long id) {
        return boardRepository.findById(id);
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board updateBoard(Long id, String title, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!board.getWriter().equals(member)) {
            throw new SecurityException("자신의 게시글만 수정할 수 있습니다.");
        }

        board.changeTitle(title);
        board.updateContent(content);
        return boardRepository.save(board);
    }

    @Transactional
    public void deleteBoard(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!board.getWriter().equals(member)) {
            throw new SecurityException("자신의 게시글만 삭제할 수 있습니다.");
        }

        boardRepository.delete(board);
    }
}
