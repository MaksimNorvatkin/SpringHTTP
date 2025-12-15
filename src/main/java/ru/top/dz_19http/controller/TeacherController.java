package ru.top.dz_19http.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.top.dz_19http.dto.TeacherDTO;
import ru.top.dz_19http.models.Teacher;
import ru.top.dz_19http.models.TeacherValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/teacher")
public class TeacherController {
    List<Teacher> teachers;

    {
        teachers = new ArrayList<>();
        teachers.add(new Teacher("Жора", "Абдулаев", "Математика", 10, 1000.0, "1", false));
        teachers.add(new Teacher("Саша", "Абдулаев", "Физкультура", 2, 200.0, "2", false));
        teachers.add(new Teacher("Жора", "Григорьев", "Математика", 3, 300.0, "2", true));
        teachers.add(new Teacher("Гена", "Петров", "Литература", 4, 400.0, "2", false));
        teachers.add(new Teacher("Сергей", "Ельшин", "Физика", 6, 9990.0, "2", false));
    }

    @GetMapping("/all")
    public List<TeacherDTO> getAll() {
        if (!teachers.isEmpty()) {
            return teachers.stream()
                    .map(Teacher::convert).toList();
        }
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public TeacherDTO getById(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            Optional<Teacher> teacher = teachers.stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst();
            if (teacher.isPresent()) {
                return teacher.get().convert();
            }
        }
        return null;
    }

    @GetMapping("/search")
    public List<TeacherDTO> getSearch(@RequestParam(required = false) String firstName,
                                      @RequestParam(required = false) String lastName) {
        if (lastName == null && firstName != null) {
            return teachers.stream()
                    .filter(teacher -> teacher.getFirstName().equalsIgnoreCase(firstName))
                    .map(Teacher::convert)
                    .toList();
        }
        if (firstName == null && lastName != null) {
            return teachers.stream()
                    .filter(teacher -> teacher.getLastName().equalsIgnoreCase(lastName))
                    .map(Teacher::convert)
                    .toList();
        }
        if (firstName == null && lastName == null) {
            return teachers.stream()
                    .map(Teacher::convert)
                    .toList();
        }
        if (lastName != null && firstName != null) {
            return teachers.stream()
                    .filter(teacher -> teacher.getLastName().equalsIgnoreCase(lastName))
                    .filter(teacher -> teacher.getFirstName().equalsIgnoreCase(firstName))
                    .map(Teacher::convert)
                    .toList();
        }
        return null;
    }

    @GetMapping("/subject/{subject}")
    public List<TeacherDTO> subjectByTeacher(@PathVariable String subject) {
        if (!teachers.isEmpty()) {
            return teachers.stream()
                    .filter(teacher -> teacher.getSubject().equalsIgnoreCase(subject))
                    .map(Teacher::convert)
                    .toList();
        }
        return new ArrayList<>();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TeacherDTO>> filterByExpSalary( //Как я верну ошибку если мы еще не проходили ResponseEntity, а надо вернуть List
                                                               @RequestParam(name = "minExp") Integer minExp,
                                                               @RequestParam(name = "maxExp") Integer maxExp,
                                                               @RequestParam(name = "minSalary") Double minSalary,
                                                               @RequestParam(name = "maxSalary") Double maxSalary
    ) {
        if (minExp > maxExp || minSalary > maxSalary) {
            return ResponseEntity.notFound().build();
        } else {
            List<TeacherDTO> result = teachers.stream()
                    .filter(t -> (t.getExperience() >= minExp && t.getExperience() <= maxExp)
                            && (t.getSalary() >= minSalary) && t.getSalary() <= maxSalary)
                    .map(Teacher::convert)
                    .toList();
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/active")
    public List<TeacherDTO> active() {
        return teachers.stream()
                .filter(teacher -> teacher.getAtive() == true)
                .map(Teacher::convert)
                .toList();
    }

    @GetMapping("/count")
    public Integer count() {
        return teachers.size();
    }

    @GetMapping("/count-by-subject")
    public Map<String, Integer> countBySubject() {
        return teachers.stream()
                .collect(Collectors.groupingBy(
                        Teacher::getSubject,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }

    @PostMapping("/add")
    public String add(@RequestBody TeacherDTO teacherDTO) {
        String error = TeacherValidator.validate(teacherDTO, teachers);
        if (error != null) {
            return error;
        }
        teachers.add(teacherDTO.convert());
        return "success";
    }
    @PostMapping("/add-bulk")
    public String addBulk(@RequestBody List<TeacherDTO> teacherDTOS) {
        int added = 0;
        for (TeacherDTO teacherDTO : teacherDTOS) {
            String error = TeacherValidator.validate(teacherDTO, teachers);
            if (error == null) {
                teachers.add(teacherDTO.convert());
                added++;
            }
        }
        return "Added: " + added + " teachers.";
    }

    @PutMapping("/update/{id}")
    public String updateById(@RequestBody TeacherDTO teacherDTO,
                             @PathVariable Integer id) {
        String error = TeacherValidator.validate(teacherDTO, teachers);
        if (error != null) {
            return error;
        }
        Teacher foundTeacher = teachers.stream()
                .filter(teacher -> teacher.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (foundTeacher == null) {
            return "Teacher not found";
        }
        foundTeacher.setFirstName(teacherDTO.getFirstName());
        foundTeacher.setLastName(teacherDTO.getLastName());
        foundTeacher.setSubject(teacherDTO.getSubject());
        foundTeacher.setExperience(teacherDTO.getExperience());
        foundTeacher.setSalary(teacherDTO.getSalary());
        foundTeacher.setEmail(teacherDTO.getEmail());
        foundTeacher.setAtive(teacherDTO.getAtive());

        return "success";
    }

    @PatchMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Integer id) {
        if (teachers.stream().anyMatch(teacher -> teacher.getId().equals(id))) {
            teachers.stream()
                    .filter(teacher -> teacher.getId().equals(id))
                    .forEach(teacher -> teacher.setAtive(false));
            return "success";
        }
        return "fail";
    }

    @PatchMapping("/activate/{id}")
    public String activate(@PathVariable Integer id) {
        if (teachers.stream().anyMatch(teacher -> teacher.getId().equals(id))) {
            teachers.stream()
                    .filter(teacher -> teacher.getId().equals(id))
                    .forEach(teacher -> teacher.setAtive(true));
            return "success";
        }
        return "fail";
    }

    @PatchMapping("/increase-salary/{id}")
    public String upSalary(@PathVariable Integer id,
                           @RequestParam Integer percent) {
        if (percent < 0 || percent >= 100) {
            return "fail";
        }
        boolean isValidSalary = teachers.stream()
                .filter(teacher -> teacher.getId().equals(id))
                .map(teacher -> teacher.getSalary() * (1 + percent / 100.0))
                .allMatch(newSalary -> newSalary <= 100000.0);
        if (!isValidSalary) {
            return "fail";
        }
        teachers.stream()
                .filter(teacher -> teacher.getId().equals(id))
                .forEach(teacher -> {
                    teacher.setSalary(teacher.getSalary() * (1 + percent / 100.0));
                });
        return "success";
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteTeacherById(@PathVariable Integer id) {
        if (id != null && id >= 0) {
            boolean removeTeacher = teachers.removeIf(teacher -> teacher.getId().equals(id));
            return removeTeacher;
        }
        return false;
    }

    @DeleteMapping("/delete-by-subject/{subject}")
    public Integer countTeacherBySubject(@PathVariable String subject) {

        int count = 0;
        List<Teacher> remove = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher.getSubject().equalsIgnoreCase(subject)) {
                remove.add(teacher);
                count++;
            }
        }
        teachers.removeAll(remove);
        return count;
    }

    @DeleteMapping("/delete-inactive")
    public Integer deleteInactive() {
        int count = 0;
        List<Teacher> remove = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if (teacher.getAtive() == false) {
                remove.add(teacher);
                count++;
            }
        }
        teachers.removeAll(remove);
        return count;
    }
}
