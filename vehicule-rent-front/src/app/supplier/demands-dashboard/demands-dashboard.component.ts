import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-demands-dashboard',
    imports: [],
    templateUrl: './demands-dashboard.component.html',
    styleUrl: './demands-dashboard.component.css'
})
export class DemandsDashboardComponent {

}
