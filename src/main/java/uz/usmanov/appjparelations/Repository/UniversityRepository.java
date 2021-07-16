package uz.usmanov.appjparelations.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.usmanov.appjparelations.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University,Integer> {
}
