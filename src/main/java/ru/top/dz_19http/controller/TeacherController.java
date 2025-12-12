package ru.top.dz_19http.controller;


import org.springframework.web.bind.annotation.*;
import ru.top.dz_19http.dto.TeacherDTO;
import ru.top.dz_19http.models.Teacher;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/teacher")
public class TeacherController {
    List<Teacher> teachers;

    {
        teachers = new ArrayList<>();
        teachers.add(new Teacher("1","1","1",1,100.0,"1",true));
        teachers.add(new Teacher("2","2","2",2,200.0,"2",false));
    }
    @GetMapping("/all")
    public List<TeacherDTO> getAll() {
        if (!teachers.isEmpty()) {
            return teachers.stream()
                    .map(Teacher::convert).toList();
        }
        return new ArrayList<>();
    }
    @PostMapping("/add")
    public String add(@RequestBody TeacherDTO teacherDTO){
        if (!teacherDTO.getFirstName().isEmpty() || !teacherDTO.getLastName().isEmpty()) {
            teachers.add(teacherDTO.convert());
            return "success";
        }
        return  "fail";
    }



}
