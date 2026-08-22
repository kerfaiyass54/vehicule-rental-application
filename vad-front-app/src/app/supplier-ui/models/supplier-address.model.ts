import { AddressStatus } from './address-status.enum';

export interface SupplierAddress {
  idAddress: number;
  road: string;
  number: number;
  location: string;
  supplierEmail?: string;
  addressStatus?: AddressStatus;
}
