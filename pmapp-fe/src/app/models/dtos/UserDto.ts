import { Role } from "./Role";

export interface UserDto {
    username: string;
    email: string;
    name: string;
    surname: string;
    role: Role;
    password: string;
}
