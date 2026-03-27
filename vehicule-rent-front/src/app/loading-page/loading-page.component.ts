import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { KeycloakService } from '../shared/keycloak.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'app-loading-page',
  standalone: true,
  imports: [],
  templateUrl: './loading-page.component.html',
  styleUrl: './loading-page.component.css'
})
export class LoadingPageComponent implements OnInit {

  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ) {}

  ngOnInit() {
    const roles = this.keycloakService.getRoles();

    if (roles.includes('admin')) {
      this.router.navigate(['/admin']);
    } else if (roles.includes('client')) {
      this.router.navigate(['/client']);
    } else if (roles.includes('supplier')) {
      this.router.navigate(['/supplier']);
    } else if (roles.includes('repair')) {
      this.router.navigate(['/repair']);
    } else {
      this.router.navigate(['/select-role']);
    }
  }
}
