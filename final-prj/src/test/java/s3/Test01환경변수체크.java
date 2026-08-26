package s3;

import org.junit.jupiter.api.Test;

public class Test01환경변수체크 {
	@Test
	public void test() {
		System.out.println("AWS_PROFILE = " + System.getenv("AWS_PROFILE"));
		System.out.println("AWS_REGION = " + System.getenv("AWS_REGION"));
	}
}
