import { AddressStatus } from './address-status.enum';

export interface CreateSupplierAddress {
  idAddress?: number;
  road: string;
  number: number;
  location: string;
  supplierEmail: string;
  addressStatus?: AddressStatus;
}
