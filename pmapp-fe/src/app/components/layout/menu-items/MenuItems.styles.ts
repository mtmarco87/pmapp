import { makeStyles } from "@material-ui/core";

export const useMenuItemsStyles = makeStyles((theme) => ({
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