package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

public interface TaskFileService {
	//업무 본체 첨부파일
    TaskFileResponseVO uploadTaskFile(int taskNo, String fileRole, MultipartFile file) throws IOException;
    List<TaskFileResponseVO> getTaskFiles(int taskNo);
    boolean removeTaskFile(int taskNo, int attachNo);
    
    //댓글 첨부파일
    TaskFileResponseVO uploadCommentFile(int taskCommentNo, String fileRole, MultipartFile file) throws IOException;
    List<TaskFileResponseVO> getCommentFiles(int taskCommentNo);
    boolean removeCommentFile(int taskCommentNo, int attachNo);
}