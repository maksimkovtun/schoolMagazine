package com.project.schoolmagazine.services;

import com.project.schoolmagazine.entities.ScoresEntity;
import com.project.schoolmagazine.repositories.ScoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoresService {

    private final ScoresRepository scoresRepository;
    @Autowired
    public ScoresService(ScoresRepository scoresRepository) {
        this.scoresRepository = scoresRepository;
    }
    public List<ScoresEntity> getAllScores() {
        return scoresRepository.findAll();
    }
    public Optional<ScoresEntity> getScoreById(Integer id) {
        return scoresRepository.findById(id);
    }
    public void addScore(ScoresEntity score) {
        scoresRepository.save(score);
    }
    public void deleteScore(Integer id) {
        scoresRepository.deleteById(id);
    }
}

