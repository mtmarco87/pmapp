import { Theme } from "@material-ui/core/styles";
import { makeStyles } from "@material-ui/styles";

const useUsersStyles = makeStyles((theme: Theme) => ({
    paper: {
        padding: theme.spacing(2),
        display: 'flex',
        overflow: 'auto',
        flexDirection: 'column',
    },
    fixedHeight: {
        height: 240,
    },
    actionButtons: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'center',
        width: '100%'
    },
    button: {
        maxHeight: 50,
        margin: 'auto 0 auto 0'
    },
    header: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
    }
}));

export default useUsersStyles;