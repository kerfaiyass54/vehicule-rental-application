import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {UserService} from '../../services/user-service';

@Component({
  selector: 'app-update-profile-modal',
  standalone: true,
  imports: [
    FormsModule,
    MatButtonModule, MatFormFieldModule, MatIconModule,
    MatInputModule, MatProgressSpinnerModule,
  ],
  templateUrl: './update-profile-modal.html',
  styleUrl: './update-profile-modal.css',
})
export class UpdateProfileModal implements OnInit {
  @Input() userId    = '';
  @Input() firstName = '';
  @Input() lastName  = '';
  @Input() email     = '';
  @Input() role      = '';   // required by UpdateUserDTO

  @Output() closed  = new EventEmitter<void>();
  @Output() updated = new EventEmitter<any>();

  private keycloakService = inject(UserService);

  // mirrors UpdateUserDTO: { email, firstName, lastName, role, newEmail }
  form: any = { email: '', firstName: '', lastName: '', role: '', newEmail: '' };

  loading = false;
  error   = '';
  success = false;

  ngOnInit(): void {
    this.form = {
      email:     this.email,
      firstName: this.firstName,
      lastName:  this.lastName,
      role:      this.role,
      newEmail:  this.email,   // user can change this to a new email
    };
  }

  onSubmit(): void {
    this.loading = true;
    this.error   = '';
    this.success = false;

    this.keycloakService.updateUser(this.userId, this.form).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        this.updated.emit(this.form);
        setTimeout(() => this.onClose(), 1200);
      },
      error: (err: any) => {
        this.error   = err?.error?.message ?? 'Failed to update profile.';
        this.loading = false;
      },
    });
  }

  onClose(): void { this.closed.emit(); }

  onBackdropClick(e: MouseEvent): void {
    if ((e.target as HTMLElement).classList.contains('modal-overlay')) {
      this.onClose();
    }
  }
}
