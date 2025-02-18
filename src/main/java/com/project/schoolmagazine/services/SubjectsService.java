package com.project.schoolmagazine.services;

import com.project.schoolmagazine.entities.SubjectsEntity;
import com.project.schoolmagazine.repositories.SubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectsService {

    private final SubjectsRepository subjectsRepository;

    @Autowired
    public SubjectsService(SubjectsRepository subjectsRepository) {
        this.subjectsRepository = subjectsRepository;
    }

    public List<SubjectsEntity> getAllSubjects() {
        return subjectsRepository.findAll();
    }

    public List<String> getSubjectsList() {
        return subjectsRepository.findAllSubjects();
    }

    public Optional<SubjectsEntity> getSubjectById(Integer id) {
        return subjectsRepository.findById(id);
    }

    public SubjectsEntity saveSubject(SubjectsEntity subject) {
        return subjectsRepository.save(subject);
    }

    public void deleteSubject(Integer id) {
        subjectsRepository.deleteById(id);
    }
    public void addSubject(String subjectName) {
        SubjectsEntity subject = new SubjectsEntity();
        subject.setSubject(subjectName);
        subjectsRepository.save(subject);
    }
}
