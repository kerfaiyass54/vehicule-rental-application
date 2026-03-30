import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TitleCasePipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { PasswordService } from '../../services/password-service';
import { UserService } from '../../services/user-service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-update-password-modal',
  standalone: true,
  imports: [
    FormsModule, TitleCasePipe,
    MatButtonModule, MatFormFieldModule, MatIconModule,
    MatInputModule, MatProgressSpinnerModule,
  ],
  templateUrl: './update-password-modal.html',
  styleUrl: './update-password-modal.css',
})
export class UpdatePasswordModal {
  @Input() userId = '';
  @Input() email  = '';
  @Input() role   = '';

  @Output() closed  = new EventEmitter<void>();
  @Output() updated = new EventEmitter<void>();

  private keycloakService  = inject(UserService);
  private passwordService  = inject(PasswordService);
  private cdr = inject(ChangeDetectorRef);

  form: any = { newPassword: '', confirmPassword: '' };

  showNew     = false;
  showConfirm = false;

  loading = false;
  error   = '';
  success = false;

  strengthPct         = 0;
  strengthLabel       = '';
  strengthChecked     = false;
  strengthSuggestions: string[] = [];

  validationAlerts: string[] = [];

  get isStrongEnough(): boolean {
    return this.strengthLabel === 'strong';
  }

  get passwordMismatch(): boolean {
    return !!this.form.confirmPassword &&
      this.form.newPassword !== this.form.confirmPassword;
  }

  validatePassword(): void {
    this.validationAlerts = [];

    const pwd = this.form.newPassword || '';

    if (pwd.length < 8)
      this.validationAlerts.push('Password must contain at least 8 characters.');

    if (!/[A-Z]/.test(pwd))
      this.validationAlerts.push('Add at least one uppercase letter.');

    if (!/[a-z]/.test(pwd))
      this.validationAlerts.push('Add at least one lowercase letter.');

    if (!/[0-9]/.test(pwd))
      this.validationAlerts.push('Add at least one number.');

    if (!/[!@#$%^&*(),.?":{}|<>]/.test(pwd))
      this.validationAlerts.push('Add at least one special character.');
  }

  onPasswordInput(): void {
    if (!this.form.newPassword) {
      this.strengthPct         = 0;
      this.strengthLabel       = '';
      this.strengthSuggestions = [];
      this.validationAlerts    = [];
      return;
    }

    this.validatePassword();

    this.passwordService.predict(this.form.newPassword).subscribe({
      next: (res: any) => {
        this.strengthPct = (res.strength / 3) * 100;

        this.strengthLabel =
          res.strength === 0 ? 'weak' :
            res.strength === 1 ? 'fair' :
              res.strength === 2 ? 'good' :
                res.strength === 3 ? 'strong' :
                  '';
        this.strengthSuggestions = res.suggestions ?? [];
      },
      error: () => {},
    });
  }

  checkStrength(): void {
    if (!this.form.newPassword) return;

    this.validatePassword();

    this.passwordService.predict(this.form.newPassword).subscribe({
      next: (res: any) => {
        this.strengthPct = (res.strength / 3) * 100;

        this.strengthLabel =
          res.strength === 0 ? 'weak' :
            res.strength === 1 ? 'fair' :
              res.strength === 2 ? 'good' :
                res.strength === 3 ? 'strong' :
                  '';
        this.strengthSuggestions = res.suggestions ?? [];
        this.strengthChecked     = true;
        this.cdr.detectChanges(); // ✅ fixes Angular NG0100 error

      },
      error: () => {},
    });
  }

  onSubmit(): void {
    if (this.passwordMismatch || !this.isStrongEnough) return;

    this.loading = true;
    this.error   = '';
    this.success = false;

    const dto: any = {
      email:       this.email,
      newPassword: this.form.newPassword,
      role:        this.role,
    };

    this.keycloakService.updatePassword(this.userId, dto).subscribe({
      next: () => {
        this.success = true;
        this.loading = false;
        this.updated.emit();
        setTimeout(() => this.onClose(), 1200);
      },
      error: (err: any) => {
        this.error   = err?.error?.message ?? 'Failed to update password.';
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
