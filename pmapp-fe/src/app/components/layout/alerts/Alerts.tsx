import { Snackbar } from "@material-ui/core";
import { Alert } from "@material-ui/lab";
import { SessionStatus } from "../../../models/core/SessionStatus";
import { useAppDispatch, useAppSelector } from "../../../redux/hooks";
import { selectSessionStatus, setStatus } from "../../../redux/slices/sessionSlice";

export default function Alerts() {
    const sessionStatus = useAppSelector(selectSessionStatus);
    const dispatch = useAppDispatch();

    const handleSnackbarClose = () => {
        dispatch(setStatus(SessionStatus.None));
    };

    return (
        <>
            <Snackbar
                open={sessionStatus === SessionStatus.Unauthorized}
                autoHideDuration={3000}
                onClose={handleSnackbarClose}
            >
                <Alert severity="error" onClose={handleSnackbarClose}>
                    Invalid credentials! Please try again to login.
                </Alert>
            </Snackbar>
            <Snackbar
                open={sessionStatus === SessionStatus.Forbidden}
                autoHideDuration={3000}
                onClose={handleSnackbarClose}
            >
                <Alert severity="error" onClose={handleSnackbarClose}>
                    You tried to access a forbidden resource.
                </Alert>
            </Snackbar>
            <Snackbar
                open={sessionStatus === SessionStatus.Authenticated}
                autoHideDuration={3000}
                onClose={handleSnackbarClose}
            >
                <Alert severity="success" onClose={handleSnackbarClose}>
                    Successfully logged in!
                </Alert>
            </Snackbar>
        </>
    );
};
