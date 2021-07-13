import axios from 'axios';
import { SessionStatus } from '../models/core/SessionStatus';
import { setStatus } from '../redux/slices/sessionSlice';
import { store as AppStore } from '../redux/store';

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
                // In case of 401 (Unauthorized) user login credentials are expired or the user is not logged in,
                // so we dispatch an action to the store to logout and redirect to login showing an error message
                store.dispatch(setStatus(SessionStatus.Unauthorized, true));
            }
            else if (error.response.status === 403) {
                // In case of 403 (Forbidden) user tried to access resources forbidden to its role
                // so we dispatch an action to the store to just show an error message
                store.dispatch(setStatus(SessionStatus.Forbidden));
            }

            return Promise.reject(error);
        }
    );
};
