package uz.usmanov.appjparelations.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.usmanov.appjparelations.Repository.AddressRepository;
import uz.usmanov.appjparelations.Repository.UniversityRepository;
import uz.usmanov.appjparelations.entity.Address;
import uz.usmanov.appjparelations.entity.University;
import uz.usmanov.appjparelations.payload.UniversityDto;

import java.util.List;
import java.util.Optional;

@RestController
public class UniversityController {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    AddressRepository addressRepository;

    /*** READ ALL ***/
    @RequestMapping(value = "/university",method = RequestMethod.GET)
    public List<University> getUniversities(){
        List<University> all = universityRepository.findAll();
        return all;
    }

    /*** READ BY ID ***/
    @RequestMapping(value = "/university/{id}",method = RequestMethod.GET)
    public University getUniverById(@PathVariable Integer id){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()){
            University university = optionalUniversity.get();
            return university;
        }
        return new University();
    }


    /***** CREAT ****/
    @RequestMapping(value = "/university",method = RequestMethod.POST)
    public String addUniversity(@RequestBody UniversityDto universityDto){

        Address address=new Address();
        address.setCity(universityDto.getCity());
        address.setDistrict(universityDto.getDistrict());
        address.setStreet(universityDto.getStreet());

        Address address1 = addressRepository.save(address);

        University university=new University();
        university.setName(universityDto.getName());
        university.setAddress(address1);
        universityRepository.save(university);

        return "Universoty Saved";
    }

    /***** UPDATE ****/
    @RequestMapping(value = "/university/{id}",method = RequestMethod.PUT)
    public String editUniversity(@RequestBody UniversityDto universityDto,@PathVariable Integer id){

        Optional<University> optUniversity = universityRepository.findById(id);
            if (optUniversity.isPresent()){
                University university = optUniversity.get();

                university.setName(universityDto.getName());

                university.getAddress().setCity(universityDto.getCity());
                university.getAddress().setDistrict(universityDto.getDistrict());
                university.getAddress().setStreet(universityDto.getStreet());

                universityRepository.save(university);

            return "Successfully";
        }
        return "Not Found";
    }

    /**** DELETE ****/
    @RequestMapping(value = "/university/{id}",method = RequestMethod.DELETE)
    public String deleteUniversity(@PathVariable Integer id){
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isPresent()){
            University university = universityOptional.get();
            universityRepository.delete(university);
            return "Deleted";
        }
        return "Not found such a university";
    }


}
