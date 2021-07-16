package uz.usmanov.appjparelations.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.usmanov.appjparelations.Repository.GroupRepository;
import uz.usmanov.appjparelations.Repository.SubjectRepository;
import uz.usmanov.appjparelations.entity.Subject;
import uz.usmanov.appjparelations.payload.Student1Dto;

import java.util.List;
import java.util.Optional;

@RestController
public class SubjectController {


    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupRepository groupRepository;

    //read all

//    @GetMapping=>bir xil funksiya bajaradi
    @RequestMapping(value = "/subject",method = RequestMethod.GET)
    public List<Subject> getSubjects(){
        List<Subject> all = subjectRepository.findAll();
        return all;
    }

    //read one by id
    @RequestMapping(value = "/subject/{id}",method = RequestMethod.GET)
    public Subject getSubject(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);

        if (optionalSubject.isPresent()){
            Subject subject = optionalSubject.get();
            return subject;
        }
        return new Subject();
    }

    //create
    @RequestMapping(value = "/subject",method = RequestMethod.POST)
    public String addSubject(@RequestBody Subject subject){
        boolean math = subjectRepository.existsByName(subject.getName());
        if (math){
            return "Subject already exists";
        }
        subjectRepository.save(subject);
        return "Saved";
    }

    //update
    @RequestMapping(value = "/subject/{id}",method = RequestMethod.PUT)
    public String editSubject(@RequestBody Subject subject,@PathVariable Integer id){
        Optional<Subject> subjectOptional = subjectRepository.findById(id);
        if (subjectOptional.isPresent()){
            Subject subject1 = subjectOptional.get();
            subject1.setName(subject.getName());
            return "Edited";
        }
        return "Not found subject";
    }

    //delete
    @RequestMapping(value = "/subject/{id}",method = RequestMethod.DELETE)
    public String deleteSubject(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isPresent()){

            Subject subject = optionalSubject.get();
            subjectRepository.delete(subject);
            return "Deleted";
        }
        return "Not found subject";
    }

    /************************************************************************************/

}
