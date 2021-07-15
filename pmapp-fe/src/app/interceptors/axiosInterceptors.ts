import axios from 'axios';
import { SessionStatus } from '../models/core/SessionStatus';
import { setAccessToken, setNotification, setStatus } from '../redux/slices/sessionSlice';
import { store as AppStore } from '../redux/store';
import { authService } from '../services/authService';
import { getErrorMessage } from '../utils/Utils';

export default function setupAxiosInterceptors(store: typeof AppStore): void {
    // Request interceptor for API calls
    axios.interceptors.request.use(
        async config => {
            const accessToken = store.getState().session.accessToken;

            // If the User is logged in and an access token is available, add it to headers
            if (accessToken) {
                config.headers = {
                    'Authorization': `Bearer ${accessToken}`,
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                }
            }
            return config;
        },
        error => {
            Promise.reject(error)
        });

    // Response interceptor for API calls
    axios.interceptors.response.use(
        (response) => response,
        async (error) => {
            console.log(error);
            if (error.response.status === 401) {
                // In case of 401 (Unauthorized) user login credentials are expired or the user is not logged in
                const originalRequest = error.config;
                if (error.response.headers['auth-status'] === 'token-expired' && !originalRequest._retry) {
                    // in the first case we try to refresh the token and retry the request
                    const refreshedToken = await (await authService.RefreshToken())?.data?.accessToken ?? null;
                    store.dispatch(setAccessToken(refreshedToken));
                    return axios(originalRequest);
                } else {
                    // else we dispatch an action to the store to logout and redirect to login showing an error message
                    store.dispatch(setStatus(SessionStatus.Unauthorized, true));
                }
            }
            else if (error.response.status === 403) {
                // In case of 403 (Forbidden) user tried to access resources forbidden to its role
                // so we dispatch an action to the store to just show an error message
                store.dispatch(setStatus(SessionStatus.Forbidden));
            } else {
                // In case of any other error we show an error notification to the user
                store.dispatch(setNotification({
                    message: getErrorMessage(error),
                    type: 'error'
                }));
            }

            return Promise.reject(error);
        }
    );
};
