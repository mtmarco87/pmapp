import { AppBar, Badge, Box, Container, CssBaseline, Divider, Drawer, IconButton, List, Toolbar, Typography } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import NotificationsIcon from '@material-ui/icons/Notifications';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import clsx from 'clsx';
import { BottomMenuItems, UpperMenuItems } from '../menu-items/MenuItems';
import { useAppSelector } from '../../../redux/hooks';
import { selectIsAuthenticated, selectLoggedUser } from '../../../redux/slices/sessionSlice';
import useDashboardStyles from './Dashboard.styles';
import { useState } from 'react';
import Footer from '../footer/Footer';
import Routes from '../../../../Routes';

export default function Dashboard() {
    const isAuthenticated = useAppSelector(selectIsAuthenticated);
    const loggedUser = useAppSelector(selectLoggedUser);
    const classes = useDashboardStyles();
    const [drawerOpen, setDrawerOpen] = useState(true);

    const handleDrawerOpen = () => {
        setDrawerOpen(true);
    };
    const handleDrawerClose = () => {
        setDrawerOpen(false);
    };

    return (
        <div className={classes.root}>
            <CssBaseline />
            {/* Top bar */}
            <AppBar position="absolute" className={clsx(classes.appBar, drawerOpen && classes.appBarShift)}>
                <Toolbar className={classes.toolbar}>
                    <IconButton
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        className={clsx(classes.menuButton, drawerOpen && classes.menuButtonHidden)}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
                        Project Management App
                    </Typography>
                    {loggedUser ?
                        <Typography color="inherit" noWrap>
                            [{loggedUser?.name} {loggedUser?.surname} / {loggedUser?.email} / {loggedUser?.role}]
                        </Typography>
                        :
                        <></>
                    }
                    <IconButton color="inherit">
                        <Badge badgeContent={0} color="secondary">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton>
                </Toolbar>
            </AppBar>

            { /* Side bar */}
            <Drawer
                variant="permanent"
                classes={{
                    paper: clsx(classes.drawerPaper, !drawerOpen && classes.drawerPaperClose),
                }}
                open={drawerOpen}
            >
                <div className={classes.toolbarIcon}>
                    <IconButton onClick={handleDrawerClose}>
                        <ChevronLeftIcon />
                    </IconButton>
                </div>
                <Divider />
                <List>
                    <UpperMenuItems isAuthenticated={isAuthenticated} loggedUser={loggedUser} />
                </List>
                <Divider />
                <List>
                    <BottomMenuItems isAuthenticated={isAuthenticated} loggedUser={loggedUser} />
                </List>
            </Drawer>

            {/* Components render panel */}
            <main className={classes.content}>
                <div className={classes.appBarSpacer} />
                <Container maxWidth="lg" className={classes.container}>
                    {/* Routes */}
                    <Routes />
                    <Box pt={4}>
                        <Footer />
                    </Box>
                </Container>
            </main>
        </div>
    );
}
