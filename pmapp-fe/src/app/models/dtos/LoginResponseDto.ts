import { UserDto } from './UserDto';

export interface LoginResponseDto {
    accessToken?: string | null;
    tokenType?: string | null;
    user?: UserDto | null;
}
