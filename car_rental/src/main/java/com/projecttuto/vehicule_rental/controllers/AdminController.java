package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.projecttuto.vehicule_rental.services.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/clients/{id}")
    public ResponseEntity<List<ClientDTO>> getClients(@PathVariable long id){
        List<ClientDTO> clients =  adminService.getClients(id);
        if (clients != null) {
            return ResponseEntity.ok(clients);
        } else {
            return ResponseEntity.notFound().build();
        }
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








    @GetMapping("/suppliers/{id}")
    public ResponseEntity<List<SupplierDTO>> getSuppliers(@PathVariable long id){
        List<SupplierDTO> suppliers =  adminService.getSuppliers(id);
        if (suppliers != null) {
            return ResponseEntity.ok(suppliers);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/repairs/{id}")
    public ResponseEntity<List<RepairDTO>> getRepairs(@PathVariable long id){
        List<RepairDTO> repairs =  adminService.getRepairs(id);
        if (repairs != null) {
            return ResponseEntity.ok(repairs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<List<LocationDTO>> getLocations(@PathVariable long id){
        List<LocationDTO> locations =  adminService.getLocations(id);
        if (locations != null) {
            return ResponseEntity.ok(locations);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDetails(@RequestBody AdminDTO adminDTO,@PathVariable long id){
        adminService.updateDetails(adminDTO,id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{adminName}")
    public ResponseEntity<AdminDTO> getDetails(@PathVariable long id){
        AdminDTO admin =  adminService.getDetails(id);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getNames(){
        List<String> names =  adminService.getNames();
        if (names != null) {
            return ResponseEntity.ok(names);
        } else {
            return ResponseEntity.notFound().build();
        }
    }






}
