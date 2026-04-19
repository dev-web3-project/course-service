package com.lms.course.controller;

import com.lms.course.dto.CourseUpdateDto;
import com.lms.course.dto.ModuleUpdateDto;
import com.lms.course.entity.Course;
import com.lms.course.entity.CModule;
import com.lms.course.exception.ConflictException;
import com.lms.course.exception.NotFoundException;
import com.lms.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/course")
@Tag(name = "Cours & Modules", description = "Gestion des cours, modules, matériels, notes, quiz et forums")
public class CourseController {
    private final CourseService courseService;

    // ======================== COURS ========================

    @Operation(summary = "Lister tous les cours", description = "Retourne la liste complète des cours disponibles.")
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve courses", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtenir le nom d'un cours", description = "Retourne uniquement le nom d'un cours par son ID.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/name/{courseId}")
    public ResponseEntity<?> getCourseNameById(@PathVariable Long courseId) {
        try {
            String courseName = courseService.getCourseNameById(courseId);
            return new ResponseEntity<>(courseName, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get course name", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtenir un cours par ID", description = "Retourne les détails complets d'un cours.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get course", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Créer un cours", description = "Crée un nouveau cours dans le système.")
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Course createdCourse = courseService.createCourse(course);
            return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
        } catch (ConflictException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create course", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Mettre à jour un cours", description = "Met à jour les informations d'un cours existant.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDto courseDetails) {
        try {
            return new ResponseEntity<>(courseService.updateCourse(id, courseDetails), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update course", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Supprimer un cours", description = "Supprime un cours par son ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.deleteCourse(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete course", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== MODULES ========================

    @Operation(summary = "Lister tous les modules", description = "Retourne la liste complète des modules pédagogiques.")
    @GetMapping("/module")
    public ResponseEntity<?> getAllModules() {
        try {
            List<CModule> CModules = courseService.getModules();
            return new ResponseEntity<>(CModules, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve modules", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Modules par enseignant", description = "Retourne les modules assignés à un enseignant identifié par son username.")
    @GetMapping("/module/lecturer/{username}")
    public ResponseEntity<?> getModulesByLecturer(@PathVariable String username) {
        try {
            List<CModule> modules = courseService.getModulesByLecturer(username);
            return new ResponseEntity<>(modules, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get modules for lecturer", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtenir un module par ID", description = "Retourne les détails complets d'un module.")
    @GetMapping("/module/{id}")
    public ResponseEntity<?> getModuleById(@PathVariable Long id) {
        try {
            CModule module = courseService.getModuleById(id);
            return new ResponseEntity<>(module, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get module", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Créer un module", description = "Crée un nouveau module pédagogique.")
    @PostMapping("/module")
    public ResponseEntity<?> createModule(@RequestBody CModule module) {
        try {
            CModule createdModule = courseService.createModule(module);
            return new ResponseEntity<>(createdModule, HttpStatus.CREATED);
        } catch (ConflictException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create module", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Mettre à jour un module", description = "Met à jour les informations d'un module existant.")
    @PutMapping("/module/{id}")
    public ResponseEntity<?> updateModule(@PathVariable Long id, @RequestBody ModuleUpdateDto moduleUpdateDto) {
        try {
            return new ResponseEntity<>(courseService.updateModule(id, moduleUpdateDto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update module", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Supprimer un module", description = "Supprime un module par son ID.")
    @DeleteMapping("/module/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.deleteModule(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete module", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Cours par module", description = "Retourne la liste des cours assignés à un module.")
    @GetMapping("/course/module/{moduleId}")
    public ResponseEntity<?> getCoursesByModuleId(@PathVariable Long moduleId) {
        try {
            List<Course> courses = courseService.getCoursesByModuleId(moduleId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get courses", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Cours non assignés", description = "Retourne les cours qui ne sont assignés à aucun département.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned")
    public ResponseEntity<?> getCoursesWithoutAssigned() {
        try {
            List<Course> courses = courseService.getCoursesWithoutAssigned();
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get courses", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Cours par département", description = "Retourne les cours assignés à un département spécifique.")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getCoursesByDepartmentId(@PathVariable Long departmentId) {
        try {
            List<Course> courses = courseService.getCoursesByDepartmentId(departmentId);
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get courses", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Désassigner un cours d'un département", description = "Retire l'assignation d'un cours à son département.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/unassign/{courseId}/department")
    public ResponseEntity<?> unassignCourseFromDepartment(@PathVariable Long courseId) {
        try {
            return new ResponseEntity<>(courseService.unassignCourseFromDepartment(courseId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to unassign course from department", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== MATÉRIELS ========================

    @Operation(summary = "Matériels par module", description = "Retourne la liste des supports de cours d'un module.")
    @GetMapping("/material/module/{moduleId}")
    public ResponseEntity<?> getMaterialsByModuleId(@PathVariable Long moduleId) {
        try {
            List<com.lms.course.entity.Material> materials = courseService.getMaterialsByModuleId(moduleId);
            return new ResponseEntity<>(materials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get materials", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Ajouter un matériel", description = "Ajoute un nouveau support de cours (PDF, lien, etc.) à un module.")
    @PostMapping("/material")
    public ResponseEntity<?> createMaterial(@RequestBody com.lms.course.dto.MaterialRequestDto dto) {
        try {
            com.lms.course.entity.Material material = courseService.createMaterial(dto);
            return new ResponseEntity<>(material, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create material", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Supprimer un matériel", description = "Supprime un support de cours par son ID.")
    @DeleteMapping("/material/{id}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.deleteMaterial(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete material", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== NOTES ========================

    @Operation(summary = "Notes d'un étudiant", description = "Retourne toutes les notes d'un étudiant.")
    @GetMapping("/grade/student/{studentId}")
    public ResponseEntity<?> getGradesByStudentId(@PathVariable Long studentId) {
        try {
            return new ResponseEntity<>(courseService.getGradesByStudentId(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get grades", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Enregistrer une note", description = "Enregistre une nouvelle note pour un étudiant dans un cours.")
    @PostMapping("/grade")
    public ResponseEntity<?> saveGrade(@RequestBody com.lms.course.entity.Grade grade) {
        try {
            return new ResponseEntity<>(courseService.saveGrade(grade), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save grade", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== QUIZ ========================

    @Operation(summary = "Quiz par module", description = "Retourne la liste des quiz disponibles dans un module.")
    @GetMapping("/quiz/module/{moduleId}")
    public ResponseEntity<?> getQuizzesByModuleId(@PathVariable Long moduleId) {
        try {
            return new ResponseEntity<>(courseService.getQuizzesByModuleId(moduleId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get quizzes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtenir un quiz par ID", description = "Retourne les détails d'un quiz avec ses questions et options.")
    @GetMapping("/quiz/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.getQuizById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Créer un quiz", description = "Crée un nouveau quiz avec ses questions et options de réponse.")
    @PostMapping("/quiz")
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<?> createQuiz(@RequestBody com.lms.course.dto.QuizCreateDto dto) {
        try {
            return new ResponseEntity<>(courseService.createQuizFromDto(dto), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create quiz: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Modifier un quiz", description = "Met à jour un quiz existant (questions, options, etc.).")
    @PutMapping("/quiz/{id}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @RequestBody com.lms.course.dto.QuizCreateDto dto) {
        try {
            return new ResponseEntity<>(courseService.updateQuiz(id, dto), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Supprimer un quiz", description = "Supprime un quiz et toutes ses questions associées.")
    @DeleteMapping("/quiz/{id}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.deleteQuiz(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete quiz", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== RÉSULTATS QUIZ ========================

    @Operation(summary = "Soumettre un quiz", description = "Soumet les réponses d'un étudiant à un quiz et calcule le score automatiquement.")
    @PostMapping("/quiz/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitQuiz(@RequestBody com.lms.course.dto.QuizSubmissionDto dto) {
        try {
            return new ResponseEntity<>(courseService.submitQuiz(dto), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to submit quiz: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Enregistrer un résultat de quiz", description = "Sauvegarde manuellement un résultat de quiz pour un étudiant.")
    @PostMapping("/quiz/result")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> saveQuizResult(@RequestBody com.lms.course.entity.QuizResult result) {
        try {
            return new ResponseEntity<>(courseService.saveQuizResult(result), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save quiz result", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Résultats quiz d'un étudiant", description = "Retourne tous les résultats de quiz d'un étudiant.")
    @GetMapping("/quiz/result/student/{studentId}")
    public ResponseEntity<?> getQuizResultsByStudentId(@PathVariable String studentId) {
        try {
            return new ResponseEntity<>(courseService.getQuizResultsByStudentId(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get student quiz results", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Résultats d'un quiz", description = "Retourne tous les résultats des étudiants pour un quiz donné.")
    @GetMapping("/quiz/result/quiz/{quizId}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<?> getQuizResultsByQuizId(@PathVariable Long quizId) {
        try {
            return new ResponseEntity<>(courseService.getQuizResultsByQuizId(quizId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get quiz results", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ======================== FORUM ========================

    @Operation(summary = "Fils de discussion par module", description = "Retourne tous les fils de discussion du forum d'un module.")
    @GetMapping("/forum/module/{moduleId}")
    public ResponseEntity<?> getForumThreadsByModuleId(@PathVariable Long moduleId) {
        try {
            return new ResponseEntity<>(courseService.getForumThreadsByModuleId(moduleId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get forum threads", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtenir un fil de discussion", description = "Retourne un fil de discussion avec tous ses posts.")
    @GetMapping("/forum/thread/{id}")
    public ResponseEntity<?> getForumThreadById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.getForumThreadById(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get forum thread", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Créer un fil de discussion", description = "Crée un nouveau fil de discussion dans le forum d'un module.")
    @PostMapping("/forum/thread")
    public ResponseEntity<?> createForumThread(@RequestBody com.lms.course.dto.ForumThreadCreateDto dto) {
        try {
            return new ResponseEntity<>(courseService.createForumThread(dto), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create forum thread: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Répondre dans un fil", description = "Ajoute un post (réponse) à un fil de discussion existant.")
    @PostMapping("/forum/post")
    public ResponseEntity<?> createForumPost(@RequestBody com.lms.course.dto.ForumPostCreateDto dto) {
        try {
            return new ResponseEntity<>(courseService.createForumPost(dto), HttpStatus.CREATED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create forum post: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Voter pour un fil", description = "Ajoute un vote positif (upvote) à un fil de discussion.")
    @PostMapping("/forum/thread/{id}/upvote/{userId}")
    public ResponseEntity<?> upvoteThread(@PathVariable Long id, @PathVariable String userId) {
        try {
            courseService.upvoteThread(id, userId);
            return new ResponseEntity<>("Thread upvoted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upvote thread", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Retirer le vote d'un fil", description = "Retire un vote positif d'un fil de discussion.")
    @DeleteMapping("/forum/thread/{id}/upvote/{userId}")
    public ResponseEntity<?> removeUpvoteThread(@PathVariable Long id, @PathVariable String userId) {
        try {
            courseService.removeUpvoteThread(id, userId);
            return new ResponseEntity<>("Upvote removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove upvote", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Voter pour un post", description = "Ajoute un vote positif à un post de forum.")
    @PostMapping("/forum/post/{id}/upvote/{userId}")
    public ResponseEntity<?> upvotePost(@PathVariable Long id, @PathVariable String userId) {
        try {
            courseService.upvotePost(id, userId);
            return new ResponseEntity<>("Post upvoted", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upvote post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Retirer le vote d'un post", description = "Retire un vote positif d'un post de forum.")
    @DeleteMapping("/forum/post/{id}/upvote/{userId}")
    public ResponseEntity<?> removeUpvotePost(@PathVariable Long id, @PathVariable String userId) {
        try {
            courseService.removeUpvotePost(id, userId);
            return new ResponseEntity<>("Upvote removed", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to remove upvote", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Marquer un post comme solution", description = "Permet à un enseignant de marquer un post comme la solution d'un fil de discussion.")
    @PutMapping("/forum/thread/{threadId}/solution/{postId}")
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<?> markPostAsSolution(@PathVariable Long threadId, @PathVariable Long postId) {
        try {
            courseService.markPostAsSolution(postId, threadId);
            return new ResponseEntity<>("Post marked as solution", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConflictException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to mark post as solution", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
