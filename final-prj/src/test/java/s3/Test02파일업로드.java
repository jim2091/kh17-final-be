package s3;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
public class Test02파일업로드 {
	//목표
	//- AWS의 SSO(Signle Sign-On, 통합로그인) 기능을 이용하여 S3에 파일 업로드
	//- 코드에서는 로그인을 하지 않으며, aws cli 환경에서 sso로그인을 한 상태여야함
	//- AWS에서 제공해주는 SDK관련 의존성이 필요
	//		- Spring(또는 Maven)에서 공식적으로 제공해주는 라이브러리가 아님
	//		- BOM(Bill Of Materials)을 설치하여 라이브러리를 가져올 수 있도록 알려줘야함
	@Test
	public void test() {
		//[1] AWS S3 전용 클라이언트 생성 (=접속 도구)
		S3Client s3Client = S3Client.builder()
					.region(Region.of("ap-northeast-2"))
					//.region(Region.AP_NORTHEAST_2)
				.build();
		
		//[2] 업로드할 파일명과 내용을 준비
		//String objectKey = "uploads/test/"+UUID.randomUUID()+".txt";
		String objectKey = "uploads/test/dummy.txt";
		String content = "드디어 AWS S3 업로드가 완성되었습니다!!";
		
		//[3] 업로드 요청(PutObjectRequest)을 보낼 요청객체, 응답객체를 준비하여 실행
		PutObjectRequest request = PutObjectRequest.builder()
					.bucket("kh17-final-440977419814-ap-northeast-2-an")
					.key(objectKey)
					.contentType("text/plain; charset=UTF-8")
				.build();
		
		PutObjectResponse response = s3Client.putObject(
				request, 
				RequestBody.fromBytes(
					content.getBytes(StandardCharsets.UTF_8)
				)
		);
		
		//[4] 결과 확인
		log.debug("업로드 성공");
		log.debug("객체 키 = {}", objectKey);
		log.debug("ETag = {}", response.eTag());
		
	}
}