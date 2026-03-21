import {Component, ChangeDetectionStrategy} from '@angular/core';
import {UserManagement} from "../../user-management/user-management";




@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-admin-details',
    imports: [
        UserManagement
    ],
    templateUrl: './admin-details.component.html',
    styleUrl: './admin-details.component.css'
})
export class AdminDetailsComponent {



}
