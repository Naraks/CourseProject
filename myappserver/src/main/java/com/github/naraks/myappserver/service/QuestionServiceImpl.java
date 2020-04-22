package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.question.AnswerItemDTO;
import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.JournalFilterItem;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
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
    public List<QuestionItemDTO> getPagingAndFilteredQuestions(JournalRowsRequestDTO request) {
        return getFilteredQuestions(request)
                .subList((request.getPage() - 1) * request.getPageSize(),
                        Math.min(getFilteredQuestions(request).size(),
                                ((request.getPage() - 1) * request.getPageSize() + request.getPageSize())));
    }

    @Override
    public Integer countFilteredQuestions(JournalRowsRequestDTO request) {
        List<Question> questions = questionRepository.findAll();
        for (JournalFilterItem filterItem : request.getFilters()) {
            switch (filterItem.getCode()) {
                case "question-answer-count":
                    if (filterItem.getValue() != null && !filterItem.getValue().equals("")) {
                        questions = questions.stream()
                                .filter(question ->
                                        answerRepository.findByQuestion(question).size() == Integer.parseInt(filterItem.getValue()))
                                .collect(Collectors.toList());
                    }
                    break;

                default:
                    throw new RuntimeException(String.format("Filter item with code %s not found", filterItem.getCode()));
            }
        }
        return questions.size();
    }

    public List<QuestionItemDTO> getFilteredQuestions(JournalRowsRequestDTO request) {
        List<QuestionItemDTO> questionsItemDTO = getQuestions(request);
        for (JournalFilterItem filterItem : request.getFilters()) {
            switch (filterItem.getCode()) {
                case "question-answer-count":
                    if (filterItem.getValue() != null && !filterItem.getValue().equals("")) {
                        questionsItemDTO = questionsItemDTO.stream()
                                .filter(questionItemDTO -> questionItemDTO.getAnswers().size() == Integer.parseInt(filterItem.getValue()))
                                .collect(Collectors.toList());
                    }
                    break;

                default:
                    throw new RuntimeException(String.format("Filter item with code %s not found", filterItem.getCode()));
            }
        }
        return questionsItemDTO;
    }

    public List<QuestionItemDTO> getQuestions(JournalRowsRequestDTO request) {
        return questionRepository.getByNameContainingIgnoreCase(request.getSearch())
                .stream()
                .map(question -> new QuestionItemDTO(question, answerRepository.findByQuestion(question)))
                .collect(Collectors.toList());
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
