import { Typography } from "@material-ui/core";
import MatLink from '@material-ui/core/Link';

export default function Footer() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <MatLink color="inherit" href="/">
                PmApp
            </MatLink>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}
