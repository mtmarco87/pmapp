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
    }
}));

export default useUsersStyles;