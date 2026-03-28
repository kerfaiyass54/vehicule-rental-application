export interface Session {
  id: string;
  userId: string;
  username: string;
  email: string;
  sessionStart: string;
  ipAddress: string;
  userAgent: string;
  deviceType: string;
  country: string;
  city: string;
  riskScore: number;
  suspicious: boolean;
  suspiciousReason: string;
}
