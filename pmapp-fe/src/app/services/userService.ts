import axios, { AxiosResponse } from 'axios';
import endpoints from "../constants/endpoints";
import { UserDto } from "../models/dtos/UserDto";

export const userService = {
    FindAll: (): Promise<AxiosResponse<UserDto[]>> => {
        return axios.get(endpoints.user.findAll);
    }
};