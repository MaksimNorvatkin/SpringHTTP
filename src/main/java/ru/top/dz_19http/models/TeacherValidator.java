package ru.top.dz_19http.models;

import ru.top.dz_19http.dto.TeacherDTO;

import java.util.List;

public class TeacherValidator {
    public static String validate(TeacherDTO teacherDTO, List<Teacher> teachers) {
        if (teacherDTO.getFirstName() == null || teacherDTO.getFirstName().trim().isEmpty()) {
            return "First name is required";
        }
        if (teacherDTO.getFirstName().length() < 2 || teacherDTO.getFirstName().length() > 50) {
            return "First name must be 2-50 characters and contain only letters";
        }
        if (teacherDTO.getLastName() == null || teacherDTO.getLastName().trim().isEmpty()) {
            return "Last name is required";
        }
        if (teacherDTO.getLastName().length() < 2 || teacherDTO.getLastName().length() > 50) {
            return "Last name must be 2-50 characters and contain only letters";
        }

        if (teacherDTO.getSubject() == null || teacherDTO.getSubject().trim().isEmpty()) {
            return "Subject is required";
        }
        if (teacherDTO.getExperience() < 0 || teacherDTO.getExperience() > 50) {
            return "Experience must be between 0 and 50 years";
        }
        if (teacherDTO.getSalary() <= 0 || teacherDTO.getSalary() > 100000) {
            return "Salary must be between 0 and 100000";
        }
        String email = teacherDTO.getEmail();
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return "Invalid email format";
        }
        boolean duplicateExists = teachers.stream()
                .anyMatch(t -> t.getFirstName().equalsIgnoreCase(teacherDTO.getFirstName())
                        && t.getLastName().equalsIgnoreCase(teacherDTO.getLastName()));
        if (duplicateExists) {
            return "Teacher with this name already exists";
        }

        return null; // Если ошибок нет
    }
}
