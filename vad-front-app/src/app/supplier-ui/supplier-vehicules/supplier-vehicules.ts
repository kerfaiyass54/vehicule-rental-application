import {
  Component,
  ElementRef,
  ViewChild,
  inject,
  signal,
  OnInit
} from '@angular/core';

import Keycloak from 'keycloak-js';

import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule
} from '@angular/forms';

import {
  VehiculeDTO,
  VehiculesService
} from '../../services/vehicules-service';

type VehiculeStatus =
  | 'AVAILABLE'
  | 'TAKEN'
  | 'REPARATION';

@Component({
  selector: 'app-supplier-vehicules',
  standalone: true,
  templateUrl: './supplier-vehicules.html',
  styleUrl: './supplier-vehicules.css',
  imports: [ReactiveFormsModule]
})
export class SupplierVehicules implements OnInit {

  @ViewChild('carousel')
  carousel!: ElementRef;

  protected keycloak = inject(Keycloak);

  private vehiculeService =
    inject(VehiculesService);

  private fb = inject(FormBuilder);

  email = '';

  vehicules = signal<VehiculeDTO[]>([]);

  totalVehicules = signal(0);
  availableVehicules = signal(0);
  takenVehicules = signal(0);
  repairVehicules = signal(0);

  selectedVehiculeId =
    signal<number | null>(null);

  updateForm: FormGroup =
    this.fb.group({

      color: [''],

      price: [0],

      highSpeed: [0]

    });

  ngOnInit(): void {

    const token =
      this.keycloak.tokenParsed;

    if (token) {

      this.email =
        token['email'] ?? '';

      this.loadDashboard();

      this.loadVehicules();

    }

  }

  loadDashboard() {

    this.vehiculeService
      .getTotalVehicules(this.email)
      .subscribe(data =>
        this.totalVehicules.set(data)
      );

    this.vehiculeService
      .getVehiculesByStatus(
        this.email,
        'AVAILABLE'
      )
      .subscribe(data =>
        this.availableVehicules.set(data)
      );

    this.vehiculeService
      .getVehiculesByStatus(
        this.email,
        'TAKEN'
      )
      .subscribe(data =>
        this.takenVehicules.set(data)
      );

    this.vehiculeService
      .getVehiculesByStatus(
        this.email,
        'REPARATION'
      )
      .subscribe(data =>
        this.repairVehicules.set(data)
      );

  }

  loadVehicules() {

    this.vehiculeService
      .getVehiculesList(this.email)
      .subscribe(data =>
        this.vehicules.set(data)
      );

  }

  openUpdateModal(
    vehicule: VehiculeDTO
  ) {

    this.selectedVehiculeId.set(
      vehicule.idVehicule
    );

    this.updateForm.patchValue({

      color: vehicule.color,

      price: vehicule.price,

      highSpeed: vehicule.highSpeed

    });

  }

  updateVehicule() {

    const id = this.selectedVehiculeId();

    if (!id) return;

    this.vehiculeService.updateVehicule({

      idVehicule: id,

      ...this.updateForm.value

    })
      .subscribe(() => {
        this.refreshPage();

      });

  }

  scrollLeft() {

    this.carousel
      .nativeElement
      .scrollBy({

        left: -320,

        behavior: 'smooth'

      });

  }

  scrollRight() {

    this.carousel
      .nativeElement
      .scrollBy({

        left: 320,

        behavior: 'smooth'

      });

  }

  refreshPage() {

    location.reload();

  }



}
