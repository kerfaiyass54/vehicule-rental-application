package com.projecttuto.vehicule_rental.controllers;



import com.projecttuto.vehicule_rental.dto.ClientAdminDTO;
import com.projecttuto.vehicule_rental.dto.SupplierAdminDTO;
import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import com.projecttuto.vehicule_rental.dto.AdminDashboardDTO;
import com.projecttuto.vehicule_rental.dto.AdminDTO;
import com.projecttuto.vehicule_rental.services.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {


    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @GetMapping("/clients")
    public ResponseEntity<Page<ClientAdminDTO>> getClients(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size){

        return ResponseEntity.ok(
                adminService.getClients(page,size));

    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientAdminDTO> getClient(
            @PathVariable Long id){

        return ResponseEntity.ok(
                adminService.getClient(id));

    }

    @PutMapping("/clients/{id}")
    public ResponseEntity<ClientAdminDTO> updateClient(

            @PathVariable Long id,

            @RequestBody ClientAdminDTO dto){

        return ResponseEntity.ok(
                adminService.updateClient(id,dto));

    }

    @DeleteMapping("/clients/{id}")
    public ResponseEntity<String> deleteClient(
            @PathVariable Long id){

        adminService.deleteClient(id);

        return ResponseEntity.ok("Client deleted successfully.");

    }


    @GetMapping("/suppliers")
    public ResponseEntity<Page<SupplierAdminDTO>> getSuppliers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size){

        return ResponseEntity.ok(
                adminService.getSuppliers(page, size));

    }

    @GetMapping("/suppliers/{id}")
    public ResponseEntity<SupplierAdminDTO> getSupplier(
            @PathVariable Long id){

        return ResponseEntity.ok(
                adminService.getSupplier(id));

    }

    @PutMapping("/suppliers/{id}")
    public ResponseEntity<SupplierAdminDTO> updateSupplier(

            @PathVariable Long id,

            @RequestBody SupplierAdminDTO dto){

        return ResponseEntity.ok(
                adminService.updateSupplier(id, dto));

    }

    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<String> deleteSupplier(
            @PathVariable Long id){

        adminService.deleteSupplier(id);

        return ResponseEntity.ok("Supplier deleted successfully.");

    }

    @GetMapping("/repairs")
    public ResponseEntity<Page<RepairAdminDTO>> getRepairs(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                adminService.getRepairs(page, size));
    }

    @GetMapping("/repairs/{id}")
    public ResponseEntity<RepairAdminDTO> getRepair(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminService.getRepair(id));
    }

    @PutMapping("/repairs/{id}")
    public ResponseEntity<RepairAdminDTO> updateRepair(

            @PathVariable Long id,

            @RequestBody RepairAdminDTO dto) {

        return ResponseEntity.ok(
                adminService.updateRepair(id, dto));
    }

    @DeleteMapping("/repairs/{id}")
    public ResponseEntity<String> deleteRepair(
            @PathVariable Long id) {

        adminService.deleteRepair(id);

        return ResponseEntity.ok("Repair center deleted successfully.");
    }

    @PostMapping("/locations")
    public ResponseEntity<LocationAdminDTO> createLocation(
            @RequestBody LocationAdminDTO dto) {

        return ResponseEntity.ok(
                adminService.createLocation(dto));
    }

    @GetMapping("/locations")
    public ResponseEntity<Page<LocationAdminDTO>> getLocations(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                adminService.getLocations(page, size));
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationAdminDTO> getLocation(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminService.getLocation(id));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDTO> getDashboard() {

        return ResponseEntity.ok(
                adminService.getDashboard());

    }

    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationAdminDTO> updateLocation(

            @PathVariable Long id,

            @RequestBody LocationAdminDTO dto) {

        return ResponseEntity.ok(
                adminService.updateLocation(id, dto));
    }

    @DeleteMapping("/locations/{id}")
    public ResponseEntity<String> deleteLocation(
            @PathVariable Long id) {

        adminService.deleteLocation(id);

        return ResponseEntity.ok("Location deleted successfully.");
    }




    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(@RequestBody AdminDTO adminDTO,@PathVariable Long id){
        adminService.updateDetails(adminDTO,id);
        return ResponseEntity.noContent().build();
    }


    // u use id instead of name be careful


    @GetMapping("/{adminName}")
    public ResponseEntity<AdminDTO> getDetails(@PathVariable Long id){
        AdminDTO admin =  adminService.getDetails(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }








}
