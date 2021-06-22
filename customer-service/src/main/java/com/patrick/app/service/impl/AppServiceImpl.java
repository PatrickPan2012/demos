package com.patrick.app.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patrick.app.domain.QuestionAndAnswer;
import com.patrick.app.domain.QuestionAndAnswer.Correlation;
import com.patrick.app.service.IAppService;
import com.patrick.app.service.ITokenizerService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Patrick Pan
 *
 */
@Slf4j
@Service
public class AppServiceImpl implements IAppService {

	@Autowired
	private ITokenizerService tokenizerService;
	private Map<String, QuestionAndAnswer> map; // question:QuestionAndAnswer

	public AppServiceImpl() {
		map = new HashMap<>();
		QuestionAndAnswer[] questionAndAnswers = QuestionAndAnswer.values();
		for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
			String question = questionAndAnswer.getQuestion();
			map.put(question, questionAndAnswer);
		}
	}

	@Override
	public String[] getTopNQuestions(@NonNull String input) {
		List<String> wordsOfInput = tokenizerService.processSentence(input.trim());
		List<Correlation> correlations = new ArrayList<>();
		QuestionAndAnswer[] questionAndAnswers = QuestionAndAnswer.values();
		for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
			Correlation correlation = questionAndAnswer.computeCorrelation(wordsOfInput);
			correlations.add(correlation);
		}
		Collections.sort(correlations);

		String[] questions = new String[N];
		for (int i = 0; i < N && i < questionAndAnswers.length; i++) {
			Correlation correlation = correlations.get(i);
			questions[i] = correlation.getQuestion();

			if (log.isDebugEnabled()) {
				log.debug("=== The NO.{} question is \"{}\" and it hits {} words and is selected for {} times. ===",
						i + 1, questions[i], correlation.getHits(), correlation.getCount());
			}
		}

		return questions;
	}

	@Override
	public String getAnswerByQuestion(@NonNull String question) {
		QuestionAndAnswer questionAndAnswer = map.get(question.trim());
		return questionAndAnswer == null ? "" : questionAndAnswer.getAnswer();
	}

	@Override
	public void increaseCountOfQuestion(@NonNull String question) {
		QuestionAndAnswer questionAndAnswer = map.get(question.trim());
		if (questionAndAnswer != null) {
			questionAndAnswer.increaseCount();
		}
	}
}
