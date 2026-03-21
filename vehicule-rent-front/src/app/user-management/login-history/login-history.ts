import {AfterViewInit, Component, EventEmitter, inject, OnInit, Output, ViewChild, ChangeDetectionStrategy} from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { SessionDetailsDialog } from '../session-details-dialog/session-details-dialog';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatNoDataRow, MatRow, MatRowDef,
  MatTable, MatTableDataSource
} from "@angular/material/table";
import {MatInput} from "@angular/material/input";
import {MatPaginator} from "@angular/material/paginator";
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from "@angular/cdk/drag-drop";
import {MatSort, MatSortHeader, Sort} from "@angular/material/sort";
import {LiveAnnouncer} from "@angular/cdk/a11y";
import {SessionService} from "../../shared/session-service";
import {KeycloakService} from "../../shared/keycloak.service";
import {ToastrService} from "ngx-toastr";
import {MatButton} from "@angular/material/button";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-login-history',
  imports: [
    MatFormField,
    MatLabel,
    MatTable,
    MatInput,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderRow,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatNoDataRow,
    MatPaginator,
    CdkDrag,
    CdkDropList,
    MatSort,
    MatSortHeader,
    MatButton,
    MatDialogModule
  ],
    templateUrl: './login-history.html',
    styleUrl: './login-history.css'
})
export class LoginHistory implements OnInit, AfterViewInit{
  sessions:any[] = [];
  displayedColumns: string[] = ['id', 'name', 'time', 'action'];
  dataSource = new MatTableDataSource<any>([]);
  private _liveAnnouncer = inject(LiveAnnouncer);
  totalElements = 0;
  pageSize = 5;
  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Output() sentSessions = new EventEmitter<any[]>();

  email: any;

  ngAfterViewInit() {
    this.dataSource.sort = this.sort;}

  /** Announce the change in sort state for assistive technology. */
  announceSortChange(sortState: Sort) {
    if (sortState.direction) {
      this._liveAnnouncer.announce(`Sorted ${sortState.direction}ending`);
    } else {
      this._liveAnnouncer.announce('Sorting cleared');
    }
  }

  constructor(private toastrService: ToastrService,private sessionService: SessionService,private keycloakService: KeycloakService,private dialog: MatDialog) {

  }

  openDetails(session: any): void {
    this.dialog.open(SessionDetailsDialog, {
      width: '600px',
      data: { id: session.id }
    });
  }



  async ngOnInit() {
    this.dataSource.sort = this.sort;
    this.email = (await this.keycloakService.loadUserProfile()).email;
    this.sessionService.getLoginSessions(0,this.pageSize,this.email).subscribe({
      next: (data)=>{
        this.dataSource.data = data.content;this.totalElements = data.totalElements;
        console.log(this.dataSource.data);
      },
      error: (err) => this.toastrService.error("There is an expected error! Try again later","ERROR")
    });
    this.sessionService.getSessionsByEmail(this.email).subscribe(
      (sessions)=>{
        this.sessions = sessions;
        this.sentSessions.emit(this.sessions);
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.displayedColumns, event.previousIndex, event.currentIndex);
  }

  loadSessions(page: number = 0, size: number = this.pageSize){
    this.sessionService.getLoginSessions(page,size,this.email).subscribe({
      next: (data)=>{
        this.dataSource.data = data.content;this.totalElements = data.totalElements;
    },
      error: (err) => this.toastrService.error("There is an expected error! Try again later","ERROR")
  });
  }



  onPageChange(event: any): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
   this.loadSessions(this.pageIndex, this.pageSize);
  }




}
