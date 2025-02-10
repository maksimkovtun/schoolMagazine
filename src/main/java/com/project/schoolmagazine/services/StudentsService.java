package com.project.schoolmagazine.services;

import com.project.schoolmagazine.entities.StudentsEntity;
import com.project.schoolmagazine.repositories.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentsService {

    private final StudentsRepository studentsRepository;

    @Autowired
    public StudentsService(StudentsRepository studentsRepository) {
        this.studentsRepository = studentsRepository;
    }

    public List<StudentsEntity> getAllStudents() {
        return studentsRepository.findAll();
    }

    public Optional<StudentsEntity> getStudentById(Integer id) {
        return studentsRepository.findById(id);
    }

    public StudentsEntity saveStudent(StudentsEntity student) {
        return studentsRepository.save(student);
    }

    public void deleteStudent(Integer id) {
        studentsRepository.deleteById(id);
    }
}
