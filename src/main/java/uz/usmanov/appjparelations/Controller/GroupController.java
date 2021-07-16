package uz.usmanov.appjparelations.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.usmanov.appjparelations.Repository.FacultyRepository;
import uz.usmanov.appjparelations.Repository.GroupRepository;
import uz.usmanov.appjparelations.Repository.UniversityRepository;
import uz.usmanov.appjparelations.entity.Faculty;
import uz.usmanov.appjparelations.entity.Group;
import uz.usmanov.appjparelations.entity.University;
import uz.usmanov.appjparelations.payload.GroupDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/group")
public class GroupController {


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;

    //VAZIRLIK UCHUN
    @GetMapping
    public List<Group> getGroups(){
        List<Group> groupList = groupRepository.findAll();
        return groupList;
    }

    //hodim uchun
    @GetMapping("/byUniversityId/{university_id}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer university_id){

        List<Group> allByFaculty_universityId = groupRepository.findAllByFaculty_UniversityId(university_id);
        List<Group> groupByUniversityId = groupRepository.getGroupByUniversityId(university_id);

        List<Group> allByFaculty_universityId1 = groupRepository.findAllByFaculty_UniversityId(university_id);

        return allByFaculty_universityId1;

    }

    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto){
            Optional<Faculty> faculty = facultyRepository.findById(groupDto.getFacultyId());
            if (!faculty.isPresent())
                return "Faculty not found";

            Group group=new Group();
            group.setName(groupDto.getName());
            group.setFaculty(faculty.get());
            groupRepository.save(group);
            return "Saved";
        }


        /* MA'LUM BIR UNIVERSITETNI MALUM BIR FACULTETIGA GURUH QO'SHISH UCHUN */

        @PostMapping("/ByUniversity_ID/{university_id}")
        public String addgroup(@RequestBody GroupDto groupDto,@PathVariable Integer university_id){
            boolean exists = universityRepository.existsById(university_id);
            boolean exists1 = facultyRepository.existsById(groupDto.getFacultyId());
            boolean exists2 = groupRepository.existsByNameAndFaculty_IdAndFaculty_University_Id(groupDto.getName(), groupDto.getFacultyId(), university_id);
            if (exists2)
                return "Group's name already exists";
            if (exists){
                if (exists1){

                    Group group=new Group();
                    group.setName(groupDto.getName());
                    group.setFaculty(facultyRepository.getById(groupDto.getFacultyId()));
                    groupRepository.save(group);
                    return "Saved";
                }
                return "Faculty Not Found";
            }

            return "University not found";
        }


//        @PutMapping("/{group_id}")
//    public String EditGroup(@RequestBody GroupDto groupDto,@PathVariable Integer group_id){
//            boolean exists = groupRepository.existsByIdAndFaculty_Id(group_id, groupDto.getFacultyId());
//            if (exists) {
//                Group group = groupRepository.findByIdAndFaculty_Id(group_id, groupDto.getFacultyId());
//                group.setName(groupDto.getName());
//                group.setFaculty(facultyRepository.getById(groupDto.getFacultyId()));
//                groupRepository.save(group);
//                return "Edited";
//            }
//            return "Group and Faculty not found";
//
//        }


        @PutMapping("/{group_id}")
        public String edit2(@PathVariable Integer group_id,@RequestBody GroupDto groupDto){
            boolean exists = facultyRepository.existsById(groupDto.getFacultyId());
            boolean group = groupRepository.existsById(group_id);
            if (exists) {
                if (group) {
                    Optional<Group> optionalGroup = groupRepository.findById(group_id);
                    Group group1 = optionalGroup.get();
                    group1.setName(groupDto.getName());
                    group1.setFaculty(facultyRepository.getById(groupDto.getFacultyId()));
                    groupRepository.save(group1);
                    return "Edited";
                }
                return "Group Not found";
            }
            return "Faculty Not found";
        }
        
        @DeleteMapping("/{group_id}")
    public String deleteGroup(@PathVariable Integer group_id){
            boolean exists = groupRepository.existsById(group_id);

            if (exists) {
                groupRepository.deleteById(group_id);
                return "Deleted";
            }
            return "Group not found";

    }

    @GetMapping("/{university_id}/{faculty_id}")
    public List<Group> getGroup(@PathVariable Integer university_id,@PathVariable Integer faculty_id){

        List<Group> byFaculty_idAndFaculty_university_id = groupRepository.findByFaculty_IdAndFaculty_University_Id(faculty_id, university_id);

        return byFaculty_idAndFaculty_university_id;
    }
}



