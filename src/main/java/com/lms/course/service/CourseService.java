package com.lms.course.service;


import com.lms.course.dto.CourseUpdateDto;
import com.lms.course.dto.ModuleUpdateDto;
import com.lms.course.entity.CModule;
import com.lms.course.entity.Course;

import java.util.List;

public interface CourseService {
    Course createCourse(Course course);
    Course getCourseById(Long id);
    Course updateCourse(Long id, CourseUpdateDto courseUpdateDto);
    String deleteCourse(Long id);
    List<Course> getAllCourses();
    List<CModule> getModules();
    CModule getModuleById(Long id);
    CModule createModule(CModule module);
    CModule updateModule(Long id, ModuleUpdateDto moduleUpdateDto);
    String deleteModule(Long id);
    List<Course> getCoursesByModuleId(Long moduleId);
    List<CModule> getModulesByCourseId(Long courseId);
    List<CModule> getModulesByCourseId(Long courseId, String level);
    List<CModule> getModulesByLecturer(String lecturerUsername);
    String assignCourseToModule(Long courseId, Long moduleId);
    List<Course> getCoursesWithoutAssignedToModule();
    String unassignCourseFromModule(Long courseId);
    List<Course> getCoursesWithoutAssigned();
    List<Course> getCoursesByDepartmentId(Long departmentId);
    String unassignCourseFromDepartment(Long courseId);
    String getCourseNameById(Long courseId);
    
    // Material methods
    List<com.lms.course.entity.Material> getMaterialsByModuleId(Long moduleId);
    com.lms.course.entity.Material createMaterial(com.lms.course.dto.MaterialRequestDto materialRequestDto);
    String deleteMaterial(Long id);
    
    // Grade methods
    List<com.lms.course.entity.Grade> getGradesByStudentId(Long studentId);
    List<com.lms.course.entity.Grade> getGradesByModuleId(Long moduleId);
    com.lms.course.entity.Grade saveGrade(com.lms.course.entity.Grade grade);
    
    // Assignment methods
    List<com.lms.course.entity.Assignment> getAssignmentsByModuleId(Long moduleId);
    com.lms.course.entity.Assignment createAssignment(com.lms.course.entity.Assignment assignment);
    String deleteAssignment(Long id);
    
    // Submission methods
    List<com.lms.course.entity.Submission> getSubmissionsByAssignmentId(Long assignmentId);
    com.lms.course.entity.Submission submitAssignment(com.lms.course.entity.Submission submission);
    List<com.lms.course.entity.Submission> getSubmissionsByStudentId(String studentId);
    
    // Attendance methods
    List<com.lms.course.entity.Attendance> getAttendanceByModuleAndDate(Long moduleId, java.time.LocalDate date);
    com.lms.course.entity.Attendance saveAttendance(com.lms.course.entity.Attendance attendance);
    List<com.lms.course.entity.Attendance> getAttendanceByStudentId(String studentId);
    
    // Quiz methods
    List<com.lms.course.entity.Quiz> getQuizzesByModuleId(Long moduleId);
    com.lms.course.entity.Quiz createQuiz(com.lms.course.entity.Quiz quiz);
    com.lms.course.entity.Quiz createQuizFromDto(com.lms.course.dto.QuizCreateDto dto);
    com.lms.course.entity.Quiz getQuizById(Long id);
    com.lms.course.entity.Quiz updateQuiz(Long id, com.lms.course.dto.QuizCreateDto dto);
    String deleteQuiz(Long id);
    
    // Quiz Result methods
    com.lms.course.entity.QuizResult saveQuizResult(com.lms.course.entity.QuizResult result);
    List<com.lms.course.entity.QuizResult> getQuizResultsByStudentId(String studentId);
    com.lms.course.entity.QuizResult submitQuiz(com.lms.course.dto.QuizSubmissionDto dto);
    List<com.lms.course.entity.QuizResult> getQuizResultsByQuizId(Long quizId);

    // Forum methods
    List<com.lms.course.entity.ForumThread> getForumThreadsByModuleId(Long moduleId);
    com.lms.course.entity.ForumThread getForumThreadById(Long id);
    com.lms.course.entity.ForumThread createForumThread(com.lms.course.dto.ForumThreadCreateDto dto);
    com.lms.course.entity.ForumPost createForumPost(com.lms.course.dto.ForumPostCreateDto dto);
    void upvoteThread(Long threadId, String userId);
    void upvotePost(Long postId, String userId);
    void removeUpvoteThread(Long threadId, String userId);
    void removeUpvotePost(Long postId, String userId);
    void markPostAsSolution(Long postId, Long threadId);
}
