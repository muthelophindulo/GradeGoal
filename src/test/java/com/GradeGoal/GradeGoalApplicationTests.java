package com.GradeGoal;

import com.GradeGoal.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GradeGoalApplicationTests {

	@Test
	void contextLoads() {
		UserService userService = new UserService();
		System.out.println(userService.yearOfStudy("225004680"));
	}

}
