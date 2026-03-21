import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-add-ticket',
    imports: [],
    templateUrl: './add-ticket.component.html',
    styleUrl: './add-ticket.component.css'
})
export class AddTicketComponent {

}
