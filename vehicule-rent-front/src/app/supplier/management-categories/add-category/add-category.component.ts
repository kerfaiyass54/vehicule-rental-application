import { Component , ChangeDetectionStrategy} from '@angular/core';

@Component({
changeDetection: ChangeDetectionStrategy.OnPush,
    selector: 'app-add-category',
    imports: [],
    templateUrl: './add-category.component.html',
    styleUrl: './add-category.component.css'
})
export class AddCategoryComponent {

}
