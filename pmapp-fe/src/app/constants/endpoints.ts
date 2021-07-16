import environment from './environment'

const endpoints = {
    auth: {
        login: `${environment.baseUrl}/auth/login`,
        logout: `${environment.baseUrl}/auth/logout`,
        refreshtoken: `${environment.baseUrl}/auth/refreshtoken`,
    },
    user: {
        create: `${environment.baseUrl}/user`,
        findAll: `${environment.baseUrl}/user/all`,
        updateByUsername: (username: string) => `${environment.baseUrl}/user/${username}`,
        deleteByUsername: (username: string) => `${environment.baseUrl}/user/${username}`,
    },
    project: {
        create: `${environment.baseUrl}/project`,
        getByCode: (code: number) => `${environment.baseUrl}/project/${code}`,
        me: `${environment.baseUrl}/project/me`,
        findAll: `${environment.baseUrl}/project/all`,
        updateByCode: (code: number) => `${environment.baseUrl}/project/${code}`,
        deleteByCode: (code: number) => `${environment.baseUrl}/project/${code}`,
    },
    task: {
        create: `${environment.baseUrl}/task`,
        getByCode: (code: number) => `${environment.baseUrl}/task/${code}`,
        me: `${environment.baseUrl}/task/me`,
        findAll: `${environment.baseUrl}/task/all`,
        findNotAssigned: `${environment.baseUrl}/task/findNotAssigned`,
        findByProject: (projectCode: number) => `${environment.baseUrl}/task/findByProject/${projectCode}`,
        updateByCode: (code: number) => `${environment.baseUrl}/task/${code}`,
        deleteByCode: (code: number) => `${environment.baseUrl}/task/${code}`,
    }
};

export default endpoints;