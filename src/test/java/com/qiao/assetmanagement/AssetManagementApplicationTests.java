package com.qiao.assetmanagement;

import com.qiao.assetmanagement.assignment.AssignmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AssetManagementApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
    AssignmentRepository assignmentRepository;
	@Test
	void appStartsAndReadsSeedRow() {
		long count = assignmentRepository.count();
		System.out.println("Number of assignment records: " + count);
		assertEquals(1,count);

	}

}
