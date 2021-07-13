import React from 'react';
import logo from '../../../../assets/logo.svg';
import useHomeStyles from './Home.styles';

export default function Home() {
    const classes = useHomeStyles();

    return <div className={classes.home}>
        <header className={classes.homeHeader}>
            <h2>Welcome to Project Management App</h2>
            <img src={logo} className={classes.homeLogo} alt="logo" />
        </header>
    </div>
};