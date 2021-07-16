package uz.usmanov.appjparelations.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.usmanov.appjparelations.entity.Address;

public interface AddressRepository extends JpaRepository<Address,Integer> {

    boolean existsAddressByCityAndAndDistrictAndStreet(String city,String district,String street);
    Address getAddressByCityAndDistrictAndStreet(String city,String district,String street);
}
