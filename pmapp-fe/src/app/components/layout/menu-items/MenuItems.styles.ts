import { Theme } from "@material-ui/core/styles";
import { makeStyles } from "@material-ui/styles";

export const useMenuItemsStyles = makeStyles((theme: Theme) => ({
    caption: {
        whiteSpace: 'normal',
        textAlign: 'center',
        fontSize: '13px',
        margin: '0 3px 8px 3px'
    },
    title: {
        whiteSpace: 'normal',
        textAlign: 'center',
        marginRight: '15px'
    }
}));