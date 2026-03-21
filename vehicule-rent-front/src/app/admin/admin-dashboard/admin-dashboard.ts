import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-admin-dashboard',
    imports: [],
    templateUrl: './admin-dashboard.html',
    styleUrl: './admin-dashboard.css'
})
export class AdminDashboard {

}
