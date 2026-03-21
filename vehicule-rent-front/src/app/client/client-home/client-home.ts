import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-client-home',
    imports: [],
    templateUrl: './client-home.html',
    styleUrl: './client-home.css'
})
export class ClientHome {

}
