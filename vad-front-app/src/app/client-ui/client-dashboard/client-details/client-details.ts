import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  ElementRef,
  NgZone,
  OnDestroy,
  OnInit,
  QueryList,
  ViewChildren,
  inject,
  signal
} from '@angular/core';

import { CommonModule } from '@angular/common';
import Keycloak from 'keycloak-js';

import {
  finalize,
  take
} from 'rxjs';

import { ClientService } from '../../../services/client-services/client.service';
import { Client } from '../../models/client.model';


@Component({
  selector: 'app-client-details',
  standalone: true,

  imports: [
    CommonModule
  ],

  templateUrl: './client-details.html',
  styleUrl: './client-details.css',

  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientDetails implements OnInit, OnDestroy {

  // =========================================================
  // DEPENDENCIES
  // =========================================================

  private readonly keycloak = inject(Keycloak);

  private readonly clientService =
    inject(ClientService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  private readonly ngZone =
    inject(NgZone);


  // =========================================================
  // DOM
  // =========================================================

  @ViewChildren('revealElement', {
    read: ElementRef
  })
  private readonly revealElements!: QueryList<ElementRef<HTMLElement>>;


  private intersectionObserver?: IntersectionObserver;


  // =========================================================
  // STATE
  // =========================================================

  readonly client =
    signal<Client | null>(null);

  readonly loading =
    signal(true);

  readonly error =
    signal(false);

  readonly clientEmail =
    signal('');


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngOnInit(): void {
    this.loadLoggedInClient();
  }


  ngAfterViewInit(): void {

    /*
     * The DOM is rendered progressively because the template
     * contains @if blocks.
     *
     * Therefore we wait until Angular has rendered the current
     * view before creating the observer.
     */

    this.ngZone.runOutsideAngular(() => {

      setTimeout(() => {
        this.initializeRevealObserver();
      });

    });

  }


  ngOnDestroy(): void {

    this.intersectionObserver?.disconnect();

  }


  // =========================================================
  // LOAD LOGGED-IN CLIENT
  // =========================================================

  private loadLoggedInClient(): void {

    const token =
      this.keycloak.tokenParsed;

    const email =
      token?.['email'] as string | undefined;


    if (!email) {

      console.error(
        'Client email could not be retrieved from Keycloak.'
      );

      this.loading.set(false);
      this.error.set(true);

      this.cdr.markForCheck();

      return;
    }


    this.clientEmail.set(email);

    this.loadClient(email);
  }


  // =========================================================
  // LOAD CLIENT
  // =========================================================

  private loadClient(
    email: string
  ): void {

    this.loading.set(true);
    this.error.set(false);


    this.clientService
      .getClient(email)

      .pipe(
        take(1),

        finalize(() => {

          this.loading.set(false);

          this.cdr.markForCheck();

          /*
           * The client HTML appears after the API response.
           * Recreate the observer after Angular renders it.
           */
          setTimeout(() => {
            this.initializeRevealObserver();
          });

        })
      )

      .subscribe({

        next: client => {

          this.client.set(client);

          this.error.set(false);

          this.cdr.markForCheck();

        },


        error: error => {

          console.error(
            'Unable to load client details:',
            error
          );

          this.client.set(null);

          this.error.set(true);

          this.cdr.markForCheck();

        }

      });

  }


  // =========================================================
  // SCROLL REVEAL
  // =========================================================

  private initializeRevealObserver(): void {

    /*
     * Destroy the previous observer.
     */
    this.intersectionObserver?.disconnect();


    const elements =
      document.querySelectorAll<HTMLElement>(
        '.reveal'
      );


    if (!elements.length) {
      return;
    }


    this.intersectionObserver =
      new IntersectionObserver(

        entries => {

          entries.forEach(entry => {

            const element =
              entry.target as HTMLElement;


            if (entry.isIntersecting) {

              /*
               * Appears when entering viewport.
               */
              element.classList.add('is-visible');

            } else {

              /*
               * Remove the class when leaving viewport.
               * This makes the animation play again when the
               * user scrolls back.
               */
              element.classList.remove('is-visible');

            }

          });

        },

        {
          threshold: 0.12,

          rootMargin:
            '0px 0px -60px 0px'

        }

      );


    elements.forEach(element => {

      this.intersectionObserver?.observe(element);

    });

  }


  // =========================================================
  // RETRY
  // =========================================================

  retry(): void {

    const email =
      this.clientEmail();


    if (!email) {

      this.loadLoggedInClient();

      return;

    }


    this.loadClient(email);

  }


  // =========================================================
  // HELPERS
  // =========================================================

  getInitials(
    name: string | undefined
  ): string {

    if (!name) {
      return '?';
    }


    return name
      .trim()
      .split(/\s+/)
      .slice(0, 2)
      .map(
        part =>
          part.charAt(0).toUpperCase()
      )
      .join('');

  }


  // =========================================================
  // FORMAT BUDGET
  // =========================================================

  formatBudget(
    budget: number | undefined
  ): string {

    if (
      budget === undefined ||
      budget === null
    ) {

      return '€0.00';

    }


    return new Intl.NumberFormat(
      'en-EU',
      {
        style: 'currency',
        currency: 'EUR'
      }
    ).format(budget);

  }


  // =========================================================
  // ROLE
  // =========================================================

  getRoleLabel(
    role: string | undefined
  ): string {

    if (!role) {
      return 'Client';
    }


    return role
      .toLowerCase()
      .replace(
        /^./,
        char =>
          char.toUpperCase()
      );

  }

}
