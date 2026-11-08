export interface LoginResponse {
  token: string;
  type: string;
  username: string;
  roles: string[];
  expiresIn: number;
}