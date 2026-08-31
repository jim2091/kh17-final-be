package com.finalprj.email;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.RandomService;

@SpringBootTest
public class Test02회원생성 {
	
	@Autowired
	private EmpDao empDao;
	
	@Autowired
	private RandomService randomService;
	
	@Test
	public void test() {
		Random r = new Random();
		List<String> addrList = List.of(
				"김민수","이서준","박지훈","최현우","정우진",
				"강민재","조현준","윤서연","장예은","임수빈",
				"한지민","오하늘","서지우","신예린","권도윤",
				"황지호","안서현","송민준","류하준","전유진",
				"홍지민","문서준","양수아","배지훈","백승현",
				"허준영","남지우","노윤서","심민재","유하진",
				"김도현","이준호","박서연","최유진","정민재",
				"강서준","조은우","윤지후","장민서","임채원",
				"김서현","이도현","박민준","최서윤","정현우",
				"강지훈","조민서","윤민재","장도윤","임서준"
				);
		
		for(int i=1; i<= 1000; i++) {
			empDao.insert(EmpDto.builder()
						.empEmail("dummy"+i+"@kh.com")
						.empName(addrList.get(r.nextInt(addrList.size())))
					.build());
		}
		
		
		
	}

}
