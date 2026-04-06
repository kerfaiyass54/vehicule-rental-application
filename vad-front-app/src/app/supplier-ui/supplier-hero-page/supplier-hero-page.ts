import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-supplier-hero-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './supplier-hero-page.html',
  styleUrls: ['./supplier-hero-page.css']
})
export class SupplierHeroPage {

  supplier = {
    suppName: 'Foulen',
    nationality: 'Tunisian',
    email: 'supplier@email.com',
    experience: 6,
    role: 'SUPPLIER'
  };

  vehiclesCount = 12;
  categoriesCount = 5;
  placesCount = 3;

}
