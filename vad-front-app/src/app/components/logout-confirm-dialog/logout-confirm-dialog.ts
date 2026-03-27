import {Component, inject} from '@angular/core';
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import {MatIcon} from '@angular/material/icon';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-logout-confirm-dialog',
  imports: [
    MatIcon,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton
  ],
  templateUrl: './logout-confirm-dialog.html',
  styleUrl: './logout-confirm-dialog.css',
})
export class LogoutConfirmDialog {

  private dialogRef = inject(MatDialogRef<LogoutConfirmDialog>);

  cancel(): void {
    this.dialogRef.close(false);
  }

  confirm(): void {
    this.dialogRef.close(true);
  }

}
