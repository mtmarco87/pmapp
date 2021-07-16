import { Role } from "./Role";

export interface UserDto {
    username: string | null;
    email?: string;
    name?: string;
    surname?: string;
    role?: Role;
    password?: string;
}
