package uz.usmanov.appjparelations.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.usmanov.appjparelations.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject,Integer> {

    boolean existsByName(String name);
}
