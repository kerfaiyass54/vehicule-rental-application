export interface Session {
  id: string;
  ipAddress: string;
  started: Date;
  lastAccess: Date;
  browser: string;
  current: boolean;
}
