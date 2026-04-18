import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

declare var bootstrap: any;

@Component({
  selector: 'app-free-location',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './free-location.html',
  styleUrl: './free-location.css',
})
export class FreeLocation {

  searchText = '';

  locations = [
    { id: 1, country: 'France', city: 'Paris' },
    { id: 2, country: 'Germany', city: 'Berlin' },
    { id: 3, country: 'USA', city: 'Chicago' },
    { id: 4, country: 'Spain', city: 'Madrid' },
    { id: 5, country: 'Italy', city: 'Rome' },
    { id: 6, country: 'Japan', city: 'Tokyo' },
    { id: 7, country: 'Canada', city: 'Toronto' },
    { id: 8, country: 'Brazil', city: 'Rio' },
    { id: 9, country: 'UK', city: 'London' },
    { id: 10, country: 'Netherlands', city: 'Amsterdam' }
  ];

  filteredLocations = [...this.locations];

  selectedId: number | null = null;

  page = 1;
  pageSize = 5;

  ngOnInit() {
    this.filter();
  }

  filter() {

    this.filteredLocations = this.locations.filter(loc =>
      loc.city.toLowerCase().includes(this.searchText.toLowerCase()) ||
      loc.country.toLowerCase().includes(this.searchText.toLowerCase())
    );

    this.page = 1;
  }

  get paginatedData() {

    const start = (this.page - 1) * this.pageSize;

    return this.filteredLocations.slice(start, start + this.pageSize);

  }

  get totalPages() {

    return Math.ceil(this.filteredLocations.length / this.pageSize);

  }

  changePage(p: number) {

    this.page = p;

  }

  openDeleteModal(id: number) {

    this.selectedId = id;

    const modal = new bootstrap.Modal(
      document.getElementById('deleteModal')
    );

    modal.show();

  }

  confirmDelete() {

    this.locations = this.locations.filter(
      loc => loc.id !== this.selectedId
    );

    this.filter();

    bootstrap.Modal.getInstance(
      document.getElementById('deleteModal')
    ).hide();

  }

}
