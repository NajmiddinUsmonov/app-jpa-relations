package uz.usmanov.appjparelations.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.usmanov.appjparelations.entity.Group;
import uz.usmanov.appjparelations.entity.University;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Integer> {


    /*1-usul:JPA querlaridan foydalandik*/
    List<Group> findAllByFaculty_UniversityId(Integer facuty_university_id);




    /*2-usul:JPA querlaridan foydalandik*/
    @Query("select gr from groups gr where gr.faculty.university.id=:universityId")
    List<Group> getGroupByUniversityId(Integer universityId);


    /*3-usul:Native query yozish orqali guruhlarni topish*/
    @Query(value ="select * from groups g join faculty f on f.id=g.faculty_id join university u on u.id=f.university_id where u.id=:university_id",nativeQuery = true)
    List<Group> getGroupsByUniversityIdNativeQuery(Integer university_id);

    Group findByIdAndFaculty_Id(Integer id,Integer faculty_id);

    boolean existsByIdAndFaculty_Id(Integer groupId,Integer facultyId);

    boolean existsByNameAndFaculty_IdAndFaculty_University_Id(String name,Integer facultyId,Integer universityId);

    List<Group> findByFaculty_IdAndFaculty_University_Id(Integer facultyId,Integer universityId);

    boolean existsByIdAndFaculty_IdAndFaculty_University_Id(Integer id,Integer faculty_id,Integer university_id);





}
