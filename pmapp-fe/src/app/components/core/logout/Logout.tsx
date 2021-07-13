import React, { useEffect } from 'react';
import { Redirect } from 'react-router';
import { useAppDispatch, useAppSelector } from '../../../redux/hooks';
import { logoutAsync, selectIsAuthenticated } from '../../../redux/slices/sessionSlice';

export default function Logout() {
    const isAuthenticated = useAppSelector(selectIsAuthenticated);
    const dispatch = useAppDispatch();

    useEffect(() => {
        dispatch(logoutAsync());
    }, [dispatch]);

    if (!isAuthenticated) {
        return <Redirect to={{ pathname: '/' }} />;
    }

    return <div>Logging out...</div>;
};