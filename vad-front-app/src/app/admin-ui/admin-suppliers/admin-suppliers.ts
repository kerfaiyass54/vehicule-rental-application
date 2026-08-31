import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  MatButtonModule
} from '@angular/material/button';

import {
  MatIconModule
} from '@angular/material/icon';

import {
  MatPaginatorModule,
  PageEvent
} from '@angular/material/paginator';

import {
  MatProgressSpinnerModule
} from '@angular/material/progress-spinner';

import {
  MatTooltipModule
} from '@angular/material/tooltip';

import {
  MatTableModule
} from '@angular/material/table';

import {
  Subject,
  finalize,
  takeUntil
} from 'rxjs';

import {
  SupplierManagementService
} from '../../services/admin-services/supplier-management.service';
import {SupplierAdmin} from '../models/supplier-admin.model';





@Component({
  selector: 'app-admin-suppliers',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule,

    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatTableModule
  ],

  templateUrl: './admin-suppliers.html',

  styleUrl: './admin-suppliers.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AdminSuppliers
  implements OnInit, AfterViewInit, OnDestroy {


  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly supplierService =
    inject(SupplierManagementService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly destroy$ =
    new Subject<void>();


  // =========================================================
  // STATE
  // =========================================================

  readonly suppliers =
    signal<SupplierAdmin[]>([]);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly page =
    signal(0);

  readonly size =
    signal(7);

  readonly totalElements =
    signal(0);


  // =========================================================
  // SEARCH
  // =========================================================

  searchTerm = '';


  // =========================================================
  // DIALOG
  // =========================================================

  readonly showEditDialog =
    signal(false);

  readonly showDeleteDialog =
    signal(false);


  selectedSupplier:
    SupplierAdmin | null = null;


  // =========================================================
  // FORM
  // =========================================================

  editName = '';

  editNationality = '';

  editExperience = '';


  saving = false;

  deleting = false;


  // =========================================================
  // MESSAGES
  // =========================================================

  successMessage = '';

  errorMessage = '';


  // =========================================================
  // TABLE
  // =========================================================

  readonly displayedColumns = [

    'supplier',

    'email',

    'nationality',

    'experience',

    'actions'

  ];


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {

    this.loadSuppliers();

  }


  ngAfterViewInit(): void {

    this.setupRevealAnimation();

  }


  ngOnDestroy(): void {

    this.destroy$.next();

    this.destroy$.complete();

  }


  // =========================================================
  // LOAD SUPPLIERS
  // =========================================================

  loadSuppliers(): void {

    this.loading.set(true);

    this.error.set(false);

    this.errorMessage = '';

    this.cdr.markForCheck();


    this.supplierService

      .getSuppliers(
        this.page(),
        this.size()
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

          setTimeout(() => {

            this.setupRevealAnimation();

          });

        })

      )

      .subscribe({

        next: response => {

          this.suppliers.set(
            response.content ?? []
          );

          this.totalElements.set(
            response.totalElements ?? 0
          );

          this.error.set(false);

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to load suppliers',
            error
          );

          this.suppliers.set([]);

          this.totalElements.set(0);

          this.error.set(true);

          this.errorMessage =
            'Unable to load suppliers.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // REFRESH
  // =========================================================

  refresh(): void {

    if (this.loading()) {

      return;

    }

    this.successMessage = '';

    this.loadSuppliers();

  }


  // =========================================================
  // PAGINATION
  // =========================================================

  onPageChange(
    event: PageEvent
  ): void {

    this.page.set(
      event.pageIndex
    );

    this.size.set(
      event.pageSize
    );

    this.loadSuppliers();

    window.scrollTo({

      top: 0,

      behavior: 'smooth'

    });

  }


  // =========================================================
  // SEARCH
  // =========================================================

  get filteredSuppliers(): SupplierAdmin[] {

    const search =
      this.searchTerm
        .trim()
        .toLowerCase();


    if (!search) {

      return this.suppliers();

    }


    return this.suppliers()
      .filter(supplier =>

        supplier.suppName
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.email
          ?.toLowerCase()
          .includes(search)

        ||

        supplier.nationality
          ?.toLowerCase()
          .includes(search)

      );

  }


  // =========================================================
  // OPEN EDIT DIALOG
  // =========================================================

  openEditDialog(
    supplier: SupplierAdmin
  ): void {

    this.selectedSupplier =
      supplier;

    this.editName =
      supplier.suppName ?? '';

    this.editNationality =
      supplier.nationality ?? '';

    this.editExperience =
      String(
        supplier.experience ?? 0
      );

    this.errorMessage = '';

    this.successMessage = '';

    this.showEditDialog.set(true);

    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE EDIT DIALOG
  // =========================================================

  closeEditDialog(): void {

    if (this.saving) {

      return;

    }

    this.showEditDialog.set(false);

    this.selectedSupplier =
      null;

    this.editName = '';

    this.editNationality = '';

    this.editExperience = '';

  }


  // =========================================================
  // UPDATE SUPPLIER
  // =========================================================

  updateSupplier(): void {

    if (
      !this.selectedSupplier ||
      this.saving
    ) {

      return;

    }


    const name =
      this.editName.trim();

    const nationality =
      this.editNationality.trim();

    const experience =
      Number(
        this.editExperience
      );


    if (!name) {

      this.errorMessage =
        'Supplier name is required.';

      return;

    }


    if (name.length < 2) {

      this.errorMessage =
        'Supplier name must contain at least 2 characters.';

      return;

    }


    if (!nationality) {

      this.errorMessage =
        'Nationality is required.';

      return;

    }


    if (
      Number.isNaN(experience) ||
      experience < 0
    ) {

      this.errorMessage =
        'Experience must be a valid positive number.';

      return;

    }


    this.saving = true;

    this.errorMessage = '';

    this.cdr.markForCheck();


    const updatedSupplier:
      SupplierAdmin = {

      ...this.selectedSupplier,

      suppName: name,

      nationality: nationality,

      experience: experience

    };


    this.supplierService

      .updateSupplier(
        this.selectedSupplier.id,
        updatedSupplier
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.saving = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: response => {

          const updated =
            response ?? updatedSupplier;


          this.suppliers.update(
            suppliers =>
              suppliers.map(supplier =>

                supplier.id === updated.id
                  ? updated
                  : supplier

              )
          );


          this.showEditDialog.set(false);

          this.selectedSupplier =
            null;

          this.successMessage =
            'Supplier updated successfully.';

          this.errorMessage = '';

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to update supplier',
            error
          );

          this.errorMessage =
            'Unable to update supplier.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // OPEN DELETE DIALOG
  // =========================================================

  openDeleteDialog(
    supplier: SupplierAdmin
  ): void {

    this.selectedSupplier =
      supplier;

    this.errorMessage = '';

    this.successMessage = '';

    this.showDeleteDialog.set(true);

    this.cdr.markForCheck();

  }


  // =========================================================
  // CLOSE DELETE DIALOG
  // =========================================================

  closeDeleteDialog(): void {

    if (this.deleting) {

      return;

    }

    this.showDeleteDialog.set(false);

    this.selectedSupplier =
      null;

  }


  // =========================================================
  // DELETE SUPPLIER
  // =========================================================

  deleteSupplier(): void {

    if (
      !this.selectedSupplier ||
      this.deleting
    ) {

      return;

    }


    this.deleting = true;

    this.errorMessage = '';

    this.cdr.markForCheck();


    const supplierId =
      this.selectedSupplier.id;


    this.supplierService

      .deleteSupplier(
        supplierId
      )

      .pipe(

        takeUntil(this.destroy$),

        finalize(() => {

          this.deleting = false;

          this.cdr.markForCheck();

        })

      )

      .subscribe({

        next: () => {

          this.suppliers.update(
            suppliers =>
              suppliers.filter(
                supplier =>
                  supplier.id !== supplierId
              )
          );


          this.totalElements.update(
            total =>
              Math.max(
                0,
                total - 1
              )
          );


          this.showDeleteDialog.set(false);

          this.selectedSupplier =
            null;

          this.successMessage =
            'Supplier deleted successfully.';

          this.errorMessage = '';

          this.cdr.markForCheck();

        },

        error: error => {

          console.error(
            'Unable to delete supplier',
            error
          );

          this.errorMessage =
            'Unable to delete supplier.';

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // INITIALS
  // =========================================================

  getInitials(
    name: string
  ): string {

    if (!name) {

      return '?';

    }


    return name

      .trim()

      .split(/\s+/)

      .slice(0, 2)

      .map(
        part =>
          part
            .charAt(0)
            .toUpperCase()
      )

      .join('');

  }


  // =========================================================
  // EXPERIENCE LABEL
  // =========================================================

  getExperienceLabel(
    experience: number
  ): string {

    if (experience === 1) {

      return '1 year';

    }

    return `${experience} years`;

  }


  // =========================================================
  // TRACK SUPPLIER
  // =========================================================

  trackSupplier(
    _index: number,
    supplier: SupplierAdmin
  ): number {

    return supplier.id;

  }


  // =========================================================
  // BACKDROP
  // =========================================================

  onEditBackdropClick(
    event: MouseEvent
  ): void {

    if (
      event.target ===
      event.currentTarget
    ) {

      this.closeEditDialog();

    }

  }


  onDeleteBackdropClick(
    event: MouseEvent
  ): void {

    if (
      event.target ===
      event.currentTarget
    ) {

      this.closeDeleteDialog();

    }

  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private setupRevealAnimation(): void {

    const elements =
      document.querySelectorAll(
        '.reveal'
      );


    if (!elements.length) {

      return;

    }


    const observer =
      new IntersectionObserver(

        entries => {

          entries.forEach(entry => {

            if (entry.isIntersecting) {

              entry.target.classList.add(
                'visible'
              );

            }

          });

        },

        {
          threshold: 0.08
        }

      );


    elements.forEach(element => {

      observer.observe(element);

    });

  }

}
