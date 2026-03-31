import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {UserService} from '../../services/user-service';
import {UpdateProfileModal} from '../update-profile-modal/update-profile-modal';
import {UpdatePasswordModal} from '../update-password-modal/update-password-modal';

@Component({
  selector: 'app-delete-account-modal',
  standalone: true,
  imports: [
    FormsModule,
    MatButtonModule, MatFormFieldModule, MatIconModule,
    MatInputModule, MatProgressSpinnerModule,
  ],
  templateUrl: './delete-account-modal.html',
  styleUrl: './delete-account-modal.css',
})
export class DeleteAccountModal {
  @Input() userId = '';
  @Input() role   = '';
  @Input() email  = '';

  @Output() closed  = new EventEmitter<void>();
  @Output() deleted = new EventEmitter<void>();

  private keycloakService = inject(UserService);

  confirmText = '';
  loading     = false;
  error       = '';

  onConfirm(): void {
    if (this.confirmText !== 'DELETE') return;
    this.loading = true;
    this.error   = '';

    this.keycloakService.deleteUser(this.userId, this.role, this.email).subscribe({
      next: () => {
        this.loading = false;
        this.deleted.emit();
      },
      error: (err) => {
        this.error   = err?.error?.message ?? 'Failed to delete account.';
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
