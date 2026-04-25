import { Component, inject, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

import Keycloak from 'keycloak-js';

import {
  SupplierDetailsService
} from '../../../services/supplier-details-service';

declare var bootstrap: any;


@Component({

  selector: 'app-free-location',

  standalone: true,

  imports: [

    CommonModule,
    FormsModule

  ],

  templateUrl: './free-location.html',

  styleUrl: './free-location.css'

})

export class FreeLocation implements OnInit {

  protected keycloak = inject(Keycloak);

  email = '';

  searchText = '';

  selectedId: number | null = null;


  // backend data
  addresses: any[] = [];

  filteredAddresses: any[] = [];


  // pagination
  page = 0;

  size = 5;

  totalPages = 0;


  constructor(

    private supplierService: SupplierDetailsService

  ) {}


  ngOnInit(): void {

    const token = this.keycloak.tokenParsed;

    if (token) {

      this.email = token['email'] ?? '';

    }

    this.loadAddresses();

  }


  // load paginated addresses

  loadAddresses() {

    this.supplierService
      .getAddressesList(this.email, this.size, this.page)
      .subscribe({

        next: data => {

          this.addresses = data;

          this.filteredAddresses = data;

          this.totalPages =
            Math.ceil(data.length / this.size);

        },

        error: err =>
          console.error('Failed loading addresses', err)

      });

  }


  // search filter

  filter() {

    this.filteredAddresses =
      this.addresses.filter(addr =>

        addr.location
          ?.toLowerCase()
          .includes(this.searchText.toLowerCase())

        ||

        addr.road
          ?.toLowerCase()
          .includes(this.searchText.toLowerCase())

      );

  }


  // pagination getter

  get paginatedData() {

    const start =
      this.page * this.size;

    return this.filteredAddresses.slice(
      start,
      start + this.size
    );

  }


  changePage(p: number) {

    this.page = p;

  }


  // open modal

  openDeleteModal(id: number) {

    this.selectedId = id;

    const modal =
      new bootstrap.Modal(

        document.getElementById('deleteModal')

      );

    modal.show();

  }


  // free address (detach location)

  confirmDelete() {

    if (!this.selectedId) return;


    this.supplierService
      .freeAddress(this.selectedId)
      .subscribe({

        next: () => {

          this.loadAddresses();

          bootstrap.Modal
            .getInstance(
              document.getElementById('deleteModal')
            )
            .hide();

        },

        error: err =>
          console.error('Free address failed', err)

      });

  }

}
