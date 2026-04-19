package com.lms.course.controller;

import com.lms.course.entity.Course;
import com.lms.course.exception.NotFoundException;
import com.lms.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/studentcourse")
@Tag(name = "Vue Étudiant", description = "Endpoints dédiés à la consultation des cours par les étudiants")
public class StudentCourseController {

    private final CourseService courseService;

    @Operation(summary = "Cours par module (vue étudiant)", description = "Retourne les cours d'un module accessibles par un étudiant.")
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

    @Operation(summary = "Modules par cours", description = "Retourne les modules associés à un cours, avec filtre optionnel par niveau.")
    @GetMapping("/module/{courseId}/course")
    public ResponseEntity<?> getModulesByCourseId(
            @PathVariable Long courseId,
            @RequestParam(required = false) String level) {
        try {
            return new ResponseEntity<>(courseService.getModulesByCourseId(courseId, level), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get modules", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
