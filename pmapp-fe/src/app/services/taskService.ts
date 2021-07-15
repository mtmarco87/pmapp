import axios, { AxiosResponse } from 'axios';
import endpoints from "../constants/endpoints";
import { TaskDto } from "../models/dtos/TaskDto";

export const taskService = {
    GetByCode: (code: number): Promise<AxiosResponse<TaskDto>> => {
        return axios.get(endpoints.task.getByCode(code));
    },
    FindAll: (): Promise<AxiosResponse<TaskDto[]>> => {
        return axios.get(endpoints.task.findAll);
    },
    Me: (): Promise<AxiosResponse<TaskDto[]>> => {
        return axios.get(endpoints.task.me);
    },
    FindNotAssigned: (): Promise<AxiosResponse<TaskDto[]>> => {
        return axios.get(endpoints.task.findNotAssigned);
    },
    FindByProject: (projectCode: number): Promise<AxiosResponse<TaskDto[]>> => {
        return axios.get(endpoints.task.findByProject(projectCode));
    },
    Update: (task: TaskDto): Promise<AxiosResponse<TaskDto>> => {
        return axios.put(endpoints.task.updateByCode(task.code), task);
    },
    DeleteByCode: (code: number): Promise<AxiosResponse> => {
        return axios.delete(endpoints.task.deleteByCode(code));
    }
};