import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-client-details',
    imports: [],
    templateUrl: './client-details.component.html',
    styleUrl: './client-details.component.css'
})
export class ClientDetailsComponent {

}
