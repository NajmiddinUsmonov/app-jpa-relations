package uz.usmanov.appjparelations.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.usmanov.appjparelations.entity.Address;
import uz.usmanov.appjparelations.entity.Student;

import java.lang.annotation.Native;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Integer> {




    boolean existsStudentByAddress_CityAndAddress_DistrictAndAddress_StreetAndFirstNameAndLastName(String city,String district,String street,String firstName,String lastname);

    List<Student> getStudentsByGroup_Faculty_University_Id(Integer university_id);

    @Query("select st from Student st where st.group.faculty.university.id=:universityId")
    List<Student> getStudentsByUniveristId(Integer universityId);

    @Query(value = "select * from Student st join groups gr on st.group_id=gr.id join Faculty ft on ft.id=gr.faculty_id where ft.id=:facultyID",nativeQuery = true)
    List<Student> getStudentsByFacultyID(Integer facultyID);

    @Query(value = "select * from Student st join groups gr on st.group_id=gr.id where gr.id=:groupID",nativeQuery = true)
    List<Student> getStudentsByGroupID(Integer groupID);

    Page<Student> findByGroup_Faculty_University_Id(Integer universityId);



}
