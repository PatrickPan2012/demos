package com.patrick.app.service.impl.jieba;

import java.util.List;

import org.springframework.stereotype.Service;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.patrick.app.domain.QuestionAndAnswer;
import com.patrick.app.service.ITokenizerService;

import lombok.NonNull;

@Service
public class TokenizerServiceImpl implements ITokenizerService {

	private JiebaSegmenter segmenter;

	public TokenizerServiceImpl() {
		segmenter = new JiebaSegmenter();

		// process all questions
		QuestionAndAnswer[] questionAndAnswers = QuestionAndAnswer.values();
		for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
			questionAndAnswer.setWords(segmenter.sentenceProcess(questionAndAnswer.getQuestion()));
		}
	}

	@Override
	public List<String> processSentence(@NonNull String sentence) {
		return segmenter.sentenceProcess(sentence);
	}
}
