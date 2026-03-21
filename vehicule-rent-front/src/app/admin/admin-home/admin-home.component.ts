import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-admin-home',
    imports: [],
    templateUrl: './admin-home.component.html',
    styleUrl: './admin-home.component.css'
})
export class AdminHomeComponent {

}
