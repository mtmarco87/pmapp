import axios, { AxiosResponse } from "axios";
import endpoints from "../constants/endpoints";
import { LoginRequestDto } from "../models/dtos/LoginRequestDto";
import { LoginResponseDto } from "../models/dtos/LoginResponseDto";

export const authService = {
    Login: (loginReq: LoginRequestDto): Promise<AxiosResponse<LoginResponseDto>> => {
        return axios.post(endpoints.auth.login, loginReq);
    },
    Logout: (): Promise<AxiosResponse> => {
        return axios.post(endpoints.auth.logout);
    }
};