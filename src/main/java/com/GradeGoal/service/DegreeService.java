package com.GradeGoal.service;

import com.GradeGoal.model.Degree;
import com.GradeGoal.model.User;
import com.GradeGoal.repository.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeService {
    @Autowired
    private DegreeRepository degreeRepository;

    public Degree getDegree(String studentNo){
        List<Degree> degrees = degreeRepository.findAll();

        for(Degree x : degrees){
            List<User> users = x.getUsers();
            for(User u : users){
               if(u.getStudentNo().equals(studentNo)){
                   return x;
               }
            }
        }

        return null;
    }

    public Degree saveDegree(Degree degree){
        return degreeRepository.save(degree);
    }

    public List<Degree> getDegrees(){
        return degreeRepository.findAll();
    }

}
