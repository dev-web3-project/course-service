package com.lms.course.service.impl;

import com.lms.course.dto.CourseUpdateDto;
import com.lms.course.dto.ModuleUpdateDto;
import com.lms.course.entity.CModule;
import com.lms.course.entity.Course;
import com.lms.course.exception.ConflictException;
import com.lms.course.exception.NotFoundException;
import com.lms.course.repository.CourseRepository;
import com.lms.course.repository.ModuleRepository;
import com.lms.course.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private CourseRepository courseRepository;
    private ModuleRepository moduleRepository;
    private com.lms.course.repository.MaterialRepository materialRepository;
    private com.lms.course.repository.GradeRepository gradeRepository;
    private com.lms.course.repository.AssignmentRepository assignmentRepository;
    private com.lms.course.repository.SubmissionRepository submissionRepository;
    private com.lms.course.repository.AttendanceRepository attendanceRepository;
    private com.lms.course.repository.QuizRepository quizRepository;
    private com.lms.course.repository.QuizResultRepository quizResultRepository;
    private com.lms.course.repository.ForumThreadRepository forumThreadRepository;
    private com.lms.course.repository.ForumPostRepository forumPostRepository;
    private com.lms.course.repository.ForumUpvoteRepository forumUpvoteRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.web.client.RestTemplate restTemplate;

    public Course createCourse(Course course) {
        if (courseRepository.existsByTitle(course.getTitle())) {
            throw new RuntimeException("Course already exists");
        }
        return courseRepository.save(course);
    }

    public Course getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new NotFoundException("Faculty not found");
        }
        return course;
    }

    public Course updateCourse(Long id, CourseUpdateDto courseUpdateDto) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found with id " + id);
        }
        course.setTitle(courseUpdateDto.getTitle());
        course.setDescription(courseUpdateDto.getDescription());
        course.setLecturer(courseUpdateDto.getLecturer());
        course.setDuration(courseUpdateDto.getDuration());
        course.setLevel(courseUpdateDto.getLevel());
        course.setLanguage(courseUpdateDto.getLanguage());
        course.setFormat(courseUpdateDto.getFormat());
        course.setCredits(courseUpdateDto.getCredits());

        return courseRepository.save(course);
    }

    public String deleteCourse(Long id) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        courseRepository.delete(course);
        return "Faculty deleted successfully";
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return courses;
    }

    // get all modules
    public List<CModule> getModules() {
        List<CModule> modules = moduleRepository.findAll();
        if (modules.isEmpty()) {
            throw new NotFoundException("No modules found");
        }
        return modules;
    }

    // Get Module by id
    public CModule getModuleById(Long id) {
        CModule module = moduleRepository.findById(id).orElse(null);
        if(module == null) {
            throw new NotFoundException("Module not found");
        }
        return module;
    }

    public CModule createModule(CModule module) {
        if(moduleRepository.existsByTitle(module.getTitle())) {
            throw new ConflictException("Module already exists");
        }
        return moduleRepository.save(module);
    }

    public CModule updateModule(Long id, ModuleUpdateDto moduleUpdateDto) {
        CModule module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }
        module.setTitle(moduleUpdateDto.getTitle());
        module.setDescription(moduleUpdateDto.getDescription());
        module.setDuration(moduleUpdateDto.getDuration());
        module.setLevel(moduleUpdateDto.getLevel());
        module.setLanguage(moduleUpdateDto.getLanguage());
        module.setCredits(moduleUpdateDto.getCredits());
        module.setSemester(moduleUpdateDto.getSemester());
        if (moduleUpdateDto.getLecturer() != null) {
            module.setLecturer(moduleUpdateDto.getLecturer());
        }
        return moduleRepository.save(module);
    }

    public String deleteModule(Long id) {
        CModule module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }
        moduleRepository.delete(module);
        return "Module deleted successfully";
    }

    // get all courses by module id
    public List<Course> getCoursesByModuleId(Long moduleId) {
        return courseRepository.findByModuleId(moduleId);
    }

    public List<CModule> getModulesByCourseId(Long courseId) {
        return getModulesByCourseId(courseId, null);
    }

    public List<CModule> getModulesByCourseId(Long courseId, String level) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        
        // Strategy: If course has a department, return all modules of all courses in that department
        // IF a level is provided, filter those modules by level
        if (course.getDepartmentId() != null) {
            List<Course> deptCourses = courseRepository.findByDepartmentId(course.getDepartmentId());
            return deptCourses.stream()
                    .filter(c -> level == null || (c.getLevel() != null && c.getLevel().equalsIgnoreCase(level)))
                    .map(Course::getModule)
                    .filter(java.util.Objects::nonNull)
                    .filter(m -> level == null || (m.getLevel() != null && m.getLevel().equalsIgnoreCase(level)))
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // Fallback: return only the module of this course
        if (course.getModule() != null) {
            CModule m = course.getModule();
            if (level == null || (m.getLevel() != null && m.getLevel().equalsIgnoreCase(level))) {
                return java.util.List.of(m);
            }
        }
        
        return java.util.Collections.emptyList();
    }

    public List<CModule> getModulesByLecturer(String lecturerUsername) {
        return moduleRepository.findByLecturer(lecturerUsername);
    }

    // assign course to module
    public String assignCourseToModule(Long courseId, Long moduleId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        CModule module = moduleRepository.findById(moduleId).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }
        course.setModule(module);
        courseRepository.save(course);
        return "Course assigned to module successfully";
    }

    // list courses without assigned to module
    public List<Course> getCoursesWithoutAssignedToModule() {
        List<Course> courses = courseRepository.findByModuleIsNull();
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return courses;
    }

    // Unassign course from module
    public String unassignCourseFromModule(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        course.setModule(null);
        courseRepository.save(course);
        return "Course unassigned from module successfully";
    }

    public List<Course> getCoursesWithoutAssigned() {
        List<Course> courses = courseRepository.findByDepartmentIdIsNull();
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return courses;
    }

    public List<Course> getCoursesByDepartmentId(Long departmentId) {
        List<Course> courses = courseRepository.findByDepartmentId(departmentId);
        if (courses.isEmpty()) {
            throw new NotFoundException("No courses found");
        }
        return courses;
    }

    public String unassignCourseFromDepartment(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        course.setDepartmentId(null);
        courseRepository.save(course);
        return "Course unassigned from department successfully";
    }

    public String getCourseNameById(Long courseId) {
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            throw new NotFoundException("Course not found");
        }
        return course.getTitle();
    }

    public List<com.lms.course.entity.Material> getMaterialsByModuleId(Long moduleId) {
        return materialRepository.findByModuleId(moduleId);
    }

    public com.lms.course.entity.Material createMaterial(com.lms.course.dto.MaterialRequestDto dto) {
        CModule module = moduleRepository.findById(dto.getModuleId()).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module not found");
        }
        com.lms.course.entity.Material material = new com.lms.course.entity.Material();
        material.setTitle(dto.getTitle());
        material.setDescription(dto.getDescription());
        material.setType(dto.getType());
        material.setFileUrl(dto.getFileUrl());
        material.setModule(module);
        return materialRepository.save(material);
    }

    public String deleteMaterial(Long id) {
        com.lms.course.entity.Material material = materialRepository.findById(id).orElse(null);
        if (material == null) {
            throw new NotFoundException("Material not found");
        }
        materialRepository.delete(material);
        return "Material deleted successfully";
    }

    public List<com.lms.course.entity.Grade> getGradesByStudentId(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public List<com.lms.course.entity.Grade> getGradesByModuleId(Long moduleId) {
        return gradeRepository.findByModuleId(moduleId);
    }

    public com.lms.course.entity.Grade saveGrade(com.lms.course.entity.Grade grade) {
        return gradeRepository.save(grade);
    }

    public List<com.lms.course.entity.Assignment> getAssignmentsByModuleId(Long moduleId) {
        CModule module = moduleRepository.findById(moduleId).orElse(null);
        if (module == null) throw new NotFoundException("Module not found");
        return assignmentRepository.findByModule(module);
    }

    public com.lms.course.entity.Assignment createAssignment(com.lms.course.entity.Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public String deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
        return "Assignment deleted successfully";
    }

    public List<com.lms.course.entity.Submission> getSubmissionsByAssignmentId(Long assignmentId) {
        com.lms.course.entity.Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
        if (assignment == null) throw new NotFoundException("Assignment not found");
        return submissionRepository.findByAssignment(assignment);
    }

    public com.lms.course.entity.Submission submitAssignment(com.lms.course.entity.Submission submission) {
        return submissionRepository.save(submission);
    }

    public List<com.lms.course.entity.Submission> getSubmissionsByStudentId(String studentId) {
        return submissionRepository.findByStudentId(studentId);
    }

    public List<com.lms.course.entity.Attendance> getAttendanceByModuleAndDate(Long moduleId, java.time.LocalDate date) {
        CModule module = moduleRepository.findById(moduleId).orElse(null);
        if (module == null) throw new NotFoundException("Module not found");
        return attendanceRepository.findByModuleAndDate(module, date);
    }

    public com.lms.course.entity.Attendance saveAttendance(com.lms.course.entity.Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public List<com.lms.course.entity.Attendance> getAttendanceByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<com.lms.course.entity.Quiz> getQuizzesByModuleId(Long moduleId) {
        return quizRepository.findByModuleId(moduleId);
    }

    public com.lms.course.entity.Quiz createQuiz(com.lms.course.entity.Quiz quiz) {
        // Link questions and options to the parent
        if (quiz.getQuestions() != null) {
            quiz.getQuestions().forEach(q -> {
                q.setQuiz(quiz);
                if (q.getOptions() != null) {
                    q.getOptions().forEach(o -> o.setQuestion(q));
                }
            });
        }
        return quizRepository.save(quiz);
    }

    public com.lms.course.entity.Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new NotFoundException("Quiz not found"));
    }

    public com.lms.course.entity.QuizResult saveQuizResult(com.lms.course.entity.QuizResult result) {
        return quizResultRepository.save(result);
    }

    public List<com.lms.course.entity.QuizResult> getQuizResultsByStudentId(String studentId) {
        return quizResultRepository.findByStudentId(studentId);
    }

    public com.lms.course.entity.Quiz createQuizFromDto(com.lms.course.dto.QuizCreateDto dto) {
        CModule module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));

        com.lms.course.entity.Quiz quiz = new com.lms.course.entity.Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setTimeLimit(dto.getTimeLimit());
        quiz.setTimeLimitPerQuestion(dto.getTimeLimitPerQuestion());
        quiz.setDocumentMode(dto.getDocumentMode());
        quiz.setCameraSurveillanceEnabled(dto.getCameraSurveillanceEnabled());
        quiz.setRandomizeQuestions(dto.getRandomizeQuestions());
        quiz.setRandomizeAnswers(dto.getRandomizeAnswers());
        quiz.setMaxAttempts(dto.getMaxAttempts());
        quiz.setPublishDate(dto.getPublishDate());
        quiz.setDueDate(dto.getDueDate());
        quiz.setIsDraft(dto.getIsDraft());
        quiz.setPassingScore(dto.getPassingScore());
        quiz.setModule(module);

        if (dto.getQuestions() != null) {
            List<com.lms.course.entity.Question> questions = dto.getQuestions().stream().map(qDto -> {
                com.lms.course.entity.Question question = new com.lms.course.entity.Question();
                question.setText(qDto.getText());
                question.setHint(qDto.getHint());
                question.setOrderIndex(qDto.getOrderIndex());
                question.setPoints(qDto.getPoints());
                question.setType(qDto.getType());
                question.setQuiz(quiz);

                if (qDto.getOptions() != null) {
                    List<com.lms.course.entity.QuestionOption> options = qDto.getOptions().stream().map(oDto -> {
                        com.lms.course.entity.QuestionOption option = new com.lms.course.entity.QuestionOption();
                        option.setText(oDto.getText());
                        option.setCorrect(oDto.getCorrect());
                        option.setQuestion(question);
                        return option;
                    }).collect(java.util.stream.Collectors.toList());
                    question.setOptions(options);
                }

                return question;
            }).collect(java.util.stream.Collectors.toList());
            quiz.setQuestions(questions);
        }

        return quizRepository.save(quiz);
    }

    public com.lms.course.entity.Quiz updateQuiz(Long id, com.lms.course.dto.QuizCreateDto dto) {
        com.lms.course.entity.Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setTimeLimit(dto.getTimeLimit());
        quiz.setTimeLimitPerQuestion(dto.getTimeLimitPerQuestion());
        quiz.setDocumentMode(dto.getDocumentMode());
        quiz.setCameraSurveillanceEnabled(dto.getCameraSurveillanceEnabled());
        quiz.setRandomizeQuestions(dto.getRandomizeQuestions());
        quiz.setRandomizeAnswers(dto.getRandomizeAnswers());
        quiz.setMaxAttempts(dto.getMaxAttempts());
        quiz.setPublishDate(dto.getPublishDate());
        quiz.setDueDate(dto.getDueDate());
        quiz.setIsDraft(dto.getIsDraft());
        quiz.setPassingScore(dto.getPassingScore());

        return quizRepository.save(quiz);
    }

    public String deleteQuiz(Long id) {
        com.lms.course.entity.Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        quizRepository.delete(quiz);
        return "Quiz deleted successfully";
    }

    public com.lms.course.entity.QuizResult submitQuiz(com.lms.course.dto.QuizSubmissionDto dto) {
        com.lms.course.entity.Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new NotFoundException("Quiz not found"));

        // Check max attempts
        long attemptCount = quizResultRepository.countByQuizAndStudentId(quiz, dto.getStudentId());
        if (quiz.getMaxAttempts() != null && attemptCount >= quiz.getMaxAttempts()) {
            throw new ConflictException("Max attempts reached");
        }

        com.lms.course.entity.QuizResult result = new com.lms.course.entity.QuizResult();
        result.setQuiz(quiz);
        result.setStudentId(dto.getStudentId());
        result.setAttemptNumber((int) (attemptCount + 1));
        result.setCompletedDate(java.time.LocalDateTime.now());

        // Process answers and calculate score
        int totalPoints = 0;
        int earnedPoints = 0;

        List<com.lms.course.entity.StudentAnswer> studentAnswers = new java.util.ArrayList<>();
        for (com.lms.course.dto.StudentAnswerDto answerDto : dto.getAnswers()) {
            com.lms.course.entity.Question question = quiz.getQuestions().stream()
                    .filter(q -> q.getId().equals(answerDto.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Question not found"));

            com.lms.course.entity.StudentAnswer studentAnswer = new com.lms.course.entity.StudentAnswer();
            studentAnswer.setQuizResult(result);
            studentAnswer.setQuestion(question);
            studentAnswer.setTextAnswer(answerDto.getTextAnswer());

            if (answerDto.getSelectedOptionId() != null) {
                com.lms.course.entity.QuestionOption selectedOption = question.getOptions().stream()
                        .filter(o -> o.getId().equals(answerDto.getSelectedOptionId()))
                        .findFirst()
                        .orElse(null);
                studentAnswer.setSelectedOption(selectedOption);
                studentAnswer.setIsCorrect(selectedOption != null && selectedOption.isCorrect());
            }

            studentAnswers.add(studentAnswer);

            if (question.getPoints() != null) {
                totalPoints += question.getPoints();
                if (Boolean.TRUE.equals(studentAnswer.getIsCorrect())) {
                    earnedPoints += question.getPoints();
                }
            }
        }

        result.setAnswers(studentAnswers);

        // Calculate score percentage
        if (totalPoints > 0) {
            double score = (double) earnedPoints / totalPoints * 100;
            result.setScore(score);
            result.setPassed(quiz.getPassingScore() != null && score >= quiz.getPassingScore());

            // Add Gamification XP
            try {
                if (result.getPassed() != null && result.getPassed()) {
                    restTemplate.postForObject("http://user-service/api/user/gamification/" + dto.getStudentId() + "/xp?amount=50", null, String.class);
                } else {
                    restTemplate.postForObject("http://user-service/api/user/gamification/" + dto.getStudentId() + "/xp?amount=10", null, String.class);
                }
            } catch (Exception e) {
                System.err.println("Failed to award XP: " + e.getMessage());
            }
        }

        // Handle integrity report
        if (dto.getIntegrityReport() != null) {
            com.lms.course.entity.IntegrityReport integrityReport = new com.lms.course.entity.IntegrityReport();
            integrityReport.setQuizResult(result);
            integrityReport.setMouseExitCount(dto.getIntegrityReport().getMouseExitCount());
            integrityReport.setTabSwitchCount(dto.getIntegrityReport().getTabSwitchCount());
            integrityReport.setAutoSubmitted(dto.getIntegrityReport().getAutoSubmitted());
            integrityReport.setSuspiciousVideoSegments(dto.getIntegrityReport().getSuspiciousVideoSegments());
            integrityReport.setNotes(dto.getIntegrityReport().getNotes());

            // Calculate integrity score (simple formula)
            int integrityScore = 100;
            if (dto.getIntegrityReport().getMouseExitCount() != null) {
                integrityScore -= dto.getIntegrityReport().getMouseExitCount() * 5;
            }
            if (dto.getIntegrityReport().getTabSwitchCount() != null) {
                integrityScore -= dto.getIntegrityReport().getTabSwitchCount() * 10;
            }
            if (Boolean.TRUE.equals(dto.getIntegrityReport().getAutoSubmitted())) {
                integrityScore -= 30;
            }
            integrityReport.setIntegrityScore(Math.max(0, integrityScore));

            result.setIntegrityReport(integrityReport);
        }

        return quizResultRepository.save(result);
    }

    public List<com.lms.course.entity.QuizResult> getQuizResultsByQuizId(Long quizId) {
        com.lms.course.entity.Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new NotFoundException("Quiz not found"));
        return quizResultRepository.findByQuiz(quiz);
    }

    // --- Forum Methods ---

    public List<com.lms.course.entity.ForumThread> getForumThreadsByModuleId(Long moduleId) {
        return forumThreadRepository.findByModuleId(moduleId);
    }

    public com.lms.course.entity.ForumThread getForumThreadById(Long id) {
        com.lms.course.entity.ForumThread thread = forumThreadRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Thread not found"));
        thread.setViewCount(thread.getViewCount() + 1);
        return forumThreadRepository.save(thread);
    }

    public com.lms.course.entity.ForumThread createForumThread(com.lms.course.dto.ForumThreadCreateDto dto) {
        CModule module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new NotFoundException("Module not found"));
        
        com.lms.course.entity.ForumThread thread = new com.lms.course.entity.ForumThread();
        thread.setTitle(dto.getTitle());
        thread.setContent(dto.getContent());
        thread.setAuthorId(dto.getAuthorId());
        thread.setAuthorName(dto.getAuthorName());
        thread.setModule(module);
        
        return forumThreadRepository.save(thread);
    }

    public com.lms.course.entity.ForumPost createForumPost(com.lms.course.dto.ForumPostCreateDto dto) {
        com.lms.course.entity.ForumThread thread = forumThreadRepository.findById(dto.getThreadId())
                .orElseThrow(() -> new NotFoundException("Thread not found"));
        
        com.lms.course.entity.ForumPost post = new com.lms.course.entity.ForumPost();
        post.setContent(dto.getContent());
        post.setAuthorId(dto.getAuthorId());
        post.setAuthorName(dto.getAuthorName());
        post.setThread(thread);
        
        if (dto.getParentPostId() != null) {
            com.lms.course.entity.ForumPost parentPost = forumPostRepository.findById(dto.getParentPostId())
                    .orElseThrow(() -> new NotFoundException("Parent post not found"));
            post.setParentPost(parentPost);
        }
        
        try {
            restTemplate.postForObject("http://user-service/api/user/gamification/" + dto.getAuthorId() + "/xp?amount=15", null, String.class);
        } catch (Exception e) {
            System.err.println("Failed to award forum XP: " + e.getMessage());
        }
        
        return forumPostRepository.save(post);
    }

    @org.springframework.transaction.annotation.Transactional
    public void upvoteThread(Long threadId, String userId) {
        com.lms.course.entity.ForumThread thread = forumThreadRepository.findById(threadId)
                .orElseThrow(() -> new NotFoundException("Thread not found"));
        
        forumUpvoteRepository.findByUserIdAndThreadId(userId, threadId)
                .ifPresentOrElse(
                        upvote -> {},
                        () -> {
                            com.lms.course.entity.ForumUpvote upvote = new com.lms.course.entity.ForumUpvote();
                            upvote.setUserId(userId);
                            upvote.setThread(thread);
                            forumUpvoteRepository.save(upvote);
                            thread.setUpvoteCount(thread.getUpvoteCount() + 1);
                            forumThreadRepository.save(thread);
                        }
                );
    }

    @org.springframework.transaction.annotation.Transactional
    public void upvotePost(Long postId, String userId) {
        com.lms.course.entity.ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        
        forumUpvoteRepository.findByUserIdAndPostId(userId, postId)
                .ifPresentOrElse(
                        upvote -> {},
                        () -> {
                            com.lms.course.entity.ForumUpvote upvote = new com.lms.course.entity.ForumUpvote();
                            upvote.setUserId(userId);
                            upvote.setPost(post);
                            forumUpvoteRepository.save(upvote);
                            post.setUpvoteCount(post.getUpvoteCount() + 1);
                            forumPostRepository.save(post);
                        }
                );
    }

    @org.springframework.transaction.annotation.Transactional
    public void removeUpvoteThread(Long threadId, String userId) {
        com.lms.course.entity.ForumThread thread = forumThreadRepository.findById(threadId)
                .orElseThrow(() -> new NotFoundException("Thread not found"));
        
        forumUpvoteRepository.findByUserIdAndThreadId(userId, threadId)
                .ifPresent(upvote -> {
                    forumUpvoteRepository.delete(upvote);
                    if (thread.getUpvoteCount() > 0) {
                        thread.setUpvoteCount(thread.getUpvoteCount() - 1);
                        forumThreadRepository.save(thread);
                    }
                });
    }

    @org.springframework.transaction.annotation.Transactional
    public void removeUpvotePost(Long postId, String userId) {
        com.lms.course.entity.ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        
        forumUpvoteRepository.findByUserIdAndPostId(userId, postId)
                .ifPresent(upvote -> {
                    forumUpvoteRepository.delete(upvote);
                    if (post.getUpvoteCount() > 0) {
                        post.setUpvoteCount(post.getUpvoteCount() - 1);
                        forumPostRepository.save(post);
                    }
                });
    }

    @org.springframework.transaction.annotation.Transactional
    public void markPostAsSolution(Long postId, Long threadId) {
        com.lms.course.entity.ForumThread thread = forumThreadRepository.findById(threadId)
                .orElseThrow(() -> new NotFoundException("Thread not found"));
        
        com.lms.course.entity.ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        
        if (!post.getThread().getId().equals(threadId)) {
            throw new ConflictException("Post does not belong to this thread");
        }
        
        if (thread.getSolution() != null) {
            thread.getSolution().setIsSolution(false);
            forumPostRepository.save(thread.getSolution());
        }
        
        post.setIsSolution(true);
        forumPostRepository.save(post);
        thread.setSolution(post);
        forumThreadRepository.save(thread);
    }
}
