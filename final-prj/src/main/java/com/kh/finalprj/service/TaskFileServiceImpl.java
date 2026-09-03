package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.TaskCommentsDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dao.TaskFileDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.TaskCommentFileDto;
import com.kh.finalprj.dto.TaskFileDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

@Service
public class TaskFileServiceImpl implements TaskFileService {

    @Autowired
    private TaskFileDao taskFileDao;

    @Autowired
    private AttachService attachService;

    @Autowired
    private AttachDao attachDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private TaskCommentsDao taskCommentsDao;

    @Override
    @Transactional
    public TaskFileResponseVO uploadTaskFile(int taskNo, int projectNo, String uploader, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        String validUploader = (uploader != null && !uploader.isBlank()) ? uploader : "SYSTEM";
        int validProjectNo = projectNo;
        if (validProjectNo <= 0) {
            TaskDetailResponseVO task = taskDao.selectOne(taskNo);
            if (task != null) validProjectNo = task.getProjectNo();
        }
        int attachNo = attachService.save(validProjectNo, file, validUploader, "TASK");
        taskFileDao.addTaskFile(TaskFileDto.builder().taskNo(taskNo).attachNo(attachNo).build());
        return TaskFileResponseVO.builder().attachNo(attachNo).attachName(file.getOriginalFilename()).attachType(file.getContentType()).attachSize(file.getSize()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskFileResponseVO> getTaskFiles(int taskNo) {
        return taskFileDao.selectFilesByTaskNo(taskNo);
    }

    @Override
    @Transactional
    public boolean removeTaskFile(int taskNo, int attachNo, String uploader) {
        boolean deleted = taskFileDao.deleteTaskFile(taskNo, attachNo);
        if (deleted) {
            AttachDto attachDto = attachDao.selectOne(attachNo);
            String targetUploader = (attachDto != null && attachDto.getAttachUploader() != null) ? attachDto.getAttachUploader() : uploader;
            attachService.delete(attachNo, targetUploader);
        }
        return deleted;
    }

    @Override
    @Transactional
    public TaskFileResponseVO uploadCommentFile(int taskCommentNo, int projectNo, String uploader, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        String validUploader = (uploader != null && !uploader.isBlank()) ? uploader : "SYSTEM";
        int validProjectNo = projectNo;
        if (validProjectNo <= 0) {
            TaskCommentDetailResponseVO comment = taskCommentsDao.selectOne(taskCommentNo);
            if (comment != null) {
                TaskDetailResponseVO task = taskDao.selectOne(comment.getTaskNo());
                if (task != null) validProjectNo = task.getProjectNo();
            }
        }
        int attachNo = attachService.save(validProjectNo, file, validUploader, "TASK_COMMENT");
        taskFileDao.addCommentFile(TaskCommentFileDto.builder().taskCommentNo(taskCommentNo).attachNo(attachNo).build());
        return TaskFileResponseVO.builder().attachNo(attachNo).attachName(file.getOriginalFilename()).attachType(file.getContentType()).attachSize(file.getSize()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskFileResponseVO> getCommentFiles(int taskCommentNo) {
        return taskFileDao.selectFilesByCommentNo(taskCommentNo);
    }

    @Override
    @Transactional
    public boolean removeCommentFile(int taskCommentNo, int attachNo, String uploader) {
        boolean deleted = taskFileDao.deleteCommentFile(taskCommentNo, attachNo);
        if (deleted) {
            AttachDto attachDto = attachDao.selectOne(attachNo);
            String targetUploader = (attachDto != null && attachDto.getAttachUploader() != null) ? attachDto.getAttachUploader() : uploader;
            attachService.delete(attachNo, targetUploader);
        }
        return deleted;
    }
}