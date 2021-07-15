import environment from './environment'

const endpoints = {
    auth: {
        login: `${environment.baseUrl}/auth/login`,
        logout: `${environment.baseUrl}/auth/logout`,
        refreshtoken: `${environment.baseUrl}/auth/refreshtoken`,
    },
    user: {
        findAll: `${environment.baseUrl}/user/all`,
    },
    project: {
        getByCode: `${environment.baseUrl}/project`,
        me: `${environment.baseUrl}/project/me`,
        findAll: `${environment.baseUrl}/project/all`,
        deleteByCode: `${environment.baseUrl}/project`,
    },
    task: {
        getByCode: `${environment.baseUrl}/task`,
        me: `${environment.baseUrl}/task/me`,
        findAll: `${environment.baseUrl}/task/all`,
        findNotAssigned: `${environment.baseUrl}/task/findNotAssigned`,
        findByProject: `${environment.baseUrl}/task/findByProject`,
        deleteByCode: `${environment.baseUrl}/task`,
    }
};

export default endpoints;