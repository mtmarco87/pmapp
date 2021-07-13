import React from "react";
import { Redirect, Route, useLocation } from "react-router-dom";
import { CurrentLocationState } from "../../../models/core/CurrentLocationState";
import { SessionStatus } from "../../../models/core/SessionStatus";
import { useAppSelector } from "../../../redux/hooks";
import { selectIsAuthenticated, selectSessionStatus } from "../../../redux/slices/sessionSlice";

const PrivateRoute = ({ Component, ...rest }: { Component: Function, [restKey: string]: any }) => {
    const location = useLocation<CurrentLocationState>();
    const isAuthenticated = useAppSelector(selectIsAuthenticated);
    const isUnauthorized = useAppSelector(selectSessionStatus) === SessionStatus.Unauthorized;
    const isForbidden = useAppSelector(selectSessionStatus) === SessionStatus.Forbidden;

    const showComponent = isAuthenticated && !isUnauthorized && !isForbidden;

    return (
        <Route {...rest}>
            {showComponent && <Component />}
            {(!isAuthenticated || isUnauthorized) && <Redirect to={{ pathname: "/login", state: { from: location } }} />}
            {isForbidden && <Redirect to={{ pathname: "/", state: { from: location } }} />}
        </Route>
    );
};

export default PrivateRoute;