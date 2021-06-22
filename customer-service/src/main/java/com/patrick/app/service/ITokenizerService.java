package com.patrick.app.service;

import java.util.List;

/**
 * 
 * @author Patrick Pan
 *
 */
public interface ITokenizerService {

	/**
	 * Process user input. For example, if sentence is "开户需要什么手续", the list
	 * ["开户","需要","什么","手续"] will be returned.
	 * 
	 * @param sentence
	 * @return
	 */
	List<String> processSentence(String sentence);
}
