import { Component, Inject, OnInit } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef} from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SessionService } from '../../shared/session-service';
import {MatIcon} from "@angular/material/icon";
import {UpdatePassword} from "../update-password/update-password";

@Component({
  selector: 'app-session-details-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIcon
  ],
  templateUrl: './session-details-dialog.html',
  styleUrl: './session-details-dialog.css'
})
export class SessionDetailsDialog implements OnInit {

  session: any;
  loading = true;

  constructor(
    private sessionService: SessionService,private dialog: MatDialog,
    public dialogRef: MatDialogRef<SessionDetailsDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { id: string }
  ) {}

  ngOnInit(): void {
    this.sessionService.getSessionInformation(this.data.id).subscribe({
      next: (response) => {
        this.session = response;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  openChangePassword(){
    this.dialog.open(UpdatePassword, {
      width: '700px',
      maxWidth: '95vw',
      disableClose: true
    });
  }
}
