package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.AdressStatus;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final AdressRepository adressRepository;
    private final DemandRepository demandRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;


    @Override
    public SupplierDetailsDTO getDetails(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null)
            throw new RuntimeException("Supplier not found");

        SupplierDetailsDTO dto = new SupplierDetailsDTO();

        dto.setExperience(supplier.getExperience());
        dto.setNationality(supplier.getNationality());
        dto.setEmail(email);
        dto.setSuppName(supplier.getSuppName());

        return dto;
    }


    @Override
    public void addSupplier(Supplier supplier) {

        supplierRepository.save(supplier);

    }


    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {

        Supplier supplier = supplierRepository.findById(supplierDTO.getIdSupp())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.setNationality(supplierDTO.getNationality());
        supplier.setPass(supplierDTO.getPass());
        supplier.setSuppName(supplierDTO.getSuppName());
        supplier.setEmail(supplierDTO.getEmail());

        supplierRepository.save(supplier);
    }


    @Override
    public void deleteSupplier(String name) {

        Supplier supplier = supplierRepository.findBySuppName(name)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplierRepository.delete(supplier);
    }


    @Override
    public SupplierDTO getSupplier(String supplierName) {

        Supplier supplier = supplierRepository.findBySuppName(supplierName)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        SupplierDTO dto = new SupplierDTO();

        dto.setIdSupp(supplier.getIdSupp());
        dto.setNationality(supplier.getNationality());
        dto.setSuppName(supplier.getSuppName());
        dto.setEmail(supplier.getEmail());

        return dto;
    }


    @Override
    public void changeSupplierPassword(Supplier supplier, String newPassword) {

        supplier.setPass(newPassword);

        supplierRepository.save(supplier);

    }


    @Override
    public List<Subscription> getSubscriptions(Supplier supplier) {

        return supplierRepository.findById(supplier.getIdSupp())
                .orElseThrow(() -> new RuntimeException("Supplier not found"))
                .getSubscriptions();
    }


    @Override
    public List<Adress> getAdresses(Supplier supplier) {

        return supplierRepository.findById(supplier.getIdSupp())
                .orElseThrow(() -> new RuntimeException("Supplier not found"))
                .getAdresses();
    }


    @Override
    public List<Vehicule> getVehicules(Supplier supplier) {

        return supplierRepository.findById(supplier.getIdSupp())
                .orElseThrow(() -> new RuntimeException("Supplier not found"))
                .getVehicules();
    }


    @Override
    public Integer getSupplierVehicules(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getVehicules()
                .size();
    }


    @Override
    public Integer getSupplierCategories(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getCategories()
                .size();
    }


    @Override
    public Integer getSupplierAdresses(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getAdresses()
                .size();
    }


    @Override
    public Integer getSupplierCountries(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        return supplier.getAdresses()
                .stream()
                .map(Adress::getLocation)
                .map(Location::getCountry)
                .distinct()
                .toList()
                .size();
    }


    @Override
    public Integer getSupplierLocations(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getAdresses()
                .stream()
                .map(Adress::getLocation)
                .distinct()
                .toList()
                .size();
    }


    @Override
    public List<AdressDTO> getAdressesList(String email, int size, int page) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAdresses() == null)
            return List.of();

        return supplier.getAdresses()
                .stream()
                .skip((long) page * size)
                .limit(size)
                .map(adress -> {

                    AdressDTO dto = new AdressDTO();

                    dto.setIdAdress(adress.getIdAdress());
                    dto.setRoad(adress.getRoad());
                    dto.setNumber(adress.getNumber());
                    dto.setLocation(adress.getLocation().getName());
                    dto.setSupplierEmail(adress.getSupplier().getEmail());
                    dto.setAdressStatus(adress.getAdressStatus());

                    return dto;

                }).toList();
    }


    @Override
    public List<String> getCountries(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAdresses() == null)
            return List.of();

        return supplier.getAdresses()
                .stream()
                .map(Adress::getLocation)
                .map(Location::getCountry)
                .distinct()
                .toList();
    }


    @Override
    public List<LocationDTO> getLocations(String email, int size, int page) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAdresses() == null)
            return List.of();

        return supplier.getAdresses()
                .stream()
                .map(Adress::getLocation)
                .distinct()
                .skip((long) page * size)
                .limit(size)
                .map(location -> {

                    LocationDTO dto = new LocationDTO();

                    dto.setIdLoc(location.getIdLoc());
                    dto.setName(location.getName());
                    dto.setCountry(location.getCountry());
                    dto.setPosition(location.getPosition());

                    return dto;

                }).toList();
    }


    @Override
    public Integer getTotalVehicules(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicules() == null)
            return 0;

        return supplier.getVehicules().size();
    }


    @Override
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status) {

        return (int) supplierRepository.findSupplierByEmail(email)
                .getVehicules()
                .stream()
                .filter(v -> v.getVehiculeStatus() == status)
                .count();
    }


    @Override
    public List<VehiculeDTO> getVehiculesList(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicules() == null)
            return List.of();

        return supplier.getVehicules()
                .stream()
                .map(vehicule -> {

                    VehiculeDTO dto = new VehiculeDTO();

                    dto.setIdVehicule(vehicule.getIdVehicule());
                    dto.setNameVehicule(vehicule.getNameVehicule());
                    dto.setBrand(vehicule.getBrand());
                    dto.setColor(vehicule.getColor());
                    dto.setPrice(vehicule.getPrice());
                    dto.setHighSpeed(vehicule.getHighSpeed());
                    dto.setTransmission(vehicule.getTransmission());
                    dto.setVehiculeStatus(vehicule.getVehiculeStatus());

                    return dto;

                }).toList();
    }


    @Override
    public void updateVehicule(VehiculeUpdate vehiculeUpdate) {

        Vehicule vehicule = vehiculeRepository.findById(vehiculeUpdate.getIdVehicule())
                .orElseThrow(() -> new RuntimeException("Vehicule not found"));

        vehicule.setColor(vehiculeUpdate.getColor());
        vehicule.setPrice(vehiculeUpdate.getPrice());
        vehicule.setHighSpeed(vehiculeUpdate.getHighSpeed());

        vehiculeRepository.save(vehicule);
    }


    @Override
    public void addVehicule(VehiculeDTO vehiculeDTO) {

        Supplier supplier = supplierRepository.findSupplierByEmail(vehiculeDTO.getSupplier());

        Vehicule vehicule = new Vehicule();

        vehicule.setNameVehicule(vehiculeDTO.getNameVehicule());
        vehicule.setBrand(vehiculeDTO.getBrand());
        vehicule.setColor(vehiculeDTO.getColor());
        vehicule.setPrice(vehiculeDTO.getPrice());
        vehicule.setHighSpeed(vehiculeDTO.getHighSpeed());
        vehicule.setTransmission(vehiculeDTO.getTransmission());
        vehicule.setVehiculeStatus(vehiculeDTO.getVehiculeStatus());
        vehicule.setSupplier(supplier);

        vehiculeRepository.save(vehicule);
    }


    @Override
    public Integer getTotalCategories(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getCategories()
                .size();
    }


    @Override
    public Integer getTotalStock(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        Integer totalStock = 0;

        for (Category category : supplier.getCategories())
            totalStock += category.getStock();

        return totalStock;
    }


    @Override
    public List<CategoryDTO> getCategoryList(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getCategories()
                .stream()
                .map(category -> {

                    CategoryDTO dto = new CategoryDTO();

                    dto.setIdCategory(category.getIdCategory());
                    dto.setNameCategory(category.getNameCategory());
                    dto.setTypeCategory(category.getTypeCategory());
                    dto.setStock(category.getStock());

                    return dto;

                }).toList();
    }


    @Override
    public Integer getStockContent(String email, String typeCategory) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null)
            return 0;

        Integer total = 0;

        for (Vehicule vehicule : supplier.getVehicules()) {

            if (vehicule.getCategory() != null &&
                    vehicule.getCategory().getTypeCategory().equals(typeCategory)) {

                total++;

            }

        }

        return total;
    }


    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO, String supplierEmail) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null)
            throw new RuntimeException("Supplier not found");


        Category category = new Category();

        category.setNameCategory(categoryDTO.getNameCategory());
        category.setTypeCategory(categoryDTO.getTypeCategory());
        category.setStock(categoryDTO.getStock());
        category.setSupplier(supplier);


        Category savedCategory = categoryRepository.save(category);


        CategoryDTO dto = new CategoryDTO();

        dto.setIdCategory(savedCategory.getIdCategory());
        dto.setNameCategory(savedCategory.getNameCategory());
        dto.setTypeCategory(savedCategory.getTypeCategory());
        dto.setStock(savedCategory.getStock());


        return dto;
    }

    @Override
    public Vehicule addVehiculeNew(VehiculeCreation vehiculeCreation, String supplierEmail){
        Vehicule vehicule = new Vehicule();
        vehicule.setNameVehicule(vehiculeCreation.getNameVehicule());
        vehicule.setBrand(vehiculeCreation.getBrand());
        vehicule.setColor(vehiculeCreation.getColor());
        vehicule.setPrice(vehiculeCreation.getPrice());
        vehicule.setHighSpeed(vehiculeCreation.getHighSpeed());
        vehicule.setTransmission(vehiculeCreation.getTransmission());
        vehicule.setVehiculeStatus(VehiculeStatus.AVAILABLE);
        vehicule.setSupplier(supplierRepository.findSupplierByEmail(supplierEmail));
        vehicule.setCategory(categoryRepository.findCategoryByTypeCategory(vehiculeCreation.getCategory()));
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Adress addAdressNew(AddressCreation addressCreation, String supplierEmail){
        Adress adress = new Adress();
        adress.setRoad(addressCreation.getRoad());
        adress.setNumber(addressCreation.getNumber());
        adress.setAdressStatus(AdressStatus.ASSIGNED);
        adress.setSupplier(supplierRepository.findSupplierByEmail(supplierEmail));
        adress.setLocation(locationRepository.findLocationByName(addressCreation.getLocation()));
        return adressRepository.save(adress);
    }

    @Override
    public void freeAddress(Long AddressId){
        Optional<Adress> adressOptional = adressRepository.findById(AddressId);
        if (adressOptional.isPresent()) {
            Adress adress = adressOptional.get();
            adress.setSupplier(null);
            adress.setAdressStatus(AdressStatus.EMPTY);
        }
    }

    @Override
    public List<String> getVehiculesNames(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getVehicules().stream().map(Vehicule::getNameVehicule).toList();
    }

    @Override
    public List<String> getCategoriesNames(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getCategories().stream().map(Category::getTypeCategory).toList();
    }



}