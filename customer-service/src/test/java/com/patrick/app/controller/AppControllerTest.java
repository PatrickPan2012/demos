package com.patrick.app.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patrick.CustomerServiceApplication;
import com.patrick.app.domain.QuestionAndAnswer;

/**
 * 
 * @author Patrick Pan
 *
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = CustomerServiceApplication.class, webEnvironment = WebEnvironment.NONE)
class AppControllerTest {

	@Autowired
	private AppController appController;

	@Test
	@Order(1)
	public void testGetQuestions() {
		String[] expected = new String[] { "如何办理开户手续", "开户所需身份证明文件包括什么", "融资融券开户条件", "LOF基金", "分级基金" };
		String[] actual = appController.getQuestions("开户需要什么手续");
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testGetAnswerByQuestion(@Autowired ObjectMapper objectMapper) throws JsonProcessingException {
		Map<String, String> map = new HashMap<>();
		map.put("answer", QuestionAndAnswer._1st.getAnswer());
		String expected = objectMapper.writeValueAsString(map);
		String actual = appController.getAnswerByQuestion(QuestionAndAnswer._1st.getQuestion());
		assertEquals(expected, actual);
	}

	@Test
	public void testIncreaseCountOfQuestion() {
		QuestionAndAnswer selectedQuestionAndAnswer = QuestionAndAnswer._2nd;
		appController.increaseCountOfQuestion(selectedQuestionAndAnswer.getQuestion());

		QuestionAndAnswer[] questionAndAnswers = QuestionAndAnswer.values();
		for (QuestionAndAnswer questionAndAnswer : questionAndAnswers) {
			int expected = 0;
			if (questionAndAnswer == selectedQuestionAndAnswer) {
				expected = 1;
			}
			assertEquals(expected, questionAndAnswer.getCount());
		}
	}
}
