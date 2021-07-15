import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { SessionStatus } from "../../../models/core/SessionStatus";
import { useAppDispatch, useAppSelector } from "../../../redux/hooks";
import { selectNotification, selectSessionStatus, setNotification, setStatus } from "../../../redux/slices/sessionSlice";

export default function Alerts() {
    const sessionStatus = useAppSelector(selectSessionStatus);
    const notification = useAppSelector(selectNotification);
    const dispatch = useAppDispatch();

    const handleSessionStatusClose = () => {
        dispatch(setStatus(SessionStatus.None));
    };

    const handleNotificationClose = () => {
        dispatch(setNotification(null));
    };

    return (
        <>
            <Snackbar
                open={sessionStatus === SessionStatus.Unauthorized}
                autoHideDuration={3000}
                onClose={handleSessionStatusClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center'
                }}
            >
                <Alert severity="error" onClose={handleSessionStatusClose}>
                    Invalid credentials! Please try again to login.
                </Alert>
            </Snackbar>
            <Snackbar
                open={sessionStatus === SessionStatus.Forbidden}
                autoHideDuration={3000}
                onClose={handleSessionStatusClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center'
                }}
            >
                <Alert severity="error" onClose={handleSessionStatusClose}>
                    You tried to access a forbidden resource.
                </Alert>
            </Snackbar>
            <Snackbar
                open={sessionStatus === SessionStatus.Authenticated}
                autoHideDuration={3000}
                onClose={handleSessionStatusClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center'
                }}
            >
                <Alert severity="success" onClose={handleSessionStatusClose}>
                    Successfully logged in!
                </Alert>
            </Snackbar>
            <Snackbar
                open={!!notification}
                autoHideDuration={3000}
                onClose={handleNotificationClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'center'
                }}
            >
                <Alert severity={notification?.type} onClose={handleNotificationClose}>
                    {notification?.message}
                </Alert>
            </Snackbar>
        </>
    );
};
