package uz.usmanov.appjparelations.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.usmanov.appjparelations.Repository.FacultyRepository;
import uz.usmanov.appjparelations.Repository.UniversityRepository;
import uz.usmanov.appjparelations.entity.Faculty;
import uz.usmanov.appjparelations.entity.University;
import uz.usmanov.appjparelations.payload.FacultyDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/faculty")
public class FacultyController {

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;


    //VAZIRLIK UCHUN BARCHA OTM DAGI Facultetlarni KORADI
    @GetMapping
    public List<Faculty> getFaculties(){
        List<Faculty> all = facultyRepository.findAll();
        return all;
    }


    @PostMapping
    public String addFaculty(@RequestBody FacultyDto facultyDto) {
        boolean exists = facultyRepository.existsByNameAndUniversityId(facultyDto.getName(), facultyDto.getUniversity_id());
        if (exists)
            return "This university with faculty already exists";

        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversity_id());
        if (optionalUniversity.isPresent()) {

            Faculty faculty = new Faculty();
            faculty.setName(facultyDto.getName());
            University university = optionalUniversity.get();
            faculty.setUniversity(university);
            facultyRepository.save(faculty);
            return "Saved";
        }
        return "University Not found";

    }

    //MALUM BIR UNIVERSITETNI XODIMI UCHUN OZ UNIVERSITETINI KORISHI UCHUN
    @GetMapping("/byUniversityId/{universityId}")
    public List<Faculty> getFacultyByUniversityId(@PathVariable Integer universityId){
        List<Faculty> allByUniversityId = facultyRepository.findAllByUniversityId(universityId);
        return allByUniversityId;

    }
}
