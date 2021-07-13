import environment from './environment'

const endpoints = {
    auth: {
        login: `${environment.baseUrl}/auth/login`,
        logout: `${environment.baseUrl}/auth/logout`
    },
    user: {
        getAll: `${environment.baseUrl}/user/all`
    }
};

export default endpoints;