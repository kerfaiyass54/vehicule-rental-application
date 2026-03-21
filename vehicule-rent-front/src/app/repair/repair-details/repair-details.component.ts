import { Component , ChangeDetectionStrategy} from '@angular/core';
import {UserManagement} from "../../user-management/user-management";

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-repair-details',
    imports: [
        UserManagement
    ],
    templateUrl: './repair-details.component.html',
    styleUrl: './repair-details.component.css'
})
export class RepairDetailsComponent {

}
