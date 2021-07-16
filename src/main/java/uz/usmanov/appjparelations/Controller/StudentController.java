package uz.usmanov.appjparelations.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.usmanov.appjparelations.Repository.*;
import uz.usmanov.appjparelations.entity.Address;
import uz.usmanov.appjparelations.entity.Student;
import uz.usmanov.appjparelations.entity.Subject;
import uz.usmanov.appjparelations.payload.Student1Dto;
import uz.usmanov.appjparelations.payload.StudentDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {
    
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;

    @RequestMapping(value = "/student",method = RequestMethod.GET)
    public List<Student> getStudents(){
        List<Student> all = studentRepository.findAll();
        return all;
    }

    //read
    @RequestMapping(value = "/student/{id}",method = RequestMethod.GET)
    public Student getStudents(@PathVariable Integer id){
        Optional<Student> option = studentRepository.findById(id);
        if (option.isPresent()){
            Student student = option.get();
            return student;

        }
        return new Student();
    }

    //CREATE
    @RequestMapping(value = "/student",method = RequestMethod.POST)
    public String addStudent(@RequestBody StudentDto studentDto){
        Address address=new Address();

        address.setCity(studentDto.getCity());
        address.setDistrict(studentDto.getDistrict());
        address.setStreet(studentDto.getStreet());
        Address saveAddress = addressRepository.save(address);

        Subject subject=new Subject();

        subject.setName(studentDto.getName());
        Subject savedSubjects = subjectRepository.save(subject);
        List<Subject> subjects=new ArrayList<>(Arrays.asList(savedSubjects));


        Student student=new Student();

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(saveAddress);
        student.setSubjects(subjects);

        studentRepository.save(student);

        return "Saved";
    }

    @RequestMapping(value = "/student/{id}",method = RequestMethod.PUT)
    public String editStudent(@RequestBody StudentDto studentDto,@PathVariable Integer id){
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()){
            Student student = byId.get();
            student.setFirstName(studentDto.getFirstName());
            student.setLastName(studentDto.getLastName());

            student.getAddress().setCity(studentDto.getCity());
            student.getAddress().setDistrict(studentDto.getDistrict());
            student.getAddress().setStreet(studentDto.getStreet());

            for (Subject subject : student.getSubjects()) {
                subject.setName(studentDto.getName());
                break;
            }
            studentRepository.save(student);
            return "Edited";


        }
        return "Not found";
    }
    @RequestMapping(value = "/student/{id}",method = RequestMethod.DELETE)
    public String deleteStudent(@PathVariable Integer id){
        Optional<Student> byId = studentRepository.findById(id);
        if (byId.isPresent()){
            Student student = byId.get();
            studentRepository.delete(student);
            return "Deleted";
        }

        return "Not found";
    }

    /****************************************************************************************/

    @PostMapping("/student/universityID/{university_id}/facultyID/{faculty_id}/groupID/{group_id}")
    public String addStudent(@RequestBody Student1Dto student1Dto, @PathVariable Integer university_id, @PathVariable Integer faculty_id, @PathVariable Integer group_id){

        boolean group = groupRepository.existsById(group_id);
        boolean faculty = facultyRepository.existsById(faculty_id);
        boolean university = universityRepository.existsById(university_id);
        boolean subject = subjectRepository.existsById(student1Dto.getSubject_id());
        boolean addressExists = addressRepository.existsAddressByCityAndAndDistrictAndStreet(student1Dto.getCity(), student1Dto.getDistrict(), student1Dto.getStreet());


        boolean havingAddress = studentRepository.existsStudentByAddress_CityAndAddress_DistrictAndAddress_StreetAndFirstNameAndLastName(student1Dto.getCity(),student1Dto.getDistrict(),student1Dto.getStreet(), student1Dto.getFirstName(), student1Dto.getLastName());

        if (havingAddress)
            return "Such a Student with the address already exists";

        if (!university)
            return "Not Found University.Please! Put one";
        if (!faculty)
            return "Not Found Faculty.Please! Put one";
        if (!group)
            return "Not Found Group.Please! Put one";
        if (!subject)
            return "Not Found Subject.Please! Put one";




        Student student=new Student();
        student.setFirstName(student1Dto.getFirstName());
        student.setLastName(student1Dto.getLastName());

        if (addressExists){
            Address borAddress = addressRepository.getAddressByCityAndDistrictAndStreet(student1Dto.getCity(), student1Dto.getDistrict(), student1Dto.getStreet());

            student.setAddress(borAddress);
            student.setGroup(groupRepository.getById(group_id));

            student.setSubjects(Arrays.asList(subjectRepository.getById(student1Dto.getSubject_id())));

            Student save = studentRepository.save(student);
            return "Student Added";

        }

        Address address=new Address();
        address.setStreet(student1Dto.getStreet());
        address.setCity(student1Dto.getCity());
        address.setDistrict(student1Dto.getDistrict());
        Address address1 = addressRepository.save(address);
        student.setAddress(address1);

        student.setGroup(groupRepository.getById(group_id));

        student.setSubjects(Arrays.asList(subjectRepository.getById(student1Dto.getSubject_id())));

        Student save = studentRepository.save(student);
        return "Student Added";


    }


    /*Universitetga masul xodim uchun*/
    @GetMapping("/student/universityID/{university_id}")
    public List<Student> getUniversity(@PathVariable Integer university_id){
        List<Student> students = studentRepository.getStudentsByGroup_Faculty_University_Id(university_id);
        return students;
    }
    /*facultetega masul xodim uchun*/
    @GetMapping("/student/facultyId/{faculty_id}")
    public List<Student> getStudentInFaculty(@PathVariable Integer faculty_id){

        List<Student> students = studentRepository.getStudentsByFacultyID(faculty_id);
        return students;
    }

    /*Guruh masul xodimi uchun*/
    @GetMapping("/student/groupId/{group_id}")
    public List<Student> getStudentInGroups(@PathVariable Integer group_id){
        List<Student> studentsByGroupID = studentRepository.getStudentsByGroupID(group_id);
        return studentsByGroupID;
    }






    /******   Receive List of Students like Pages ******/

    //1.vazirlik

    @GetMapping("/studentPage")
    public Page<Student> getStudentListForMinistry(@RequestParam int page){
        //1-1=0 (1 pageda 0 ta tashaydi ya'ni eng boshidagi 10 ta ni oladi)
        //2-1=1 (2 pageda 10 ta tashab 11 dan boshlab 10ta oladi)
        // select * from student limit 10 offset (0*10)
        // elect * from student limit 10 offset (1*10)(ta tashaydi)

        Pageable pageable= PageRequest.of(page,10);
        Page<Student> all = studentRepository.findAll(pageable);
        return all;

    }

    //2.Universitet

    @GetMapping("/studentPage/{id}")
    public Page<Student> getStudentListForUniversity(@RequestParam int page,@PathVariable Integer id){
        //1-1=0 (1 pageda 0 ta tashaydi ya'ni eng boshidagi 10 ta ni oladi)
        //2-1=1 (2 pageda 10 ta tashab 11 dan boshlab 10ta oladi)
        // select * from student limit 10 offset (0*10)
        // elect * from student limit 10 offset (1*10)(ta tashaydi)

        Pageable pageable= PageRequest.of(page,10);
        Page<Student> all = studentRepository.findByGroup_Faculty_University_Id(id);
        return all;
    }







}
