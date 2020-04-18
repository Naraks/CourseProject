package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.AnswerItemDTO;
import com.github.naraks.myappserver.dto.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.QuestionItemDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.JournalFilterItem;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public QuestionItemDTO save(QuestionItemDTO dto) {
        Question question = new Question();
        question.setName(dto.getName());
        questionRepository.save(question);

        createAnswers(dto, question);

        return new QuestionItemDTO(question, answerRepository.findByQuestion(question));
    }

    @Override
    public QuestionItemDTO edit(QuestionItemDTO dto) {
        Question question = questionRepository
                .findById(Long.parseLong(dto.getId()))
                .orElseThrow(() -> new RuntimeException(String.format("Question with id %s not exist", dto.getId())));
        question.setName(dto.getName());
        questionRepository.save(question);

        answerRepository.deleteByQuestion(question);
        createAnswers(dto, question);

        return new QuestionItemDTO(question, answerRepository.findByQuestion(question));
    }

    @Override
    public List<QuestionItemDTO> getQuestions(String journalId, JournalRowsRequestDTO request) {
        Pageable pageRequest = PageRequest.of(request.getPage() - 1, request.getPageSize());
        return questionRepository.getByNameContainingIgnoreCase(request.getSearch(), pageRequest)
                .stream()
                .map(question -> new QuestionItemDTO(question, answerRepository.findByQuestion(question)))
                .collect(Collectors.toList());
    }

    @Override
    public Integer countQuestions(JournalRowsRequestDTO request) {
        return Math.toIntExact(questionRepository.getByNameContainingIgnoreCase(request.getSearch()).size());
    }

    private void createAnswers(QuestionItemDTO dto, Question question) {
        for (AnswerItemDTO itemDTO : dto.getAnswers()) {
            Answer answer = new Answer();
            answer.setName(itemDTO.getAnswerText());
            answer.setQuestion(question);
            answer.setIsCorrect(itemDTO.getIsCorrect());
            answerRepository.save(answer);
        }
    }
}
