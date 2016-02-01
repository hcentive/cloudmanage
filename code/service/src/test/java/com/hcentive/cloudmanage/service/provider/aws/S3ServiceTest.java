package com.hcentive.cloudmanage.service.provider.aws;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:config/application-test.properties")
public class S3ServiceTest {

	@Autowired
	private S3Service s3Service;

	@Test
	public void testGetBill() {
		String artifactName = AppConfig.billFileName;
		artifactName = artifactName.replace("<year>", String.valueOf("2015"));
		artifactName = artifactName.replace("<month>", String.valueOf("01"));
		try {
			s3Service.getBill(AppConfig.billS3BucketName, artifactName,
					AppConfig.billBaseDir);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Not able to retrieve bill file.");
		}
	}

}
