package com.github.naraks.myappserver.service;

import com.github.naraks.myappserver.dto.journal.JournalRowsRequestDTO;
import com.github.naraks.myappserver.dto.question.QuestionItemDTO;
import com.github.naraks.myappserver.dto.session.AnsweredQuestionDTO;
import com.github.naraks.myappserver.dto.session.SessionItemDTO;
import com.github.naraks.myappserver.dto.session.SessionQuestionAnswerDTO;
import com.github.naraks.myappserver.dto.session.SessionRequestDTO;
import com.github.naraks.myappserver.entity.Answer;
import com.github.naraks.myappserver.entity.Question;
import com.github.naraks.myappserver.entity.SelectedAnswer;
import com.github.naraks.myappserver.entity.Session;
import com.github.naraks.myappserver.repository.AnswerRepository;
import com.github.naraks.myappserver.repository.QuestionRepository;
import com.github.naraks.myappserver.repository.SelectedAnswerRepository;
import com.github.naraks.myappserver.repository.SessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SelectedAnswerRepository selectedAnswerRepository;

    public SessionServiceImpl(SessionRepository sessionRepository,
                              QuestionRepository questionRepository,
                              AnswerRepository answerRepository,
                              SelectedAnswerRepository selectedAnswerRepository) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.selectedAnswerRepository = selectedAnswerRepository;
    }

    @Override
    public List<SessionItemDTO> getSessions(JournalRowsRequestDTO request) {
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
    public List<QuestionItemDTO> getQuestionsForSession() {
        return questionRepository.findAll()
                .stream()
                .map(question -> new QuestionItemDTO(question, answerRepository.findByQuestion(question)))
                .collect(Collectors.toList());
    }

    @Override
    public Session save(SessionRequestDTO sessionRequestDTO) {
        Session session = new Session();
        session.setFullName(sessionRequestDTO.getName());
        session.setPercent(calculateTotalPercent(sessionRequestDTO));
        sessionRepository.save(session);
        for (AnsweredQuestionDTO questionDTO : sessionRequestDTO.getQuestionsList()) {
            for (SessionQuestionAnswerDTO answerDTO : questionDTO.getAnswersList()) {
                if (answerDTO.getIsSelected()) {
                    SelectedAnswer answer = new SelectedAnswer();
                    answer.setSession(session);
                    answer.setAnswer(answerRepository.findById(Long.parseLong(answerDTO.getId()))
                            .orElseThrow(() -> new RuntimeException(String.format("Answer with id %s not found", answerDTO.getId()))));
                    selectedAnswerRepository.save(answer);
                }
            }
        }
        return session;
    }

    @Override
    public Double calculateTotalPercent(SessionRequestDTO sessionRequestDTO) {
        double totalPoints = 0d;
        for (AnsweredQuestionDTO answeredQuestionDTO : sessionRequestDTO.getQuestionsList()) {
            Question question = questionRepository.findById(Long.parseLong(answeredQuestionDTO.getId()))
                    .orElseThrow(() -> new RuntimeException(String.format("Question with id %s not found", answeredQuestionDTO.getId())));
            List<Answer> answers = answerRepository.findByQuestion(question);
            // 1. Есть только один правильный ответ
            if (answers.stream().filter(Answer::getIsCorrect).count() == 1) {
                if (getCheckedAnswers(answeredQuestionDTO).size() == 1
                        && isAnswerSelected(getCheckedAnswers(answeredQuestionDTO).get(0))
                        && isAnswerCorrect(getCheckedAnswers(answeredQuestionDTO).get(0))) {
                    totalPoints += 1;
                }
            } else { // 2. Несколько правильных ответов
                double totalAnswers = answers.size();
                double correctAnswers = Math.toIntExact(answers.stream().filter(Answer::getIsCorrect).count());
                double correctUserAnswers = 0;
                double wrongUserAnswers = 0;

                for (SessionQuestionAnswerDTO answerDTO : answeredQuestionDTO.getAnswersList()) {
                    if (isAnswerSelected(answerDTO) && isAnswerCorrect(answerDTO)) {
                        correctUserAnswers++;
                    } else {
                        if (!isAnswerSelected(answerDTO) && isAnswerCorrect(answerDTO)
                                || isAnswerSelected(answerDTO) && !isAnswerCorrect(answerDTO)) {
                            wrongUserAnswers++;
                        }
                    }
                }
                totalPoints += calculateByFormula(totalAnswers, correctAnswers, correctUserAnswers, wrongUserAnswers);
            }
        }
        if (sessionRequestDTO.getQuestionsList().size() == 0) {
            return 100d;
        } else {
            return totalPoints / sessionRequestDTO.getQuestionsList().size() * 100;
        }
    }

    private double calculateByFormula(double totalAnswers,
                                      double correctAnswers,
                                      double correctUserAnswers,
                                      double wrongUserAnswers) {
        if (correctAnswers == 0 && totalAnswers != 0) {
            return 0;
        } else if (correctAnswers == 0 && totalAnswers == 0) {
            return 1;
        } else if (totalAnswers == correctAnswers && correctAnswers == correctUserAnswers) {
            return 1;
        } else if (totalAnswers == correctAnswers && correctAnswers != correctUserAnswers) {
            return Math.max(0, correctUserAnswers / correctAnswers);
        } else {
            return Math.max(0, correctUserAnswers / correctAnswers - wrongUserAnswers / (totalAnswers - correctAnswers));
        }
    }

    private boolean isAnswerSelected(SessionQuestionAnswerDTO answerDTO) {
        return answerDTO.getIsSelected();
    }

    private boolean isAnswerCorrect(SessionQuestionAnswerDTO answerDTO) {
        String id = answerDTO.getId();
        return answerRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException(String.format("Answer with id %s not found", id)))
                .getIsCorrect();
    }

    private List<SessionQuestionAnswerDTO> getCheckedAnswers(AnsweredQuestionDTO questionDTO) {
        List<SessionQuestionAnswerDTO> checkedAnswers = new ArrayList<>();
        for (SessionQuestionAnswerDTO answerDTO : questionDTO.getAnswersList()) {
            if (answerDTO.getIsSelected()) {
                checkedAnswers.add(answerDTO);
            }
        }
        return checkedAnswers;
    }
}

