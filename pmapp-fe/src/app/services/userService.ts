import axios, { AxiosResponse } from 'axios';
import endpoints from "../constants/endpoints";
import { UserDto } from "../models/dtos/UserDto";

export const userService = {
    Create: (user: UserDto): Promise<AxiosResponse<UserDto>> => {
        return axios.post(endpoints.user.create, user);
    },
    FindAll: (): Promise<AxiosResponse<UserDto[]>> => {
        return axios.get(endpoints.user.findAll);
    },
    Update: (user: UserDto): Promise<AxiosResponse<UserDto>> => {
        return axios.put(endpoints.user.updateByUsername(user.username!), user);
    },
    DeleteByUsername: (username: string): Promise<AxiosResponse<any>> => {
        return axios.delete(endpoints.user.deleteByUsername(username));
    }
};
