import axios, { AxiosResponse } from 'axios';
import endpoints from "../constants/endpoints";
import { ProjectDto } from "../models/dtos/ProjectDto";

export const projectService = {
    Create: (project: ProjectDto): Promise<AxiosResponse<ProjectDto>> => {
        return axios.post(endpoints.project.create, project);
    },
    FindAll: (): Promise<AxiosResponse<ProjectDto[]>> => {
        return axios.get(endpoints.project.findAll);
    },
    GetByCode: (code: number): Promise<AxiosResponse<ProjectDto>> => {
        return axios.get(endpoints.project.deleteByCode(code));
    },
    Me: (): Promise<AxiosResponse<ProjectDto[]>> => {
        return axios.get(endpoints.project.me);
    },
    Update: (project: ProjectDto): Promise<AxiosResponse<ProjectDto>> => {
        return axios.put(endpoints.project.updateByCode(project.code!), project);
    },
    DeleteByCode: (code: number): Promise<AxiosResponse<any>> => {
        return axios.delete(endpoints.project.deleteByCode(code));
    }
};