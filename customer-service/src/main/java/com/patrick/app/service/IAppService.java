package com.patrick.app.service;

/**
 * 
 * @author Patrick Pan
 *
 */
public interface IAppService {

	int N = 5;

	/**
	 * Return top n questions with highest correlations and the greatest number of
	 * being selected.
	 * 
	 * @param input
	 * @return
	 */
	String[] getTopNQuestions(String input);

	String getAnswerByQuestion(String question);

	void increaseCountOfQuestion(String question);
}
