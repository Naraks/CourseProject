package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.QuestionItemDTO;
import com.github.naraks.myappserver.dto.SessionItemDTO;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import com.github.naraks.myappserver.repository.SessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionServiceImpl implements SessionService{

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public List<SessionItemDTO> getSessions(String journalId, JournalRowsRequestDTO request) {
        Pageable pageRequest = PageRequest.of(request.getPage() - 1, request.getPageSize());
        return sessionRepository.getByFullNameContainingIgnoreCase(request.getSearch(), pageRequest)
                .stream()
                .map(SessionItemDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Integer countSessions(JournalRowsRequestDTO request) {
        return Math.toIntExact(sessionRepository.getByFullNameContainingIgnoreCase(request.getSearch()).size());
    }

    @Override
    public List<QuestionItemDTO> getQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(question -> new QuestionItemDTO(question, answerRepository.findByQuestion(question)))
                .collect(Collectors.toList());
    }
}
