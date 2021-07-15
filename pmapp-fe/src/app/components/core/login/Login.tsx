import React, { useCallback, useState } from "react";
import { Redirect, useLocation } from "react-router-dom";
import { FromLocationState } from "../../../models/core/FromLocationState";
import { useAppDispatch, useAppSelector } from "../../../redux/hooks";
import { loginAsync, selectIsAuthenticated } from "../../../redux/slices/sessionSlice";
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Container from '@material-ui/core/Container';
import useLoginStyles from "./Login.styles";

export default function Login() {
    const { state } = useLocation<FromLocationState>();
    const { from } = state || { from: null };
    const isAuthenticated = useAppSelector(selectIsAuthenticated);
    const dispatch = useAppDispatch();
    const classes = useLoginStyles();

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const login = useCallback(() => {
        dispatch(loginAsync({
            username,
            password
        }));
    }, [dispatch, username, password]);

    if (isAuthenticated) {
        return <Redirect to={from ?? { pathname: '/' }} />;
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Avatar className={classes.avatar}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                {from ? <p>You must log in to view the page at {from.pathname}</p> : <></>}
                <form className={classes.form} noValidate>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        value={username}
                        onChange={evt => setUsername(evt.target.value)}
                        onKeyDown={e => e.key === 'Enter' && login()}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        value={password}
                        onChange={evt => setPassword(evt.target.value)}
                        onKeyDown={e => e.key === 'Enter' && login()}
                    />
                    <Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        className={classes.submit}
                        onClick={login}
                    >
                        Sign In
                    </Button>
                </form>
            </div>
            {/* <Box mt={8}>
                <Footer />
            </Box> */}
        </Container>
    );
};
