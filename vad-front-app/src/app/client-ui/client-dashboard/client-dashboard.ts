import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnDestroy,
  QueryList,
  ViewChildren
} from '@angular/core';
import {ClientDetails} from './client-details/client-details';
import {ClientLocation} from './client-location/client-location';
import {ClientStats} from './client-stats/client-stats';


@Component({
  selector: 'app-client-dashboard',
  standalone: true,
  imports: [
    ClientDetails,
    ClientLocation,
    ClientStats
  ],
  templateUrl: './client-dashboard.html',
  styleUrl: './client-dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ClientDashboard implements AfterViewInit, OnDestroy {

  @ViewChildren('dashboardSection', {
    read: ElementRef
  })
  private sections!: QueryList<ElementRef<HTMLElement>>;

  private observer?: IntersectionObserver;


  // =========================================================
  // LIFECYCLE
  // =========================================================

  ngAfterViewInit(): void {
    this.initializeScrollAnimations();
  }


  ngOnDestroy(): void {
    this.observer?.disconnect();
  }


  // =========================================================
  // SCROLL ANIMATION
  // =========================================================

  private initializeScrollAnimations(): void {

    if (typeof IntersectionObserver === 'undefined') {
      this.sections.forEach(section => {
        section.nativeElement.classList.add('is-visible');
      });

      return;
    }

    this.observer = new IntersectionObserver(
      entries => {

        entries.forEach(entry => {

          const element =
            entry.target as HTMLElement;

          if (entry.isIntersecting) {

            element.classList.add('is-visible');

          } else {

            element.classList.remove('is-visible');

          }

        });

      },
      {
        threshold: 0.18,

        rootMargin:
          '-80px 0px -80px 0px'
      }
    );


    this.sections.forEach(section => {

      this.observer?.observe(
        section.nativeElement
      );

    });
  }
}
