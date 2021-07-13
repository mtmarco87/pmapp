import { makeStyles } from "@material-ui/core";

const useHomeStyles = makeStyles((theme) => ({
    home: {
        textAlign: 'center',
        minHeight: '100%',
    },

    homeLogo: {
        height: '40vmin',
        pointerEvents: 'none',
        animation: `$homeLogoFloat  infinite 3s ${theme.transitions.easing.easeInOut}`
    },
    homeHeader: {
        minHeight: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        fontSize: 'calc(10px + 2vmin)'
    },
    "@keyframes homeLogoFloat": {
        "0%": {
            transform: "translateY(0)"
        },
        "50%": {
            transform: "translateY(10px)"
        },
        "100%": {
            transform: "translateY(0px)"
        }
    }
}));

export default useHomeStyles;