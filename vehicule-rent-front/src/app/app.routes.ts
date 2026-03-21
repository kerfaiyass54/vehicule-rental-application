// app.routes.ts
import { Routes } from '@angular/router';
import { roleGuard } from "./shared/role.guard";
import { authGuard } from "./shared/auth.guard";
import { roleRedirectGuard } from "./shared/role-redirect.guard";

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./loading-page/loading-page.component').then(m => m.LoadingPageComponent),
    canActivate: [authGuard, roleRedirectGuard]
  },

  // ─── CLIENT ───────────────────────────────────────────────
  {
    path: 'client',
    loadComponent: () => import('./client/client.component').then(m => m.ClientComponent),
    title: 'Client Dashboard',
    canActivate: [roleGuard, authGuard],
    data: { roles: ['client'] },
    children: [
      { path: '', loadComponent: () => import('./client/client-home/client-home').then(m => m.ClientHome) },
      { path: 'home', loadComponent: () => import('./client/client-home/client-home').then(m => m.ClientHome) },
      { path: 'details', loadComponent: () => import('./client/client-details/client-details.component').then(m => m.ClientDetailsComponent) },
      { path: 'update-budget/:id', loadComponent: () => import('./client/client-details/update-budget/update-budget.component').then(m => m.UpdateBudgetComponent) },
      { path: 'update/:id', loadComponent: () => import('./client/client-details/update-details/update-details.component').then(m => m.UpdateDetailsComponent) },
      { path: 'tickets', loadComponent: () => import('./client/repair-tickets/repair-tickets.component').then(m => m.RepairTicketsComponent) },
      { path: 'update-ticket/:id', loadComponent: () => import('./client/repair-tickets/update-ticket/update-ticket.component').then(m => m.UpdateTicketComponent) },
      { path: 'consult/:id', loadComponent: () => import('./client/repair-tickets/consult-ticket/consult-ticket.component').then(m => m.ConsultTicketComponent) },
      { path: 'add-ticket', loadComponent: () => import('./client/repair-tickets/add-ticket/add-ticket.component').then(m => m.AddTicketComponent) },
      { path: 'subscriptions', loadComponent: () => import('./client/subscriptions-management/subscriptions-management.component').then(m => m.SubscriptionsManagementComponent) },
      { path: 'renew-subs/:id', loadComponent: () => import('./client/subscriptions-management/renew-subscription/renew-subscription.component').then(m => m.RenewSubscriptionComponent) },
      { path: 'add-subs', loadComponent: () => import('./client/subscriptions-management/add-subscription/add-subscription.component').then(m => m.AddSubscriptionComponent) },
      { path: 'buyings', loadComponent: () => import('./client/vehicules-buyings/vehicules-buyings.component').then(m => m.VehiculesBuyingsComponent) },
      { path: 'buy/:id', loadComponent: () => import('./client/vehicules-buyings/buy-vehicule/buy-vehicule.component').then(m => m.BuyVehiculeComponent) },
      { path: 'consult-vehicule/:id', loadComponent: () => import('./client/vehicules-buyings/consult-vehicule/consult-vehicule.component').then(m => m.ConsultVehiculeComponent) },
    ]
  },

  // ─── ADMIN ────────────────────────────────────────────────
  {
    path: 'admin',
    loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent),
    title: 'Vehicule App',
    canActivate: [roleGuard, authGuard],
    data: { roles: ['admin'] },
    children: [
      { path: '', loadComponent: () => import('./admin/admin-home/admin-home.component').then(m => m.AdminHomeComponent) },
      { path: 'home', loadComponent: () => import('./admin/admin-home/admin-home.component').then(m => m.AdminHomeComponent) },
      { path: 'details', loadComponent: () => import('./admin/admin-details/admin-details.component').then(m => m.AdminDetailsComponent) },
      { path: 'update', loadComponent: () => import('./admin/admin-update/admin-update.component').then(m => m.AdminUpdateComponent) },
      { path: 'clients', loadComponent: () => import('./admin/clients-management/clients-management.component').then(m => m.ClientsManagementComponent) },
      { path: 'add-client', loadComponent: () => import('./admin/clients-management/add-client/add-client.component').then(m => m.AddClientComponent) },
      { path: 'clients/details-client/:id', loadComponent: () => import('./admin/clients-management/clients-info/clients-info.component').then(m => m.ClientsInfoComponent) },
      { path: 'update/:id', loadComponent: () => import('./admin/clients-management/update-client/update-client.component').then(m => m.UpdateClientComponent) },
      { path: 'location', loadComponent: () => import('./admin/location-management/location-management.component').then(m => m.LocationManagementComponent) },
      { path: 'add-location', loadComponent: () => import('./admin/location-management/add-location/add-location.component').then(m => m.AddLocationComponent) },
      { path: 'location/details-loc/:id', loadComponent: () => import('./admin/location-management/location-details/location-details.component').then(m => m.LocationDetailsComponent) },
      { path: 'repair', loadComponent: () => import('./admin/repair-management/repair-management.component').then(m => m.RepairManagementComponent) },
      { path: 'add-repair', loadComponent: () => import('./admin/repair-management/add-repair/add-repair.component').then(m => m.AddRepairComponent) },
      { path: 'update/repair/:id', loadComponent: () => import('./admin/repair-management/update-repair/update-repair.component').then(m => m.UpdateRepairComponent) },
      { path: 'supplier', loadComponent: () => import('./admin/supplier-management/supplier-management.component').then(m => m.SupplierManagementComponent) },
      { path: 'add-supplier', loadComponent: () => import('./admin/supplier-management/add-supplier/add-supplier.component').then(m => m.AddSupplierComponent) },
      { path: 'update/supplier/:id', loadComponent: () => import('./admin/supplier-management/update-supplier/update-supplier.component').then(m => m.UpdateSupplierComponent) },
      { path: 'infos/supplier/:id', loadComponent: () => import('./admin/supplier-management/supplier-infos/supplier-infos.component').then(m => m.SupplierInfosComponent) },
    ]
  },

  // ─── SUPPLIER ─────────────────────────────────────────────
  {
    path: 'supplier',
    loadComponent: () => import('./supplier/supplier.component').then(m => m.SupplierComponent),
    title: 'Supplier Dashboard',
    canActivate: [roleGuard, authGuard],
    data: { roles: ['supplier'] },
    children: [
      { path: '', loadComponent: () => import('./supplier/supplier-home/supplier-home').then(m => m.SupplierHome) },
      { path: 'home', loadComponent: () => import('./supplier/supplier-home/supplier-home').then(m => m.SupplierHome) },
      { path: 'adresses', loadComponent: () => import('./supplier/adresses-management/adresses-management.component').then(m => m.AdressesManagementComponent) },
      { path: 'update-adress/:id', loadComponent: () => import('./supplier/adresses-management/update-adress/update-adress.component').then(m => m.UpdateAdressComponent) },
      { path: 'add-ticket', loadComponent: () => import('./supplier/adresses-management/add-adress/add-adress.component').then(m => m.AddAdressComponent) },
      { path: 'demands', loadComponent: () => import('./supplier/demands-dashboard/demands-dashboard.component').then(m => m.DemandsDashboardComponent) },
      { path: 'categories', loadComponent: () => import('./supplier/management-categories/management-categories.component').then(m => m.ManagementCategoriesComponent) },
      { path: 'add-category', loadComponent: () => import('./supplier/management-categories/add-category/add-category.component').then(m => m.AddCategoryComponent) },
      { path: 'consult-category/:id', loadComponent: () => import('./supplier/management-categories/consult-category/consult-category.component').then(m => m.ConsultCategoryComponent) },
      { path: 'subscriptions', loadComponent: () => import('./supplier/subscriptions-details/subscriptions-details.component').then(m => m.SubscriptionsDetailsComponent) },
      { path: 'consult-subscription/:id', loadComponent: () => import('./supplier/subscriptions-details/consult-subscription/consult-subscription.component').then(m => m.ConsultSubscriptionComponent) },
      { path: 'vehicules', loadComponent: () => import('./supplier/vehicules-management/vehicules-management.component').then(m => m.VehiculesManagementComponent) },
      { path: 'add', loadComponent: () => import('./supplier/vehicules-management/add-vehicule/add-vehicule.component').then(m => m.AddVehiculeComponent) },
      { path: 'update/:id', loadComponent: () => import('./supplier/vehicules-management/update-vehicule/update-vehicule.component').then(m => m.UpdateVehiculeComponent) },
      { path: 'consult/:id', loadComponent: () => import('./supplier/vehicules-management/consult-vehicule/consult-vehicule.component').then(m => m.ConsultVehiculeComponent) },  // adjust if needed
      { path: 'consult-categories', loadComponent: () => import('./supplier/vehicules-management/consult-categories/consult-categories.component').then(m => m.ConsultCategoriesComponent) },
    ]
  },

  // ─── REPAIR ───────────────────────────────────────────────
  {
    path: 'repair',
    loadComponent: () => import('./repair/repair.component').then(m => m.RepairComponent),
    title: 'Repair Dashboard',
    canActivate: [roleGuard, authGuard],
    data: { roles: ['repair'] },
    children: [
      { path: '', loadComponent: () => import('./repair/repair-home/repair-home').then(m => m.RepairHome) },
      { path: 'home', loadComponent: () => import('./repair/repair-home/repair-home').then(m => m.RepairHome) },
      { path: 'demand', loadComponent: () => import('./repair/demand-management/demand-management.component').then(m => m.DemandManagementComponent) },
      { path: 'add-demand', loadComponent: () => import('./repair/demand-management/create-demand/create-demand.component').then(m => m.CreateDemandComponent) },
      { path: 'update/:id', loadComponent: () => import('./repair/demand-management/update-demand/update-demand.component').then(m => m.UpdateDemandComponent) },
      { path: 'consult/:id', loadComponent: () => import('./repair/demand-management/consult-demand/consult-demand.component').then(m => m.ConsultDemandComponent) },
      { path: 'details', loadComponent: () => import('./repair/repair-details/repair-details.component').then(m => m.RepairDetailsComponent) },
      { path: 'ticket', loadComponent: () => import('./repair/tickets-management/tickets-management.component').then(m => m.TicketsManagementComponent) },
      { path: 'consult-ticket/:id', loadComponent: () => import('./repair/tickets-management/consult-ticket-details/consult-ticket-details.component').then(m => m.ConsultTicketDetailsComponent) },
      { path: 'veh-repair', loadComponent: () => import('./repair/vehicules-repair/vehicules-repair.component').then(m => m.VehiculesRepairComponent) },
      { path: 'consult-repair/:id', loadComponent: () => import('./repair/vehicules-repair/consult-repair/consult-repair.component').then(m => m.ConsultRepairComponent) },
      { path: 'launch/:id', loadComponent: () => import('./repair/vehicules-repair/launch-repair/launch-repair.component').then(m => m.LaunchRepairComponent) },
    ]
  },

  // ─── OTHER ────────────────────────────────────────────────
  {
    path: 'select-role',
    loadComponent: () => import('./select-role/select-role').then(m => m.SelectRole),
    canActivate: [authGuard],
    title: 'Select role'
  },
  {
    path: 'not-authorized',
    loadComponent: () => import('./not-authorized/not-authorized.component').then(m => m.NotAuthorizedComponent),
    title: 'Access required'
  },
  {
    path: '**',
    loadComponent: () => import('./error-page/error-page.component').then(m => m.ErrorPageComponent),
    title: 'Error page'
  }
];
